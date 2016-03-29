package demo.core.serialization

import java.util.UUID

import demo.core.api._
import spray.json.DefaultJsonProtocol._
import spray.json.{JsString, JsValue, JsonFormat}

/**
  * Created by hollinwilkins on 3/28/16.
  */
trait CoreJsonSupport {
  implicit object JavaUUIDFormat extends JsonFormat[UUID] {
    override def write(obj: UUID): JsValue = JsString(obj.toString)

    override def read(json: JsValue): UUID = json match {
      case JsString(value) => UUID.fromString(value)
      case _ => throw new Error("Invalid UUID")
    }
  }

  implicit val apiArtistFormat = jsonFormat3(Artist)
  implicit val apiSongFormat = jsonFormat4(Song)

  implicit val apiCreateArtistResponseFormat = jsonFormat1(CreateArtistResponse)
  implicit val apiReadArtistResponseFormat = jsonFormat1(ReadArtistResponse)
  implicit val apiCreateSongResponseFormat = jsonFormat1(CreateSongResponse)
}
object CoreJsonSupport extends CoreJsonSupport
