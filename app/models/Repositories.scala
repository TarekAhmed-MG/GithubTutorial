package models

import play.api.libs.json.{Json, OFormat}

case class Repositories(
                         repository:Seq[RepositoryModel]
                       )

object Repositories{
  implicit val formats: OFormat[Repositories] = Json.format[Repositories]
}


