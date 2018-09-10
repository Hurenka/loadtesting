package scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import load.ServiceFunctions

import scala.concurrent.duration._

class KablinksSimulation extends Simulation {

  val servfunc = new ServiceFunctions

  var httpConf = http
    .baseURL("https://testurl")
    .acceptHeader("*/*")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3")
    .userAgentHeader("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:60.0) Gecko/20100101 Firefox/60.0")

  val commerc = scenario("Commerc").exec(servfunc.login(csv("user_commerc.csv").circular),
    servfunc.getMenu(2, menu_access = true), servfunc.checkKablinks)

  val client = scenario("Clients").exec(servfunc.login(csv("user_client.csv").circular),
    servfunc.getMenu(3, menu_access = false), servfunc.checkKablinks)

  val support = scenario("Support").exec(servfunc.login(csv("user_support.csv").circular),
    servfunc.getMenu(1, menu_access = false), servfunc.checkKablinks)

  val active = scenario("Active").exec(servfunc.login(csv("user_active.csv").circular),
    servfunc.getMenu(0, menu_access = true), servfunc.checkKablinks)

  setUp(
    commerc.inject(rampUsers(1) over (10 seconds)),
    client.inject(rampUsers(20) over (10 seconds)),
    support.inject(rampUsers(100) over (10 seconds)),
    active.inject(rampUsers(50) over (10 seconds))
  ).protocols(httpConf)
}
