package scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import load.{Application, ServiceFunctions, UserActions}

import scala.concurrent.duration._

/**
* Simulation only for module creation of the Application
*/
class ApplicationSimulation extends Simulation {
  val servfunc = new ServiceFunctions
  val useract = new UserActions
  val app = new Application

  var httpConf = http
    .baseURL("https://testurl")
    .acceptHeader("*/*")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3")
    .userAgentHeader("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:60.0) Gecko/20100101 Firefox/60.0")

  val application_scn = scenario("ApplicationSimulation").exec(servfunc.login(csv("user_support.csv").circular),
    servfunc.getMenu(1, menu_access = false), servfunc.ajaxKablinks, servfunc.ajax,
    app.createNewApplication(csv("user_client.csv").circular),
    app.params(csv("user_support.csv").circular, "https://testurl"), app.finance(csv("user_support.csv").circular))

  setUp(
    application_scn.inject(rampUsers(10) over (10 seconds))
  ).protocols(httpConf)
}
