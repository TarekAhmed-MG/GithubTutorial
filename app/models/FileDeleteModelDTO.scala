package models

import play.api.libs.json.{Json, OFormat}

case class FileDeleteModelDTO(message:String,sha:String){
  val fileDeleteModel: FileDeleteModel = FileDeleteModel(message, sha)
}

object FileDeleteModelDTO{
  implicit val formats: OFormat[FileDeleteModelDTO] = Json.format[FileDeleteModelDTO]
}
