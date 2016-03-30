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

  def createArtistFlow[Context]: Flow[(CreateArtistRequest, Context),
    (Future[CreateArtistResponse], Context), Any]
  def readArtistFlow[Context]: Flow[(ReadArtistRequest, Context),
    (Future[ReadArtistResponse], Context), Any]
  def createSongFlow[Context]: Flow[(CreateSongRequest, Context),
    (Future[CreateSongResponse], Context), Any]
  def listSongsFlow[Context]: Flow[(ListSongsRequest, Context),
    (Future[ListSongsResponse], Context), Any]

  private val defaultCreateArtistFlow = createArtistFlow[Any]
  private val defaultReadArtistFlow = readArtistFlow[Any]
  private val defaultCreateSongFlow = createSongFlow[Any]
  private val defaultListSongsFlow = listSongsFlow[Any]

  def createArtist(request: CreateArtistRequest): Future[CreateArtistResponse] =
    run(defaultCreateArtistFlow)(request)
  def readArtist(request: ReadArtistRequest): Future[ReadArtistResponse] =
    run(defaultReadArtistFlow)(request)
  def createSong(request: CreateSongRequest): Future[CreateSongResponse] =
    run(defaultCreateSongFlow)(request)
  def listSongs(request: ListSongsRequest): Future[ListSongsResponse] =
    run(defaultListSongsFlow)(request)

  private def run[Request, Response](flow: Flow[(Request, Any), (Future[Response], Any), Any])
                                    (request: Request): Future[Response] = {
    Source.single((request, 42))
      .via(flow)
      .runWith(Sink.head)(materializer)
      .flatMap(_._1)(ec)
  }
}
