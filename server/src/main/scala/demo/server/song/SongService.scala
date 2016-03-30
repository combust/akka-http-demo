package demo.server.song

import demo.core.api.{CreateSongRequest, CreateSongResponse, ListSongsRequest, ListSongsResponse}

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

  def listSongs(request: ListSongsRequest): Future[ListSongsResponse] = {
    songDb.songsForArtist(request.artistSlug)
      .map(ListSongsResponse)
  }
}
