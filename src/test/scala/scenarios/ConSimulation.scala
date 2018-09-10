package scenarios

import io.gatling.core.Predef.{csv, rampUsers, scenario}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import load.{FileLoader, ServiceFunctions, UserActions}

import scala.concurrent.duration._

/**
* Simulation only for module Con
*/
class ConveyorSimulation extends Simulation {

  val servfunc = new ServiceFunctions
  val useract = new UserActions

  var httpConf = http
    .baseURL("https://testurl")
    .acceptHeader("*/*")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3")
    .userAgentHeader("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:60.0) Gecko/20100101 Firefox/60.0")


  val con = scenario("Con").exec(servfunc.login(csv("user_active.csv").circular),
    servfunc.getMenu(0, menu_access = true), useract.conEditPage, useract.ajaxCheckbox, useract.conForm,
    useract.conMainPage, useract.ajaxPrint, useract.conUploadPage, useract.ajaxCheckbox,
    useract.uploadScansForm, useract.uploadScans, useract.conUploadPage, useract.ajaxCheckbox, useract.nextStage,
    useract.convSignPage, useract.signAll)

  setUp(
    conveyor.inject(constantUsersPerSec(1) during (5 minutes))
  ).protocols(httpConf)

}
