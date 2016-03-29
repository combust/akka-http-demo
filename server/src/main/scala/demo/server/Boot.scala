package demo.server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import demo.server.artist.{ArtistDatabase, ArtistResource, ArtistService}
import demo.server.song.{SongDatabase, SongResource, SongService}
import akka.http.scaladsl.server.Directives._

/**
  * Created by hollinwilkins on 3/29/16.
  */
object Boot extends App {
  implicit val system = ActorSystem("akka-http-demo")
  implicit val materializer = ActorMaterializer()
  implicit val ec = system.dispatcher

  val artistDb = new ArtistDatabase()
  val artistService = ArtistService(artistDb)
  val artistResource = ArtistResource(artistService)

  val songDb = new SongDatabase()
  val songService = SongService(songDb)
  val songResource = SongResource(songService)

  val routes = artistResource.routes ~ songResource.routes

  Http().bindAndHandle(routes, "localhost", 8080)
}
