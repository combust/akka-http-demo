package demo.server.artist

import demo.core.api.{CreateArtistRequest, CreateArtistResponse, ReadArtistRequest, ReadArtistResponse}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by hollinwilkins on 3/29/16.
  */
case class ArtistService(artistDb: ArtistDatabase)
                        (implicit ec: ExecutionContext) {
  def createArtist(request: CreateArtistRequest): Future[CreateArtistResponse] = {
    artistDb.createArtist(request.artist)
      .map(CreateArtistResponse)
  }

  def readArtist(request: ReadArtistRequest): Future[ReadArtistResponse] = {
    artistDb.readArtist(request.uid)
      .map(ReadArtistResponse)
  }
}
