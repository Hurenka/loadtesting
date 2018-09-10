package scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import load.{ServiceFunctions, UserActions}

import scala.concurrent.duration._

/**
* Simulation only for module Sign
*/
class SigningSimulation extends Simulation{

  val servfunc = new ServiceFunctions
  val useract = new UserActions

  var httpConf = http
    .baseURL("https://testurl")
    .acceptHeader("*/*")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3")
    .userAgentHeader("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:60.0) Gecko/20100101 Firefox/60.0")

  val signing_scn = scenario("SigningSimulation").exec(servfunc.login(csv("user_client.csv").circular),
    servfunc.getMenu(3, menu_access = false), useract.csToPs)

  setUp(
    signing_scn.inject(constantUsersPerSecond(10) during (70 seconds))
  ).protocols(httpConf)

}
