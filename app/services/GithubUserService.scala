package services

import cats.data.EitherT
import connectors.GithubUserConnector
import models.{APIError, RepositoryModel, UserModel}
import play.api.libs.json._

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class GithubUserService @Inject()(connector:GithubUserConnector ){

  def getGithubUser(urlOverride: Option[String] = None, userName: String)(implicit ec: ExecutionContext):EitherT[Future, APIError, UserModel] =
    connector.get[UserModel](urlOverride.getOrElse(s"https://api.github.com/users/$userName"))

  def getGithubUserRepositories(urlOverride: Option[String] = None, userName: String)(implicit ec: ExecutionContext):EitherT[Future, APIError, List[RepositoryModel]] = {
    connector.getList[RepositoryModel](urlOverride.getOrElse(s"https://api.github.com/users/$userName/repos"))
  }

}
