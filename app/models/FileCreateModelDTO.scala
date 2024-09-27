package models

import play.api.libs.json.{Json, OFormat}

import java.nio.charset.StandardCharsets
import java.util.Base64

case class FileCreateModelDTO(message:String, content:String){

  private val encodedContent = Base64.getMimeEncoder.encodeToString(content.getBytes(StandardCharsets.UTF_8))
  val fileCreateModel: FileCreateModel = FileCreateModel(message, encodedContent)

//  def apply(message:String,content:String): Unit = {
//    val encodedContent = Base64.getMimeEncoder.encodeToString(content.getBytes(StandardCharsets.UTF_8))
//    FileRequestModel(message, encodedContent)
//  }
}

object FileCreateModelDTO {
  implicit val formats: OFormat[FileCreateModelDTO] = Json.format[FileCreateModelDTO]
}
