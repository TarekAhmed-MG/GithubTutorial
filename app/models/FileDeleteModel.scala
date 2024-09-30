package models

import play.api.libs.json.{Json, OFormat}

case class FileDeleteModel(message: String, sha: String)

object FileDeleteModel{
  implicit val formats: OFormat[FileDeleteModel] = Json.format[FileDeleteModel]
}

