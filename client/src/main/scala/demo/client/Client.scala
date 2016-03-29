package demo.client

import akka.stream.Materializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import demo.core.api._

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by hollinwilkins on 3/28/16.
  */
trait Client {
  val ec: ExecutionContext
  val materializer: Materializer

  val createArtistFlow: Flow[CreateArtistRequest, Future[CreateArtistResponse], Any]
  val readArtistFlow: Flow[ReadArtistRequest, Future[ReadArtistResponse], Any]
  val createSongFlow: Flow[CreateSongRequest, Future[CreateSongResponse], Any]

  def createArtist(request: CreateArtistRequest): Future[CreateArtistResponse] = run(createArtistFlow)(request)
  def readArtist(request: ReadArtistRequest): Future[ReadArtistResponse] = run(readArtistFlow)(request)
  def createSong(request: CreateSongRequest): Future[CreateSongResponse] = run(createSongFlow)(request)

  private def run[Request, Response](flow: Flow[Request, Future[Response], Any])
                                    (request: Request): Future[Response] = {
    Source.single(request)
      .via(flow)
      .runWith(Sink.head)(materializer)
      .flatMap(identity)(ec)
  }
}
