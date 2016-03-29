package demo.server.song

import demo.core.Util
import demo.core.api.Song

import scala.concurrent.Future
import scala.util.{Failure, Try}

/**
  * Created by hollinwilkins on 3/29/16.
  */
class SongDatabase() {
  var songs: Map[String, Song] = Map()
  var songsByArtist: Map[String, Seq[Song]] = Map()

  def createSong(song: Song): Future[Song] = {
    val slug = Util.slug(song.name)
    val song2 = song.copy(slug = Some(slug))

    this.synchronized {
      songs += (slug -> song2)

      val artistSlug = song.artistSlug.get
      songsByArtist.get(artistSlug) match {
        case Some(existingSongs) =>
          songsByArtist += (artistSlug -> (song2 +: existingSongs))
        case None =>
          songsByArtist += (artistSlug -> Seq(song2))
      }
    }

    Future.fromTry(Try(song2))
  }

  def songsForArtist(artistSlug: String): Future[Seq[Song]] = {
    this.synchronized {
      songsByArtist.get(artistSlug) match {
        case Some(artistSongs) => Future.fromTry(Try(artistSongs))
        case None => Future.fromTry(Failure(new Error(s"No songs for artist: $artistSlug")))
      }
    }
  }
}
