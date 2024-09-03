package config

object EnvironmentVariables {
  val authToken: String = sys.env.getOrElse("AuthPassword","AuthPassword environment variable not set")
}
