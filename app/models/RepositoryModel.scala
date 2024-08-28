package models

import play.api.libs.json.{Json, OFormat}

case class RepositoryModel(name:String,`type`: String ,sha:String){
  val repositoryModelDTO = RepositoryModelDTO(name,`type`,sha)
}

object RepositoryModel{
  implicit val formats: OFormat[RepositoryModel] = Json.format[RepositoryModel]
}
