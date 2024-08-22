package models

import play.api.libs.json.{Json, OFormat}

//username, date account created, location, number of followers, number following
case class UserModel(
                      _login:String,
                      created_at:String,
                      location:String,
                      followers:String,
                      following:String
                    )

object UserModel{
  implicit val formats: OFormat[UserModel] = Json.format[UserModel]
}
