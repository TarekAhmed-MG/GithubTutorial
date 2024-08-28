package models

import cats.implicits.toFunctorOps
import org.bson.json.JsonObject
import play.api.libs.json.{JsPath, Json, OFormat, OWrites, Reads}

case class RepositoryModel(name:String)

object RepositoryModel{
  implicit val formats: OFormat[RepositoryModel] = Json.format[RepositoryModel]

//  // Custom Reads to extract just the "name" field
//  implicit val reads: Reads[RepositoryModel] = (JsPath \ "name").read[String].map(RepositoryModel.apply)
//
//  // You can still define the default format if you need it for Writes
//  implicit val writes: OWrites[RepositoryModel] = Json.writes[RepositoryModel]
//
//  // Combining Reads and Writes into an OFormat
//  implicit val formats: OFormat[RepositoryModel] = OFormat(reads, writes)
}
