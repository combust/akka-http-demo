package demo.core.serialization

import java.util.UUID

import demo.core.api._
import spray.json.DefaultJsonProtocol._
import spray.json.{JsString, JsValue, JsonFormat, RootJsonFormat}

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

  implicit val apiArtistFormat: RootJsonFormat[Artist] = jsonFormat3(Artist)
  implicit val apiSongFormat: RootJsonFormat[Song] = jsonFormat4(Song)

  implicit val apiCreateArtistResponseFormat: RootJsonFormat[CreateArtistResponse] = jsonFormat1(CreateArtistResponse)
  implicit val apiReadArtistResponseFormat: RootJsonFormat[ReadArtistResponse] = jsonFormat1(ReadArtistResponse)
  implicit val apiCreateSongResponseFormat: RootJsonFormat[CreateSongResponse] = jsonFormat1(CreateSongResponse)
  implicit val apiReadSongsResponseFormat: RootJsonFormat[ListSongsResponse] = jsonFormat1(ListSongsResponse)
}
object CoreJsonSupport extends CoreJsonSupport
