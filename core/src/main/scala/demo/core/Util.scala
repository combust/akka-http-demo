package demo.core

/**
  * Created by hollinwilkins on 3/29/16.
  */
object Util {
  def slug(str: String): String = {
    str.replace(' ', '-').toLowerCase.replaceAll("\\s", "")
  }
}
