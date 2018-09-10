package scenarios

import io.gatling.core.Predef.{csv, rampUsers, scenario}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import load.{ManagerActions, FileLoader, ServiceFunctions}

import scala.concurrent.duration._

/**
* Simulation file uploading files
*/
class FileLoaderSimulation extends Simulation {

  val servfunc = new ServiceFunctions
  val fileload = new FileLoader

  var httpConf = http
    .baseURL("https://testurl")
    .acceptHeader("*/*")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3")
    .userAgentHeader("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:60.0) Gecko/20100101 Firefox/60.0")

  val active = scenario("LoadActive").exec(servfunc.login(csv("user_active.csv").circular),
    servfunc.getMenu(0, menu_access = true), servfunc.getApplication, servfunc.getOffer,
    fileload.ajaxUpload, fileload.multiformActions, servfunc.logOut)

  val active2 = scenario("LoadActive2").exec(servfunc.login(csv("user_active.csv").circular),
  servfunc.getMenu(0, menu_access = true), servfunc.getApplication, servfunc.getOffer)

  setUp(
    active.inject(rampUsers(10) over (10 seconds))
  ).protocols(httpConf)

}
