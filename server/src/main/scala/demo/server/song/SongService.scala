package demo.server.song

import demo.core.api.{CreateSongRequest, CreateSongResponse, ReadSongsRequest, ReadSongsResponse}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by hollinwilkins on 3/29/16.
  */
case class SongService(songDb: SongDatabase)
                      (implicit ec: ExecutionContext) {
  def createSong(request: CreateSongRequest): Future[CreateSongResponse] = {
    songDb.createSong(request.song)
      .map(CreateSongResponse)
  }

  def readSongs(request: ReadSongsRequest): Future[ReadSongsResponse] = {
    songDb.songsForArtist(request.artistSlug)
      .map(ReadSongsResponse)
  }
}
