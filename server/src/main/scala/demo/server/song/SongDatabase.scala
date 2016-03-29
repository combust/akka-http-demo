package demo.server.song

import java.util.UUID

import demo.core.api.Song

import scala.concurrent.Future
import scala.util.{Failure, Try}

/**
  * Created by hollinwilkins on 3/29/16.
  */
class SongDatabase() {
  var songs: Map[UUID, Song] = Map()
  var songsByArtist: Map[UUID, Seq[Song]] = Map()

  def createSong(song: Song): Future[Song] = {
    val song2 = song.copy(uid = Some(UUID.randomUUID()))

    this.synchronized {
      songs += (song2.uid.get -> song2)

      songsByArtist.get(song2.artistUid.get) match {
        case Some(existingSongs) =>
          songsByArtist += (song2.artistUid.get -> (song2 +: existingSongs))
        case None =>
          songsByArtist += (song2.artistUid.get -> Seq(song2))
      }
    }

    Future.fromTry(Try(song2))
  }

  def songsForArtist(artistUid: UUID): Future[Seq[Song]] = {
    this.synchronized {
      songsByArtist.get(artistUid) match {
        case Some(artistSongs) => Future.fromTry(Try(artistSongs))
        case None => Future.fromTry(Failure(new Error(s"No songs for artist: $artistUid")))
      }
    }
  }
}
