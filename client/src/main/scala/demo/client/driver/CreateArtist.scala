package demo.client.driver

import com.typesafe.config.Config
import demo.client.{Client, HttpClient}
import demo.core.api.{Artist, CreateArtistRequest}

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Created by hollinwilkins on 3/29/16.
  */
class CreateArtist() extends Command {
  override def execute(client: Client, config: Config): Unit = {
    val name = config.getString("artist.name")
    val genre = config.getString("artist.genre")
    val artist = Artist(name = name, genre = genre)
    val future = client.createArtist(CreateArtistRequest(artist))

    println("Creating artist: " + artist)
    val result = Await.result(future, 5.seconds)
    println("Successfully create artist: " + result.artist)
  }
}
