package demo.server.artist

import java.util.UUID

import demo.core.api.Artist

import scala.concurrent.Future
import scala.util.{Failure, Try}

/**
  * Created by hollinwilkins on 3/29/16.
  */
class ArtistDatabase() {
  var artists: Map[UUID, Artist] = Map()

  def createArtist(artist: Artist): Future[Artist] = {
    val artist2 = artist.copy(uid = Some(UUID.randomUUID()))

    this.synchronized {
      artists += (artist2.uid.get -> artist2)
    }

    Future.fromTry(Try(artist2))
  }

  def readArtist(uid: UUID): Future[Artist] = {
    this.synchronized {
      artists.get(uid) match {
        case Some(artist) => Future.fromTry(Try(artist))
        case None => Future.fromTry(Failure(new Error(s"Could not find artist for uid: $uid")))
      }
    }
  }
}
