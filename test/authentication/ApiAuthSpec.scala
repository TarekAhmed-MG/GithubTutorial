package authentication

import baseSpec.BaseSpecWithApplication
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.{aResponse, get, status, stubFor, urlEqualTo}
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import connectors.GithubUserConnector
import models.UserModel
import play.api.libs.ws.WSClient

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration


class ApiAuthSpec extends BaseSpecWithApplication{
  val WSClient: WSClient = app.injector.instanceOf[WSClient]
  // stubs are used to mimic servers if you want to make a real call then you dont use wiremock
//  val testGithubUserConnector = new GithubUserConnector(WSClient)
//  val Port = 9000
//  val Host = "localhost"
//  val wireMockServer = new WireMockServer(wireMockConfig().port(Port))
  val authPassword: String = sys.env.getOrElse("AuthPassword", "")
  val owner = "TarekAhmed-MG"
  val repo = "GithubTutorial"

//  override def beforeEach {
//    wireMockServer.start()
//    WireMock.configureFor(Host, Port)
//  }
//
//  override def afterEach {
//    wireMockServer.stop()
//  }

  "get request authentication test should return 200 " in {
    val path = "README.md"
//    stubFor(get(urlEqualTo(path))
//      .willReturn(
//        aResponse()
//          .withStatus(200)
//          .withBody("""{"content": "file content"}""")
//      ))

    val request = s"https://api.github.com/repos/$owner/$repo/contents/$path"
    val responseFuture = WSClient.url(request)
      .addHttpHeaders(
        "Accept" -> "application/vnd.github+json",
        "Authorization" -> s"Bearer $authPassword",
        "X-GitHub-Api-Version" -> "2022-11-28"
      )

    val response = Await.result(responseFuture.get(), Duration(10, TimeUnit.SECONDS))
    response.status shouldBe 200
  }

  "get request authentication test should return 401 " in {
    val path = "README.md"

    val request = s"https://api.github.com/repos/$owner/$repo/contents/$path"
    val responseFuture = WSClient.url(request)
      .addHttpHeaders(
        "Accept" -> "application/vnd.github+json",
        "Authorization" -> s"Bearer noKey",
        "X-GitHub-Api-Version" -> "2022-11-28"
      )

    val response = Await.result(responseFuture.get(), Duration(10, TimeUnit.SECONDS))
    response.status shouldBe 401
  }

}
