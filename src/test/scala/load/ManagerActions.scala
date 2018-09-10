package load

import scala.concurrent.duration._
import io.gatling.core.Predef.{Simulation, exec, _}
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

/**
  * Class for actions of active users
  */
class ManagerActions {

  val headers = new Headers

  val request_manager = Array("/user/enter", "/offer",
    "/link", "/request", "/paid",
    "/downloadScans", "/sendForSignature")

  // Get all applications in menu
  object ChangeLink {

    val changeLink = exec(http("${links(3)}*Enter*")
      .get("${links(3)}")
      .check(css("td.box-znum a", "href").findAll.saveAs("applList")))
  }

  object OfferComplain {

    val offerComplain = exec(http("/system/ajax/")
      .post("/system/ajax")
      .formParam("cmc_kom", "")
      .formParam("form_build_id", "${build_id}")
      .formParam("form_token", "${form_token}")
      .formParam("form_id", "${form_id}")
      .formParam("dateFrom", "")
      .formParam("dateTo", "")
      .formParam("_triggering_element_name", "op")
      .formParam("_triggering_element_value", "Save and send"))
  }


  val brToPf = exec(http("/system/ajax/")
    .post("/system/ajax")
    .formParam("cmc_komissia", "33300")
    .formParam("form_build_id", "${build_id}")
    .formParam("form_token", "${form_token}")
    .formParam("form_id", "${form_id}")
    .formParam("_triggering_element_name", "${triggering_element_name}")
    .formParam("_triggering_element_value", "${triggering_element_value}")
    //For signing
    .check(regex("#token\":\"([\\S][^\"]+)").saveAs("r_token"))
    .check(jsonPath("$..actions.0.act").findAll.saveAs("act"))
    .check(jsonPath("$..actions.0.z.id").findAll.saveAs("id"))
    .check(jsonPath("$..actions.0.z.client").findAll.saveAs("client"))
    .check(jsonPath("$..actions.0.z.type").findAll.saveAs("type"))
    .check(jsonPath("$..actions.0.z.created").findAll.saveAs("created"))
    .check(jsonPath("$..actions.0.z.status").saveAs("status"))




}