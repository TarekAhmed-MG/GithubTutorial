package controllers

import models.{APIError, FileCreateModel, FileCreateModelDTO, FileUpdateModelDTO, UserModel}
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import services.{GithubUserService, RepositoryService}

import java.nio.charset.StandardCharsets
import java.util.Base64
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ApplicationController @Inject()(
                                       val controllerComponents: ControllerComponents,
                                       val githubUserService: GithubUserService,
                                       val repositoryService: RepositoryService
                                     )(implicit val ec: ExecutionContext)
  extends BaseController{

  // def index () = ???

  def getGithubUser(userName: String): Action[AnyContent] = Action.async { implicit request =>
    githubUserService.getGithubUser(userName = userName).value.map {
      //case Right(user) =>  Ok {Json.toJson(user)} // over here do a views.html.PageName(user)
      case Right(user) =>  Ok {views.html.github(user)}
      case Left(_) => Status(404)(Json.toJson("Unable to find any users"))
    }
  }

  def getGithubUserRepositories(userName: String): Action[AnyContent] = Action.async { implicit request =>
    githubUserService.getGithubUserRepositories(userName = userName).value.map {
      case Right(repositories) =>  Ok {views.html.repositories(repositories, userName)}
      case Left(_) => Status(404)(Json.toJson("Unable to find any repositories"))
    }
  }

  def getGithubRepository(userName: String, repoName: String): Action[AnyContent] = Action.async { implicit request =>
    githubUserService.getGithubRepository(userName = userName,repoName = repoName).value.map {
      case Right(repo) =>  Ok {views.html.repository(repo,userName,repoName)}
      case Left(_) => Status(404)(Json.toJson("Unable to find repository"))
    }
  }

  def getGithubRepositoryFileOrDir(userName:String,repoName:String,path: String): Action[AnyContent] = Action.async { implicit request =>
     val encodedPath = Base64.getEncoder.encode(path.getBytes(StandardCharsets.UTF_8))

    githubUserService.returnFileOrDir(encodedPath) match {
      case "File" => githubUserService.getGithubRepositoryFile(userName= userName, repoName= repoName, path= encodedPath).value.map {
        case Right(file) => Ok {views.html.file(file)}
        case Left(_) => Status(404)(Json.toJson("Unable to find any files"))
      }
      case "Directory" => githubUserService.getGithubRepositoryDir(userName= userName, repoName= repoName, path= encodedPath).value.map {
        case Right(directory) => Ok {views.html.repository(directory,userName, repoName)}
        case Left(_) => Status(404)(Json.toJson("Unable to find any directories"))
      }
      case _ => Future(Status(404)(Json.toJson("Unable to find any directories or Files")))
    }
  }

  def createGithubRepositoryFile(userName:String,repoName:String,path: String): Action[JsValue] = Action.async(parse.json) { implicit request =>
    val encodedPath = Base64.getEncoder.encode(path.getBytes(StandardCharsets.UTF_8))
    request.body.validate[FileCreateModelDTO] match {
      case JsSuccess(fileCreateModelDTO , _) =>
        githubUserService.createGithubRepositoryFile(userName= userName, repoName= repoName, path= encodedPath, requestBody= fileCreateModelDTO).value.map {
          case Right(file) => Ok {views.html.file(file)}
          case Left(apiError) => Status(apiError.httpResponseStatus)(apiError.reason)
        }
      case JsError(_) => Future(BadRequest)
    }
  }

  //update repo/file

  def updateGithubRepositoryFile(userName:String,repoName:String,path: String): Action[JsValue] = Action.async(parse.json) { implicit request =>
    val encodedPath = Base64.getEncoder.encode(path.getBytes(StandardCharsets.UTF_8))
    request.body.validate[FileUpdateModelDTO] match {
      case JsSuccess(fileUpdateModelDTO , _) =>
        githubUserService.updateGithubRepositoryFile(userName= userName, repoName= repoName, path= encodedPath, requestBody= fileUpdateModelDTO).value.map {
          case Right(file) => Ok {views.html.file(file)}
          case Left(apiError) => Status(apiError.httpResponseStatus)(apiError.reason)
        }
      case JsError(_) => Future(BadRequest)
    }
  }

  // delete repo/file



  def storeGithubUser(userName: String): Action[AnyContent] = Action.async { implicit request =>
    githubUserService.getGithubUser(userName = userName).value.flatMap {
      case Right(user) => repositoryService.create(user).map {
                case Right(_) => Status(CREATED)
                case Left(apiError) => Status(apiError.upstreamStatus)(apiError.upstreamMessage)
              }
      case Left(_) => Future.successful(Status(404)(Json.toJson("Unable to find any users")))
    }
  }

  def create(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[UserModel] match {
      case JsSuccess(userModel, _) =>
        repositoryService.create(userModel:UserModel).map {
          case Right(_) => Status(CREATED)
          case Left(apiError) => Status(apiError.upstreamStatus)(apiError.upstreamMessage)
        }
      case JsError(_) => Future(BadRequest)
    }
  }

  def read(idOrName:String): Action[AnyContent] = Action.async{implicit request =>
    repositoryService.read(idOrName).map{
      case Right(Some(item: UserModel)) => Ok{Json.toJson(item)}
      case Left(apiError) => Status(apiError.upstreamStatus)(Json.toJson(apiError.upstreamMessage))
    }
  }

  def update(id:String, fieldName:String): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[UserModel] match {
      case JsSuccess(dataModel, _) =>
        repositoryService.update(id,fieldName,dataModel).map{
          case Right(_) => Status(ACCEPTED)
          case Left(apiError) => Status(apiError.upstreamStatus)(Json.toJson(apiError.upstreamMessage))
        }
      case JsError(_) => Future(BadRequest)
    }
  }

  def delete(id:String): Action[AnyContent] = Action.async{ implicit request =>
    repositoryService.delete(id).map{
      case Right(_) => Status(ACCEPTED)
      case Left(apiError) => Status(apiError.upstreamStatus)(Json.toJson(apiError.upstreamMessage))
    }
  }
}
