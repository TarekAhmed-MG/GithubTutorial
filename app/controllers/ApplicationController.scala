package controllers

import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import services.GithubUserService

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class ApplicationController @Inject()(val controllerComponents: ControllerComponents, val githubUserService: GithubUserService)(implicit val ec: ExecutionContext) extends BaseController{

  def getGithubUser(userName: String): Action[AnyContent] = Action.async { implicit request =>
    githubUserService.getGithubUser(userName = userName).value.map {
      case Right(user) =>  Ok {Json.toJson(user)}
      case Left(_) => Status(404)(Json.toJson("Unable to find any books"))
    }
  }

}
