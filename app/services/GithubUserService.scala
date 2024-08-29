package services

import cats.data.EitherT
import connectors.GithubUserConnector
import models.{APIError, FileModel, RepositoriesModel, RepositoryModel, UserModel}
import play.api.libs.json._

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class GithubUserService @Inject()(connector:GithubUserConnector){

  /*
  https://api.github.com/repos/TarekAhmed-MG/GithubTutorial
  give you the entire info of a repo
  to view files/branches go into the link above and find the links they provide for you to redirect too
   */

  def getGithubUser(urlOverride: Option[String] = None, userName: String)(implicit ec: ExecutionContext):EitherT[Future, APIError, UserModel] =
    connector.get[UserModel](urlOverride.getOrElse(s"https://api.github.com/users/$userName"))

  def getGithubUserRepositories(urlOverride: Option[String] = None, userName: String)(implicit ec: ExecutionContext):EitherT[Future, APIError, List[RepositoriesModel]] =
    connector.getList[RepositoriesModel](urlOverride.getOrElse(s"https://api.github.com/users/$userName/repos"))

  def getGithubRepository(urlOverride: Option[String] = None, userName: String, repoName:String)(implicit ec: ExecutionContext):EitherT[Future, APIError, List[RepositoryModel]] =
    connector.getList[RepositoryModel](urlOverride.getOrElse(s"https://api.github.com/repos/$userName/$repoName/contents/"))

  def getGithubRepositoryDir(urlOverride: Option[String] = None,userName: String, repoName:String, path: String)(implicit ec: ExecutionContext):EitherT[Future, APIError, List[RepositoryModel]] =
    connector.getList[RepositoryModel](urlOverride.getOrElse(s"https://api.github.com/repos/$userName/$repoName/contents/$path"))

  def getGithubRepositoryFile(urlOverride: Option[String] = None,userName: String, repoName:String, path: String)(implicit ec: ExecutionContext):EitherT[Future, APIError, FileModel] =
    connector.get[FileModel](urlOverride.getOrElse(s"https://api.github.com/repos/$userName/$repoName/contents/$path"))


  //  def getGithubRepositoryFileOrDir(urlOverride: Option[String] = None,userName: String, repoName:String, path: String)(implicit ec: ExecutionContext): EitherT[Future, APIError, _ >: List[RepositoryModel] with FileModel <: Equals] ={
  //    if (path.contains(".")) {
  //      connector.getList[RepositoryModel](urlOverride.getOrElse(s"https://api.github.com/repos/$userName/$repoName/$path"))
  //    } else {
  //      connector.get[FileModel](urlOverride.getOrElse(s"https://api.github.com/repos/$userName/$repoName/$path"))
  //    }
  //  }
}
