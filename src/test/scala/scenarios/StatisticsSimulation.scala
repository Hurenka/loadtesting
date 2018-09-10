package scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import load.{ServiceFunctions, UserActions}

import scala.concurrent.duration._

/**
* Simulation only for module Statistics
*/

class StatisticsSimulation extends Simulation {

  val servfunc = new ServiceFunctions
  val useract = new UserActions

  var httpConf = http
    .baseURL("https://testurl")
    .acceptHeader("*/*")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3")
    .userAgentHeader("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:60.0) Gecko/20100101 Firefox/60.0")

  val statistics_scn = scenario("StatisticsSimulation").exec(servfunc.login(csv("user_commerc.csv").circular),
    servfunc.getMenu(2, menu_access = true), useract.loadStatistics("ext"))

  setUp(
    statistics_scn.inject(rampUsers(10) over (10 seconds))
  ).protocols(httpConf)

}
