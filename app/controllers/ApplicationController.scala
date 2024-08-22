package controllers

import models.UserModel
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import services.{GithubUserService, RepositoryService}

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
      case Right(user) =>  Ok {Json.toJson(user)}
      case Left(_) => Status(404)(Json.toJson("Unable to find any books"))
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

  def delete(id:String): Action[AnyContent] = Action.async{implicit request =>
    repositoryService.delete(id).map{
      case Right(_) => Status(ACCEPTED)
      case Left(apiError) => Status(apiError.upstreamStatus)(Json.toJson(apiError.upstreamMessage))
    }
  }


}
