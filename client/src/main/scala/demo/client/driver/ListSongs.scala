package demo.client.driver

import com.typesafe.config.Config
import demo.client.Client
import demo.core.api.ListSongsRequest

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Created by hollinwilkins on 3/30/16.
  */
class ListSongs() extends Command {
  override def execute(client: Client, config: Config): Unit = {
    val artistSlug = config.getString("artistSlug")
    val future = client.listSongs(ListSongsRequest(artistSlug))

    val result = Await.result(future, 5.seconds)
    println("Songs:\n" + result.songs.map(_.name).mkString("\n"))
  }
}
