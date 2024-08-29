package models

import play.api.libs.json.{Json, OFormat}

case class RepositoryModel(name:String, path:String,`type`: String ,sha:String){
  val repositoryModelDTO: RepositoryModelDTO = RepositoryModelDTO(name, path,`type`,sha)
}

object RepositoryModel{
  implicit val formats: OFormat[RepositoryModel] = Json.format[RepositoryModel]
}
