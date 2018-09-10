package scenarios

import io.gatling.core.Predef.{csv, rampUsers, scenario}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import load.ServiceFunctions

import scala.concurrent.duration._

class LoginSimulation extends Simulation{

  val servfunc = new ServiceFunctions

  var httpConf = http
    .baseURL("https://testurl")
    .acceptHeader("*/*")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3")
    .userAgentHeader("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:60.0) Gecko/20100101 Firefox/60.0")

  val login_scn = scenario("LoginSimulation").exec(servfunc.login(csv("user_support.csv").circular),
    servfunc.login(csv("user_active.csv").circular), servfunc.login(csv("user_client.csv").circular),
    servfunc.login(csv("user_commerc.csv").circular))


    setUp(
      login_scn.inject(rampUsers(10) over (10 seconds))
    ).protocols(httpConf)


}
