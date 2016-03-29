package demo.client.driver

import com.typesafe.config.Config
import demo.client.Client

/**
  * Created by hollinwilkins on 3/29/16.
  */
trait Command {
  def execute(client: Client, config: Config): Unit
}
