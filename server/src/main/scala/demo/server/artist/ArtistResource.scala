package demo.server.artist

import akka.http.scaladsl.server.Directives._
import demo.core.api.{Artist, CreateArtistRequest, ReadArtistRequest}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import demo.core.serialization.CoreJsonSupport._

/**
  * Created by hollinwilkins on 3/29/16.
  */
case class ArtistResource(service: ArtistService) {
  val routes = path("artists") {
    post {
      entity(as[Artist]) {
        artist =>
          complete(service.createArtist(CreateArtistRequest(artist)))
      }
    }
  } ~ path("artists" / JavaUUID) {
    uid =>
      get {
        complete(service.readArtist(ReadArtistRequest(uid)))
      }
  }
}
