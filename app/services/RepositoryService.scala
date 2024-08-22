package services

import models.{APIError, UserModel}
import org.mongodb.scala.result.{DeleteResult, InsertOneResult, UpdateResult}
import repositories.dataRepositoryTrait

import javax.inject.Inject
import scala.concurrent.Future

class RepositoryService @Inject()(val dataRepositoryTrait: dataRepositoryTrait){

  // index()

  def create(user:UserModel): Future[Either[APIError.BadAPIResponse, InsertOneResult]] = dataRepositoryTrait.create(user)

  def read(id:String): Future[Either[APIError.BadAPIResponse, Some[UserModel]]] = dataRepositoryTrait.read(id)

  def update(id:String,fieldName:String,user:UserModel): Future[Either[APIError.BadAPIResponse, UpdateResult]] = dataRepositoryTrait.update(id,fieldName,book)

  def delete(id:String): Future[Either[APIError.BadAPIResponse, DeleteResult]] = dataRepositoryTrait.delete(id)

  def deleteAll(): Future[Unit] = dataRepositoryTrait.deleteAll()

}
