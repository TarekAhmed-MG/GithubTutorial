package models

import play.api.libs.json.{Json, OFormat}

case class FileRequestModel(message:String, content:String)

object FileRequestModel {
  implicit val formats: OFormat[FileRequestModel] = Json.format[FileRequestModel]
}
