package connectors

import baseSpec.{BaseSpec, BaseSpecWithApplication}
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock._
import com.github.tomakehurst.wiremock.core.WireMockConfiguration._
import models.{APIError, UserModel}
import play.api.http.Status
import play.api.libs.json.{JsValue, Json}
import play.api.libs.ws.WSClient
import play.api.mvc.Result
import play.api.test.FakeRequest
import play.api.test.Helpers.status

import java.util.concurrent.TimeUnit
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration


class GithubUserConnectorSpec extends BaseSpecWithApplication {
  val WSClient: WSClient = app.injector.instanceOf[WSClient]
  val testGithubUserConnector = new GithubUserConnector(WSClient)

  val Port = 9000
  val Host = "localhost"
  val wireMockServer = new WireMockServer(wireMockConfig().port(Port))

  override def beforeEach {
    wireMockServer.start()
    WireMock.configureFor(Host, Port)
  }

  override def afterEach {
    wireMockServer.stop()
  }

  val expectedJson: String = Json.obj(
    "login" -> "TarekAhmed-MG",
    "created_at" -> "2024-06-26T09:21:17Z",
    "location" -> "London",
    "followers" -> 2,
    "following" -> 3
  ).toString()

  "testGithubUserConnector.get should return usermodel " in {
    val path = "/github/users/TarekAhmed-MG"
    stubFor(get(urlEqualTo(path))
      .willReturn(
        aResponse()
          .withStatus(200)
          .withBody(expectedJson)))
    val request = s"http://$Host:$Port$path"
    val responseFuture = testGithubUserConnector.get[UserModel](request)

    val response = Await.result(responseFuture.value, Duration(100, TimeUnit.MILLISECONDS))
      response.toOption.get.login shouldBe "TarekAhmed-MG"
      response.toOption.get.created_at shouldBe "2024-06-26T09:21:17Z"
      response.toOption.get.location shouldBe "London"
      response.toOption.get.followers shouldBe 2
      response.toOption.get.following shouldBe 3
  }
}
