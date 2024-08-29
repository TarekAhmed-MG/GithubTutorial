package models

import play.api.libs.json.{Json, OFormat}

import java.nio.charset.StandardCharsets
import java.util.Base64

case class FileModel(name:String,content:String){

  private val contentDecoder = Base64.getMimeDecoder.decode(content)
  private val fileContent = new String(contentDecoder, StandardCharsets.UTF_8)

  val fileModel: FileModelDTO = FileModelDTO(name,fileContent)

}

object FileModel {
  implicit val formats: OFormat[FileModel] = Json.format[FileModel]
}
