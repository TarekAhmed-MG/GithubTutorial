package config

import baseSpec.BaseSpecWithApplication

class EnvironmentVariablesSpec extends BaseSpecWithApplication{

  "find a user in the database by username" in {
    assert(EnvironmentVariables.authToken == sys.env.getOrElse("AuthPassword","Failed"))
  }
}
