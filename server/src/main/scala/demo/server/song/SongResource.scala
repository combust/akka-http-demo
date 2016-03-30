package demo.server.song

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import demo.core.api.{CreateSongRequest, ListSongsRequest, Song}
import demo.core.serialization.CoreJsonSupport._

/**
  * Created by hollinwilkins on 3/29/16.
  */
case class SongResource(service: SongService) {
  val routes = path("artists" / Segment / "songs") {
    artistSlug =>
      post {
        entity(as[Song]) {
          song =>
            val song2 = song.copy(artistSlug = Some(artistSlug))
            complete(service.createSong(CreateSongRequest(song2)))
        }
      } ~ get {
        complete(service.listSongs(ListSongsRequest(artistSlug)))
      }
  }
}
