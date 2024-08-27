package models

import play.api.libs.json.{Json, OFormat}

case class RepositoryModel(
                            name:String
                          )

object RepositoryModel{
  implicit val formats: OFormat[RepositoryModel] = Json.format[RepositoryModel]
}
