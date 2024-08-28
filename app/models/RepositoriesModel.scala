package models

import play.api.libs.json.{Json, OFormat}

case class RepositoriesModel(name:String)

object RepositoriesModel{
  implicit val formats: OFormat[RepositoriesModel] = Json.format[RepositoriesModel]
}