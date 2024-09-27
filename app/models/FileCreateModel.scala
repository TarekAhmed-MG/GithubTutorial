package models

import play.api.libs.json.{Json, OFormat}

case class FileCreateModel(message:String, content:String)

object FileCreateModel {
  implicit val formats: OFormat[FileCreateModel] = Json.format[FileCreateModel]
}
