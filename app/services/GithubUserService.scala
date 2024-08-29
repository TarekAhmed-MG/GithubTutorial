package services

import cats.data.EitherT
import connectors.GithubUserConnector
import models.{APIError, FileModel, RepositoriesModel, RepositoryModel, UserModel}
import play.api.libs.json._

import java.nio.charset.StandardCharsets
import java.util.Base64
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class GithubUserService @Inject()(connector:GithubUserConnector){

  /*
  https://api.github.com/repos/TarekAhmed-MG/GithubTutorial
  give you the entire info of a repo
  to view files/branches go into the link above and find the links they provide for you to redirect too
   */

  private val decoder: Array[Byte] => String = (encodedPath) => new String(Base64.getDecoder.decode(encodedPath), StandardCharsets.UTF_8)

  val returnFileOrDir: Array[Byte] => String = (encodedPath) => if(decoder(encodedPath).contains(".")) "File" else "Directory"

  def getGithubUser(urlOverride: Option[String] = None, userName: String)(implicit ec: ExecutionContext):EitherT[Future, APIError, UserModel] =
    connector.get[UserModel](urlOverride.getOrElse(s"https://api.github.com/users/$userName"))

  def getGithubUserRepositories(urlOverride: Option[String] = None, userName: String)(implicit ec: ExecutionContext):EitherT[Future, APIError, List[RepositoriesModel]] =
    connector.getList[RepositoriesModel](urlOverride.getOrElse(s"https://api.github.com/users/$userName/repos"))

  def getGithubRepository(urlOverride: Option[String] = None, userName: String, repoName:String)(implicit ec: ExecutionContext):EitherT[Future, APIError, List[RepositoryModel]] =
    connector.getList[RepositoryModel](urlOverride.getOrElse(s"https://api.github.com/repos/$userName/$repoName/contents/"))

  def getGithubRepositoryDir(urlOverride: Option[String] = None,userName: String, repoName:String, path: Array[Byte])(implicit ec: ExecutionContext):EitherT[Future, APIError, List[RepositoryModel]] =
    connector.getList[RepositoryModel](urlOverride.getOrElse(s"https://api.github.com/repos/$userName/$repoName/contents/${decoder(path)}"))

  def getGithubRepositoryFile(urlOverride: Option[String] = None,userName: String, repoName:String, path: Array[Byte])(implicit ec: ExecutionContext):EitherT[Future, APIError, FileModel] =
    connector.get[FileModel](urlOverride.getOrElse(s"https://api.github.com/repos/$userName/$repoName/contents/${decoder(path)}"))



  //  def getGithubRepositoryFileOrDir(urlOverride: Option[String] = None,userName: String, repoName:String, path: String)(implicit ec: ExecutionContext): EitherT[Future, APIError, _ >: List[RepositoryModel] with FileModel <: Equals] ={
  //    if (path.contains(".")) {
  //      connector.getList[RepositoryModel](urlOverride.getOrElse(s"https://api.github.com/repos/$userName/$repoName/$path"))
  //    } else {
  //      connector.get[FileModel](urlOverride.getOrElse(s"https://api.github.com/repos/$userName/$repoName/$path"))
  //    }
  //  }
}
