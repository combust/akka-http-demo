package demo.client

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.{FromEntityUnmarshaller, Unmarshal}
import akka.stream.Materializer
import akka.stream.scaladsl.{BidiFlow, Flow, Source}
import demo.core.api.{ReadArtistResponse, _}
import demo.core.serialization.CoreJsonSupport._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.marshalling.Marshal

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

/**
  * Created by hollinwilkins on 3/28/16.
  */
object HttpClient {
  def apply(host: String)
           (implicit ec: ExecutionContext,
            materializer: Materializer,
            system: ActorSystem): HttpClient = {
    val uri = Uri(host)
    HttpClient(uri)(ec, materializer, system)
  }
}

case class HttpClient(uri: Uri)
                     (override implicit val ec: ExecutionContext,
                      override implicit val materializer: Materializer,
                      implicit val system: ActorSystem) extends Client {
  lazy val hostName = uri.authority.host.address()
  lazy val port = uri.authority.port
  lazy val scheme = uri.scheme

  override def readArtistFlow[Context]: Flow[(ReadArtistRequest, Context),
    (Try[ReadArtistResponse], Context), Any] = {
    val outbound = Flow[(ReadArtistRequest, Context)].map {
      case (request, context) =>
        val httpRequest = HttpRequest(uri = s"/artists/${request.slug}",
          method = HttpMethods.GET)
        (httpRequest, context)
    }

    BidiFlow.fromFlows(outbound, inbound[ReadArtistResponse, Context])
      .join(flow[Context])
  }

  override def listSongsFlow[Context]: Flow[(ListSongsRequest, Context),
    (Try[ListSongsResponse], Context), Any] = {
    val outbound = Flow[(ListSongsRequest, Context)].map {
      case (request, context) =>
        val httpRequest = HttpRequest(uri = s"/artists/${request.artistSlug}/songs",
          method = HttpMethods.GET)
        (httpRequest, context)
    }

    BidiFlow.fromFlows(outbound, inbound[ListSongsResponse, Context])
      .join(flow[Context])
  }

  override def createArtistFlow[Context]: Flow[(CreateArtistRequest, Context),
    (Try[CreateArtistResponse], Context), Any] = {
    val outbound = Flow[(CreateArtistRequest, Context)].map {
      case (request, context) =>
        Marshal(request.artist).to[MessageEntity].map(entity => (entity, context))
    }.flatMapConcat(Source.fromFuture).map {
      case (entity, context) =>
        val httpRequest = HttpRequest(uri = "/artists",
          method = HttpMethods.POST,
          entity = entity)
        (httpRequest, context)
    }

    BidiFlow.fromFlows(outbound, inbound[CreateArtistResponse, Context])
      .join(flow[Context])
  }

  override def createSongFlow[Context]: Flow[(CreateSongRequest, Context),
    (Try[CreateSongResponse], Context), Any] = {
    val outbound = Flow[(CreateSongRequest, Context)].map {
      case (request, context) =>
        Marshal(request.song).to[MessageEntity].map(entity => (request, entity, context))
    }.flatMapConcat(Source.fromFuture).map {
      case (request, entity, context) =>
        val httpRequest = HttpRequest(uri = s"/artists/${request.song.artistSlug.get}/songs",
          method = HttpMethods.POST,
          entity = entity)
        (httpRequest, context)
    }

    BidiFlow.fromFlows(outbound, inbound[CreateSongResponse, Context])
      .join(flow[Context])
  }

  private def flow[Context]: Flow[(HttpRequest, Context),
    (Try[HttpResponse], Context), Any] = {
    scheme match {
      case "http" =>
        Http().cachedHostConnectionPool[Context](hostName, port)
      case "https" =>
        Http().cachedHostConnectionPoolHttps[Context](hostName, port)
    }
  }

  private def inbound[Response, Context]
  (implicit um: FromEntityUnmarshaller[Response]): Flow[(Try[HttpResponse], Context),
    (Try[Response], Context), Any] = {
    Flow[(Try[HttpResponse], Context)].map {
      case (tryResponse, context) =>
        tryResponse match {
          case Success(response) =>
            Unmarshal(response.entity).to[Response].map(response => (Try(response), context))
          case Failure(error) => Future.fromTry(Failure(error))
        }
    }.flatMapConcat(Source.fromFuture)
  }
}
