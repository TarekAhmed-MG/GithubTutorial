package models

import play.api.libs.json.{Json, OFormat}

import java.nio.charset.StandardCharsets
import java.util.Base64

case class FileUpdateModelDTO(message:String, content:String, sha:String){

  private val encodedContent = Base64.getMimeEncoder.encodeToString(content.getBytes(StandardCharsets.UTF_8))
  val fileUpdateModel: FileUpdateModel = FileUpdateModel(message, encodedContent, sha)

}

object FileUpdateModelDTO {
  implicit val formats: OFormat[FileUpdateModelDTO] = Json.format[FileUpdateModelDTO]
}
