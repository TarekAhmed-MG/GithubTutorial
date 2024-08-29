package repositories

import com.google.inject.ImplementedBy
import models.{APIError, UserModel}
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Updates.set
import org.mongodb.scala.model.{Filters, IndexModel, Indexes}
import org.mongodb.scala.result.{DeleteResult, InsertOneResult, UpdateResult}
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.mongo.play.json.PlayMongoRepository
import org.mongodb.scala.model.Filters.empty

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}


@ImplementedBy(classOf[DataRepository])
trait dataRepositoryTrait {
  // add method declaration here:

  // index ??
  def create(user: UserModel): Future[Either[APIError.BadAPIResponse, InsertOneResult]]
  def read(id: String): Future[Either[APIError.BadAPIResponse, Some[UserModel]]]
  def update(id: String,fieldName:String, user:UserModel): Future[Either[APIError.BadAPIResponse, UpdateResult]]
  def delete(id: String): Future[Either[APIError.BadAPIResponse, DeleteResult]]
  def deleteAll(): Future[Unit]
}

class DataRepository @Inject()(mongoComponent: MongoComponent)(implicit ec: ExecutionContext) extends PlayMongoRepository[UserModel](
  collectionName = "dataModels", // "dataModels" is the name of the collection (you can set this to whatever you like).
  mongoComponent = mongoComponent,
  domainFormat = UserModel.formats, // UserModel.formats uses the implicit val formats we created earlier. It tells the driver how to read and write between a UserModel and JSON (the format that data is stored in Mongo)
  indexes = Seq(IndexModel( //indexes is shows the structure of the data stored in Mongo, notice we can ensure the bookId to be unique
    Indexes.ascending("login")
  )),
  replaceIndexes = false
) with dataRepositoryTrait {

  override def create(user: UserModel): Future[Either[APIError.BadAPIResponse, InsertOneResult]] =
    collection.insertOne(user).toFuture().map{ createdResult =>
      if (createdResult.wasAcknowledged())
        Right(createdResult)
      else
        Left(APIError.BadAPIResponse(500, "Unable to create user"))
    }

  private def byUsername(userName: String): Bson = // change fields
      Filters.and(
        Filters.equal("login", userName)
      )

  // retrieves a UserModel object from the database. It uses an id parameter to find the data its looking for

  override def read(id: String): Future[Either[APIError.BadAPIResponse, Some[UserModel]]] =
    collection.find(byUsername(id)).headOption flatMap {
      case Some(data) => Future(Right(Some(data)))
      case None => Future(Left(APIError.BadAPIResponse(404, "Unable to find any users")))
    }

  // takes in a UserModel, finds a matching document with the same id and updates the document. It then returns the updated UserModel

  override def update(id: String,fieldName:String, user:UserModel): Future[Either[APIError.BadAPIResponse, UpdateResult]] = {

    val change = fieldName match {
      case "login" => user.login
      case "created_at" => user.created_at
      case "location" => user.location
      case "followers" => user.followers
      case "following" => user.following
    }
    collection.updateOne(filter = byUsername(id), update=set(fieldName, change)).toFuture().map{
      updatedResult =>
        if (updatedResult.getMatchedCount != 0)
          Right(updatedResult)
        else
          Left(APIError.BadAPIResponse(404, "Unable to Update User"))
    }
  }

  //  // deletes a document in the database that matches the id passed in

  override def delete(id: String): Future[Either[APIError.BadAPIResponse, DeleteResult]] =
    collection.deleteOne(filter = byUsername(id)).toFuture().map{
      deletedResult =>
        if (deletedResult.getDeletedCount != 0)
          Right(deletedResult)
        else
          Left(APIError.BadAPIResponse(404, "Unable to Delete Book"))
    }

  // is similar to delete, this removes all data from Mongo with the same collection name
  def deleteAll(): Future[Unit] = collection.deleteMany(empty()).toFuture().map(_ => ()) //Hint: needed for tests




}
