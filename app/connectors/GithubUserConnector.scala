package connectors

import akka.actor.typed.delivery.internal.ProducerControllerImpl.Request
import cats.data.EitherT
import config.EnvironmentVariables
import models.APIError
import play.api.libs.json.{Json, OFormat, Writes}
import play.api.libs.ws.{WSClient, WSResponse}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class GithubUserConnector  @Inject()(ws: WSClient) {

  def get[Response](url: String)(implicit rds: OFormat[Response], ec: ExecutionContext): EitherT[Future, APIError, Response] = {
    val request = ws.url(url)
    val response = request.get()
    EitherT {
      response
        .map {
          result =>
            Right(result.json.as[Response])
        }
        .recover { case _: Throwable => // changed it from WSResponse to throwable as the recover wouldn't hit unless it was a WSRespnse
          Left(APIError.BadAPIResponse(500, "Could not connect"))
        }
    }
  }

  def getList[Response](url: String)(implicit rds: OFormat[Response], ec: ExecutionContext): EitherT[Future, APIError, List[Response]] = {
    val request = ws.url(url)
    val response = request.get()
    EitherT {
      response
        .map {
          result =>
            Right(result.json.as[List[Response]])
        }
        .recover { case _: Throwable =>
          Left(APIError.BadAPIResponse(500, "Could not connect"))
        }
    }
  }


  def put[Request,Response](url: String, body:Request)(implicit wts: Writes[Request], rds: OFormat[Response], ec: ExecutionContext): EitherT[Future, APIError, Response] = {
    val request = ws.url(url)
      .withHttpHeaders(
        "Authorization" -> s"Bearer ${EnvironmentVariables.authToken}",  // Add the Authorization header with the token
        "Accept" -> "application/vnd.github+json",
        "X-GitHub-Api-Version" -> "2022-11-28"
      )

    val response = request.put(Json.toJson(body))
    EitherT {
      response
        .map {
          result =>
            Right(result.json.as[Response])
        }
        .recover { case _: Throwable => // changed it from WSResponse to throwable as the recover wouldn't hit unless it was a WSRespnse
          Left(APIError.BadAPIResponse(500, "Could not connect"))
        }
    }
  }
}
