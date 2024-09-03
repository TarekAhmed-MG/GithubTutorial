package config

import baseSpec.BaseSpecWithApplication

class EnvironmentVariablesSpec extends BaseSpecWithApplication{

  "check if authToken can be retrieved" in {
    assert(EnvironmentVariables.authToken == sys.env.getOrElse("AuthPassword","Failed"))
  }

}
