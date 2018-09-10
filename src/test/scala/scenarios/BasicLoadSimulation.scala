package scenarios

import io.gatling.core.Predef.{csv, rampUsers, scenario}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import load.{ManagerActions, FileLoader, ServiceFunctions, UserActions}

import scala.concurrent.duration._

/**
* Simulation for light load and parallel GUI autotests
*/
class BasicLoadSimulation extends Simulation{

  val servfunc = new ServiceFunctions

  var httpConf = http
    .baseURL("https://testurl")
    .acceptHeader("*/*")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3")
    .userAgentHeader("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:60.0) Gecko/20100101 Firefox/60.0")

  val scn = scenario("HomePageSimulation").exec(http("https://testurl").get("https://testurl"))

  val login_scn = scenario("LoginSimulation").exec(servfunc.login(csv("user_support.csv").circular),
    servfunc.login(csv("user_active.csv").circular), servfunc.login(csv("user_client.csv").circular),
    servfunc.login(csv("user_commerc.csv").circular))

  val commerc = scenario("Commerc").exec(servfunc.login(csv("user_commerc.csv").circular),
    servfunc.getMenu(2, menu_access = true), servfunc.checkKablinks)

  val client = scenario("Clients").exec(servfunc.login(csv("user_client.csv").circular),
    servfunc.getMenu(3, menu_access = false), servfunc.checkKablinks)

  val support = scenario("Support").exec(servfunc.login(csv("user_support.csv").circular),
    servfunc.getMenu(1, menu_access = false), servfunc.checkKablinks)

  val active = scenario("Active").exec(servfunc.login(csv("user_active.csv").circular),
    servfunc.getMenu(0, menu_access = true), servfunc.checkKablinks)

  setUp(
    scn.inject(constantUsersPerSec(0.3) during (30 minutes)),
    login_scn.inject(constantUsersPerSec(0.01) during (30 minutes)),
    commerc.inject(constantUsersPerSec(0.001) during (30 minutes)),
    client.inject(constantUsersPerSec(0.05) during (30 minutes)),
    support.inject(constantUsersPerSec(0.05) during (30 minutes)),
    active.inject(constantUsersPerSec(0.05) during (30 minutes)),
  ).protocols(httpConf)

}
