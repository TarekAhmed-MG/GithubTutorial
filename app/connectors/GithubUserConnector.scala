package connectors

import cats.data.EitherT
import models.APIError
import play.api.libs.json.OFormat
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
}
