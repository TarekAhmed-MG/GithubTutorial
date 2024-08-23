package controllers

import baseSpec.{BaseSpec, BaseSpecWithApplication}
import models.UserModel
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.Result
import play.api.test.Helpers.{await, contentAsJson, defaultAwaitTimeout, status}
import play.api.test.{FakeRequest, Injecting}
import play.api.http.Status

import scala.concurrent.Future

class ApplicationControllerSpec extends BaseSpecWithApplication {
  val TestApplicationController = new ApplicationController(component,service,repoService)

  /*
  _login:String,
  created_at:String,
  location:String,
  followers:Int,
  following:Int
   */

  private val userModel: UserModel = UserModel(
    "testName",
    "2021-10-01T09:32:22Z",
    "London",
    27,
    36
  )

  "ApplicationController .Create()" should {

    "find a user in the database by username" in {
      beforeEach()
      val request: FakeRequest[JsValue] = buildGet("/api/${userModel._login}").withBody[JsValue](Json.toJson(userModel))
      val createdResult: Future[Result] = TestApplicationController.create()(request)
      status(createdResult) shouldBe Status.CREATED
      afterEach()
    }

    "return Unable to find any users when given wrong username" in {
      beforeEach()
      val request: FakeRequest[JsValue] = buildGet("/api/${userModel._login}").withBody[JsValue](Json.toJson("BadRequest"))
      val createdResult: Future[Result] = TestApplicationController.create()(request)
      status(createdResult) shouldBe Status.BAD_REQUEST
      afterEach()
    }
  }

  "ApplicationController .Read(id:String)" should {

    "find a user in the database by username" in {
      beforeEach()
      val request: FakeRequest[JsValue] = buildGet("/api/${userModel._login}").withBody[JsValue](Json.toJson(userModel))
      val createdResult: Future[Result] = TestApplicationController.create()(request)
      status(createdResult) shouldBe Status.CREATED

      val readResult: Future[Result] = TestApplicationController.read("testName")(FakeRequest())
      status(readResult) shouldBe Status.OK
      contentAsJson(readResult).as[JsValue] shouldBe request.body
      afterEach()
    }

    "return Unable to find any users when given wrong username" in {
      beforeEach()
      val request: FakeRequest[JsValue] = buildGet("/api/${userModel._login}").withBody[JsValue](Json.toJson(userModel))
      val createdResult: Future[Result] = TestApplicationController.create()(request)
      status(createdResult) shouldBe Status.CREATED

      val readResult: Future[Result] = TestApplicationController.read("ssas")(FakeRequest())
      status(readResult) shouldBe Status.NOT_FOUND
      contentAsJson(readResult).as[JsValue] shouldBe Json.toJson("Unable to find any users")
      afterEach()
    }
  }



