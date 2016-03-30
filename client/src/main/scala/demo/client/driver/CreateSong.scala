package demo.client.driver

import com.typesafe.config.Config
import demo.client.Client
import demo.core.api.{CreateSongRequest, Song}

import scala.concurrent.duration._
import scala.concurrent.Await

/**
  * Created by hollinwilkins on 3/29/16.
  */
class CreateSong() extends Command {
  override def execute(client: Client, config: Config): Unit = {
    val artistSlug = config.getString("song.artistSlug")
    val name = config.getString("song.name")
    val duration = config.getInt("song.duration")
    val song = Song(artistSlug = Some(artistSlug),
      name = name,
      duration = duration)
    val future = client.createSong(CreateSongRequest(song))

    println("Creating song: " + song)
    val result = Await.result(future, 5.seconds)
    println("Created song: " + result.song)
  }
}
