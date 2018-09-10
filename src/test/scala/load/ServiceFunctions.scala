package load

import io.gatling.core
import io.gatling.core.Predef.{Simulation, exec, foreach, _}
import io.gatling.core.session
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef.{css, http, jsonPath, jsonpJsonPath, regex}


/**
  * Class with the same functions for all usertypes
  */

class ServiceFunctions {

  val headers = new Headers

  /*List with first pages for each user group */

  val first_pages = Array("/link-action", "/link-action-1", "/link-action-2", "/link-action-3")

  // Login Function

  def login(feeder: FeederBuilder[_]): ChainBuilder = {
    exec(http("homepage")
      .get("/")
      .headers(headers.headers_0))
      .exec(http("/entry")
        .post("/entry")
        .headers(headers.headers_84)
        .check(regex("(?i)value=[\\S]+(form-[_-a-zA-Z0-9]+)[\\S]+").saveAs("auth_build")))
      .feed(feeder)
      .exec(http("/system/ajax")
        .post("/system/ajax")
        .headers(headers.headers_84)
        .formParam("name", "${username}")
        .formParam("pass", "1111")
        .formParam("form_build_id", "${auth_build}")
        .formParam("form_id", "user_login_block")
        .formParam("_triggering_element_name", "op")
        .formParam("_triggering_element_value", "Login"))
  }

  //Get all links from menu

  def getMenu(link_num: Int, menu_access: Boolean): ChainBuilder = {
    val baseRequest = http("/user/${id}" + first_pages(link_num))
      .get("/user/${id}" + first_pages(link_num))
      .headers(headers.headers_0)
      .check(css("#links-1 ul a", "href").findAll.saveAs("links"))
      .check(css("td.box-znum a", "href").findAll.saveAs("applList"))
      //
      .check(css("#kab-links-form input[name='form_build_id']", "value").saveAs("build_id_kablinks"))
      .check(css("#kab-links-form input[name='form_token']", "value").saveAs("form_token_kablinks"))
      .check(css("#kab-links-form input[name='form_id']", "value").saveAs("form_id_kablinks"))
      .check(css("#edit-next-page", "value").saveAs("triggering_element_value_kablinks"))
      .check(css("#edit-next-page", "name").saveAs("triggering_element_name_kablinks"))

    if (menu_access) {
      exec(baseRequest
        .check(css("#kab-links-2 ul a", "href").findAll.saveAs("links2"))
        .check(css("td.box-sotrfio").findAll.saveAs("sotr_fio")))

    } else {
      exec(baseRequest)
    }
  }

  // GET query for all links

  def checkKablinks: ChainBuilder = foreach("${links}", "link") {
    exec(http("${link}*Link")
      .get("${link}")
      .headers(headers.headers_0))
      .doIf(session => session.contains("links2")) {
        foreach("${links2}", "link2") {
          exec(http("${link2}*Link22")
            .get("${link2}")
            .headers(headers.headers_0))
        }
      }
  }

  // Open application for work

  val znum = new java.util.concurrent.atomic.AtomicInteger(0)

  def getApplication: ChainBuilder =  {
    doIfEqualsOrElse("-", "${sotr_fio(" + znum + ")}") {
      exec(session => session.set("znum", znum))
      exec(http("${applList(" + znum + ")}")
        .get("${applList(" + znum + ")}")
        .check(css("#block > div > ul > li > ul > li.last.leaf a", "href").saveAs("offer"))
        .check(css("#form input[name='form_build_id']", "value").saveAs("build_id"))
        .check(css("#form input[name='form_token']", "value").saveAs("form_token"))
        .check(css("#form input[name='form_id']", "value").saveAs("form_id"))
        .check(css("#edit-actions input[type='submit']:first-child", "name").saveAs("triggering_element_name"))
        .check(css("#edit-actions input[type='submit']:first-child", "value").saveAs("triggering_element_value")))
    }
    {exec(session => session.set("znum", znum.getAndIncrement()))
      exec(http("${applList(" + znum + ")}*User opens*")
        .get("${applList(" + znum + ")}")
        .check(css("#block > div > ul > li > ul > li.last.leaf a", "href").saveAs("offer"))
        .check(css("#form input[name='form_build_id']", "value").saveAs("build_id"))
        .check(css("#form input[name='form_token']", "value").saveAs("form_token"))
        .check(css("#form input[name='form_id']", "value").saveAs("form_id"))
        .check(css("#edit-actions input[type='submit']:first-child", "name").saveAs("triggering_element_name"))
        .check(css("#edit-actions input[type='submit']:first-child", "value").saveAs("triggering_element_value")))
    }
  }

  // Open offers and get some params rom it

  def getOffer: ChainBuilder = {
    exec(http("${offer}")
      .get("${offer}")
      //get form tokens for next queries
      .check(css("#form input[name='form_build_id']", "value").saveAs("build_id"))
      .check(css("#form input[name='form_token']", "value").saveAs("form_token"))
      .check(css("#form input[name='form_id']", "value").saveAs("form_id"))
      .check(css("#edit-actions input[type='submit']:first-child", "name").saveAs("triggering_element_name"))
      .check(css("#edit-actions input[type='submit']:first-child", "value").saveAs("triggering_element_value"))
      .check(css("#base-form input[name='form_build_id']", "value").saveAs("build_id_sign"))
      .check(css("#base-form input[name='form_token']", "value").saveAs("form_token_sign")))
  }

  // Simple ajax for simple cases

  def ajax: ChainBuilder = {
    exec(http("/system/ajax/")
      .post("/system/ajax")
      .formParam("form_build_id", "${build_id}")
      .formParam("form_token", "${form_token}")
      .formParam("form_id", "${form_id}")
      .formParam("_triggering_element_name", "${triggering_element_name}")
      .formParam("_triggering_element_value", "${triggering_element_value}"))
  }

  def ajaxKablinks: ChainBuilder = {
    exec(http("/system/ajax*Links*")
      .post("/system/ajax")
      .headers(headers.headers_84)
      .formParam("date1", "")
      .formParam("date2", "")
      .formParam("form_build_id", "${build_id_kablinks}")
      .formParam("form_token", "${form_token_kablinks}")
      .formParam("form_id", "form_id_kablinks")
      .formParam("_triggering_element_name", "${triggering_element_name_kablinks}")
      .formParam("_triggering_element_value", "${triggering_element_value_kablinks}"))
  }

  def logOut: ChainBuilder = {
    exec(http("/user/logout")
      .get("/user/logout")
      .headers(headers.headers_0))
  }

}