  "ApplicationController .Update()" should {

    /*
  _login:String,
  created_at:String,
  location:String,
  followers:Int,
  following:Int
   */

    "Update username" in {
      beforeEach()
      // start here:

      val updateUsername: UserModel = UserModel(
        "updatedName",
        "2021-10-01T09:32:22Z",
        "London",
        27,
        36
      )

      val request: FakeRequest[JsValue] = buildGet("/api/${userModel._login}").withBody[JsValue](Json.toJson(userModel))
      val createdResult: Future[Result] = TestApplicationController.create()(request)
      status(createdResult) shouldBe Status.CREATED

      val updateRequest: FakeRequest[JsValue] = buildGet("/api/${userModel._login}").withBody[JsValue](Json.toJson(updateUsername))
      val updatedResult = TestApplicationController.update("testName","_login")(updateRequest)
      status(updatedResult) shouldBe Status.ACCEPTED

      val readResult: Future[Result] = TestApplicationController.read("updatedName")(FakeRequest())
      status(readResult) shouldBe Status.OK
      contentAsJson(readResult).as[JsValue] shouldBe updateRequest.body




      afterEach()
    }

    "Update created at" in {
      beforeEach()
      // start here:

      val updateCreatedAt: UserModel = UserModel(
        "testName",
        "2024-08-23T09:32:22Z",
        "London",
        27,
        36
      )

      val request: FakeRequest[JsValue] = buildGet("/api/${userModel._login}").withBody[JsValue](Json.toJson(userModel))
      val createdResult: Future[Result] = TestApplicationController.create()(request)
      status(createdResult) shouldBe Status.CREATED

      val updateRequest: FakeRequest[JsValue] = buildGet("/api/${userModel._login}").withBody[JsValue](Json.toJson(updateCreatedAt))
      val updatedResult = TestApplicationController.update("testName","created_at")(updateRequest)
      status(updatedResult) shouldBe Status.ACCEPTED

      val readResult: Future[Result] = TestApplicationController.read("testName")(FakeRequest())
      status(readResult) shouldBe Status.OK
      contentAsJson(readResult).as[JsValue] shouldBe updateRequest.body



      afterEach()
    }

    "Update Location" in {
      beforeEach()
      // start here:

      val updateLocation: UserModel = UserModel(
        "testName",
        "2021-10-01T09:32:22Z",
        "Birmingham",
        27,
        36
      )

      val request: FakeRequest[JsValue] = buildGet("/api/${userModel._login}").withBody[JsValue](Json.toJson(userModel))
      val createdResult: Future[Result] = TestApplicationController.create()(request)
      status(createdResult) shouldBe Status.CREATED

      val updateRequest: FakeRequest[JsValue] = buildGet("/api/${userModel._login}").withBody[JsValue](Json.toJson(updateLocation))
      val updatedResult = TestApplicationController.update("testName","location")(updateRequest)
      status(updatedResult) shouldBe Status.ACCEPTED

      val readResult: Future[Result] = TestApplicationController.read("testName")(FakeRequest())
      status(readResult) shouldBe Status.OK
      contentAsJson(readResult).as[JsValue] shouldBe updateRequest.body



      afterEach()
    }

    "Update Followers" in {
      beforeEach()
      // start here:

      val updateFollowers: UserModel = UserModel(
        "testName",
        "2021-10-01T09:32:22Z",
        "London",
        10,
        36
      )

      val request: FakeRequest[JsValue] = buildGet("/api/${userModel._login}").withBody[JsValue](Json.toJson(userModel))
      val createdResult: Future[Result] = TestApplicationController.create()(request)
      status(createdResult) shouldBe Status.CREATED

      val updateRequest: FakeRequest[JsValue] = buildGet("/api/${userModel._login}").withBody[JsValue](Json.toJson(updateFollowers))
      val updatedResult = TestApplicationController.update("testName","followers")(updateRequest)
      status(updatedResult) shouldBe Status.ACCEPTED

      val readResult: Future[Result] = TestApplicationController.read("testName")(FakeRequest())
      status(readResult) shouldBe Status.OK
      contentAsJson(readResult).as[JsValue] shouldBe updateRequest.body



      afterEach()
    }

    "Update Following" in {
      beforeEach()
      // start here:

      val updateFollowing: UserModel = UserModel(
        "testName",
        "2021-10-01T09:32:22Z",
        "London",
        27,
        15
      )

      val request: FakeRequest[JsValue] = buildGet("/api/${userModel._login}").withBody[JsValue](Json.toJson(userModel))
      val createdResult: Future[Result] = TestApplicationController.create()(request)
      status(createdResult) shouldBe Status.CREATED

      val updateRequest: FakeRequest[JsValue] = buildGet("/api/${userModel._login}").withBody[JsValue](Json.toJson(updateFollowing))
      val updatedResult = TestApplicationController.update("testName","following")(updateRequest)
      status(updatedResult) shouldBe Status.ACCEPTED

      val readResult: Future[Result] = TestApplicationController.read("testName")(FakeRequest())
      status(readResult) shouldBe Status.OK
      contentAsJson(readResult).as[JsValue] shouldBe updateRequest.body


      afterEach()
    }
  }

  "ApplicationController .Delete()" should {

    "delete a user in the database by username" in {
      beforeEach()
      val request: FakeRequest[JsValue] = buildGet("/api/${userModel._login}").withBody[JsValue](Json.toJson(userModel))
      val createdResult: Future[Result] = TestApplicationController.create()(request)
      status(createdResult) shouldBe Status.CREATED

      val deletedResult = TestApplicationController.delete("testName")(request)
      status(deletedResult) shouldBe Status.ACCEPTED
      afterEach()
    }

    "return Unable to delete" in {
      beforeEach()
      val request: FakeRequest[JsValue] = buildGet("/api/${userModel._login}").withBody[JsValue](Json.toJson(userModel))
      val createdResult: Future[Result] = TestApplicationController.create()(request)
      status(createdResult) shouldBe Status.CREATED

      val deletedResult = TestApplicationController.delete("unknownUser")(request)
      status(deletedResult) shouldBe Status.NOT_FOUND
      afterEach()
    }
  }

  override def beforeEach(): Unit = await(repository.deleteAll())
  override def afterEach(): Unit = await(repository.deleteAll())

}
