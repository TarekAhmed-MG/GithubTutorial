package models

import play.api.libs.json.{Json, OFormat}

import java.nio.charset.StandardCharsets
import java.util.Base64

case class FileRequestModelDTO(message:String,content:String){

  private val encodedContent = Base64.getMimeEncoder.encodeToString(content.getBytes(StandardCharsets.UTF_8))
  val fileRequestModel: FileRequestModel = FileRequestModel(message, encodedContent)

//  def apply(message:String,content:String): Unit = {
//    val encodedContent = Base64.getMimeEncoder.encodeToString(content.getBytes(StandardCharsets.UTF_8))
//    FileRequestModel(message, encodedContent)
//  }
}

object FileRequestModelDTO {
  implicit val formats: OFormat[FileRequestModelDTO] = Json.format[FileRequestModelDTO]
}
