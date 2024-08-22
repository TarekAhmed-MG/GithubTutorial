package repositories

import com.google.inject.ImplementedBy
import models.UserModel
import org.mongodb.scala.model.{IndexModel, Indexes}
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.mongo.play.json.PlayMongoRepository

import javax.inject.Inject
import scala.concurrent.ExecutionContext


@ImplementedBy(classOf[DataRepository])
trait dataRepositoryTrait {
  // add method declaration here:


}

class DataRepository @Inject()(mongoComponent: MongoComponent)(implicit ec: ExecutionContext) extends PlayMongoRepository[UserModel](
  collectionName = "dataModels", // "dataModels" is the name of the collection (you can set this to whatever you like).
  mongoComponent = mongoComponent,
  domainFormat = UserModel.formats, // DataModel.formats uses the implicit val formats we created earlier. It tells the driver how to read and write between a UserModel and JSON (the format that data is stored in Mongo)
  indexes = Seq(IndexModel( //indexes is shows the structure of the data stored in Mongo, notice we can ensure the bookId to be unique
    Indexes.ascending("login")
  )),
  replaceIndexes = false
) with dataRepositoryTrait {

  // start here:


}
