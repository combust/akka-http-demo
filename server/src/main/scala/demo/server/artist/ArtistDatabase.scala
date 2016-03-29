package demo.server.artist

import demo.core.Util
import demo.core.api.Artist

import scala.concurrent.Future
import scala.util.{Failure, Try}

/**
  * Created by hollinwilkins on 3/29/16.
  */
class ArtistDatabase() {
  var artists: Map[String, Artist] = Map()

  def createArtist(artist: Artist): Future[Artist] = {
    val slug = Util.slug(artist.name)
    val artist2 = artist.copy(slug = Some(slug))

    this.synchronized {
      artists += (slug -> artist2)
    }

    Future.fromTry(Try(artist2))
  }

  def readArtist(slug: String): Future[Artist] = {
    this.synchronized {
      artists.get(slug) match {
        case Some(artist) => Future.fromTry(Try(artist))
        case None => Future.fromTry(Failure(new Error(s"Could not find artist for slug: $slug")))
      }
    }
  }
}
