package demo.server.song

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import demo.core.api.{CreateSongRequest, ReadSongsRequest, Song}
import demo.core.serialization.CoreJsonSupport._

/**
  * Created by hollinwilkins on 3/29/16.
  */
case class SongResource(service: SongService) {
  val routes = path("artists" / JavaUUID / "songs") {
    artistUid =>
      post {
        entity(as[Song]) {
          song =>
            val song2 = song.copy(artistUid = Some(artistUid))
            complete(service.createSong(CreateSongRequest(song2)))
        }
      } ~ get {
        complete(service.readSongs(ReadSongsRequest(artistUid)))
      }
  }
}
