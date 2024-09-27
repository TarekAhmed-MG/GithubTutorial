package models

import play.api.libs.json.{Json, OFormat}

case class FileUpdateModel(message:String, content:String, sha:String)

object FileUpdateModel {
  implicit val formats: OFormat[FileUpdateModel] = Json.format[FileUpdateModel]
}
