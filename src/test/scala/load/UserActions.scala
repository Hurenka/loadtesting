package load

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class UserActions {

  val headers = new Headers

  val csToPs = exec(http("${applList(0)}")
    .get("${applList(0)}")
    .check(css("#control-form input[name='form_build_id']", "value").saveAs("build_sign"))
    .check(css("#control-form input[name='form_token']", "value").saveAs("token_sign")))
    .exec(http("/system/ajax/*Sign*")
      .post("/system/ajax")
      .formParam("form_build_id", "${build_sign}")
      .formParam("form_token", "${token_sign}")
      .formParam("form_id", "control_form")
      .formParam("_triggering_element_name", "controllbutton-appl-z-sign-headbutts")
      .check(jsonPath("$..fileslist..fil").findAll.saveAs("list_files"))
      .check(jsonPath("$..fileslist..id").findAll.saveAs("ids"))
      .check(jsonPath("$..fileslist..fid").findAll.saveAs("fids"))
      .check(jsonPath("$..fileslist..name").findAll.saveAs("names"))
      .check(jsonPath("$..fileslist..size").findAll.saveAs("sizes"))
      .check(jsonPath("$..fileslist..mime").findAll.saveAs("mimes"))
      .check(jsonPath("$..fileslist..hash").findAll.saveAs("hashes"))
      .check(jsonPath("$..settings.token").saveAs("special_token"))
    )
    .foreach("${list_files}", "file", "n") {
      exec(http("${file}")
        .get("${file}"))
      exec(http("/files-signing")
        .post("/files-signing")
        .formParam("token", "${special_token}")
        .formParam("signeddata[module]", "appl")
        .formParam("signeddata[type]", "z")
        .formParam("signeddata[id]", "${ids(n)}")
        .formParam("signeddata[fil]", "${file}")
        .formParam("signeddata[fid]", "${fids(n)}")
        .formParam("signeddata[mime]", "${mimes(n)}")
        .formParam("signeddata[size]", "${sizes(n)}")
        .formParam("signeddata[name]", "${names(n)}")
        .formParam("signeddata[hash]", "${hashes(n)}")
        .formParam("signeddata[filenum]", "${n}")
    }


  // To pass type of statistics as argument

  def loadStatistics(mode: String) = exec(http("/user/${id}/statistics")
    .get("/user/${id}/statistics")
    .headers(headers.headers_0)
    .check(css("#statistics-export-form input[name='form_build_id']", "value").saveAs("build_id"))
    .check(css("#statistics-export-form input[name='form_token']", "value").saveAs("from_token")))
    .exec(http("/user/${id}/statistics/simple?")
      .get("/user/${id}/statistics/simple?")
      .headers(headers.headers_0))
    .exec(http("/user/${id}/statistics/export")
      .get("/user/${id}/statistics/export")
      //choose type of document - ext, simple, score
      .queryParam("mode", mode)
      .queryParam("form_build_id", "${build_id}")
      .queryParam("form_token", "${form_token}")
      .queryParam("form_id", "statistics_export_form")
      .headers(headers.headers_0)
      .check(jsonPath("$.data").saveAs("hash")))
    .exec(http("/user/${id}/statistics/download/id=${hash}")
      .get("/user/${id}/statistics/download/id=${hash}")
      .headers(headers.headers_0))

  def conEditPage: ChainBuilder = exec(http("/editMakets")
      .get("/editMakets")
      .check(css("td.link a").findAll.saveAs("list"))
        .check(css("#edit-execbutton input[type='submit']", "value").findAll.saveAs("triggering_element_values"))
        .check(css("#editmaket-form input[name='form_build_id']", "value").saveAs("build_id"))
        .check(css("#editmaket-form input[name='form_token']", "value").saveAs("form_token"))
        .check(css("#editmaket-form input[name='form_id']", "value").saveAs("form_id"))
      )

  def conMainPage: ChainBuilder = exec(http("/main")
    .get("/main")
     .check(css("#edit-editmaket a").findAll.saveAs("list"))
        .check(css("#main-form input[name='form_build_id']", "value").saveAs("build_id"))
        .check(css("#main-form input[name='form_token']", "value").saveAs("form_token"))
        .check(css("#main-form input[name='form_id']", "value").saveAs("form_id"))
        .check(css("#edit-printdocs", "value").findAll.saveAs("triggering_element_values"))
        .check(css("#edit-printdocs", "name").findAll.saveAs("triggering_element_names")))

  def conUploadPage: ChainBuilder = exec(http("/downloadScans")
    .get("/downloadScans")
      .check(css("#edit-uploadmaket a").findAll.saveAs("list"))
        .check(css("#edit-execbutton input[type='submit']", "value").findAll.saveAs("triggering_element_values"))
        .check(css("#edit-execbutton input[type='submit']", "name").findAll.saveAs("triggering_element_names"))
        .check(css("#downloadscans-form input[name='form_build_id']", "value").saveAs("build_id"))
        .check(css("#downloadscans-form input[name='form_token']", "value").saveAs("form_token"))
        .check(css("#downloadscans-form input[name='form_id']", "value").saveAs("form_id")))

  def conSignPage: ChainBuilder = exec(http("/sendForSignature")
    .get("/sendForSignature")
    .check(css("#edit-editmaket a").findAll.saveAs("list"))
        // Сохраняем токены формыодни на все кнопки
        .check(css("#sendforsignature-form input[name='form_build_id']", "value").saveAs("build_id"))
        .check(css("#sendforsignature-form input[name='form_token']", "value").saveAs("form_token"))
        .check(css("#sendforsignature-form input[name='form_id']", "value").saveAs("form_id")))


  // List of button names
  val popupNames = Array("action1", "action2", "action3")


  // Added a checkbox click
  def ajaxCheckbox: ChainBuilder = {
    exec(http("/system/ajax")
      .post("/system/ajax")
      .formParam("list[${list(0)}]", "${list(0)}")
      .formParam("form_build_id", "${build_id}")
      .formParam("form_token", "${form_token}")
      .formParam("form_id", "${form_id}")
      .formParam("_triggering_element_name", "${list(0)}"))
  }

  def ajaxPrint: ChainBuilder = {
    exec(http("/system/ajax*Print*")
      .post("/system/ajax")
      .formParam("list[${list(0)}]", "${list(0)}")
      .formParam("form_build_id", "${build_id}")
      .formParam("form_token", "${form_token}")
      .formParam("form_id", "main_form")
  }

  val formparam_num = new java.util.concurrent.atomic.AtomicInteger(0)

  val params = Array(
    Map("dateFrom" -> "08.08.2018"),
    Map("dateTo" -> "08.08.2018"),
    Map("FIO" -> "Петрова Екатерина Михайловна"),
    Map("regionArea" -> "Москва", "locality" -> "Москва", "street" -> "Голубинская", "house" -> "19", "flat" -> "37", "postalCode" -> ""),
    Map("regionArea" -> "Москва", "locality" -> "Москва", "street" -> "Голубинская", "house" -> "19", "flat" -> "37", "postalCode" -> "")
  )

  // Chose a button to work with
  def conForm: ChainBuilder = foreach("${triggering_element_values}", "elvalue", "i") {
    exec(http("/system/ajax")
      .post("/system/ajax")
      .formParam("list[${list(0)}]", "${list(0)}")
      .formParam("form_build_id", "${build_id}")
      .formParam("form_token", "${form_token}")
      .formParam("form_id", "${form_id}")
      .formParam("_triggering_element_name", "${triggering_element_names(i)}")
      .formParam("_triggering_element_value", "${elvalue}")
      // Take all tokens in list
      .check(regex("(?i)value=\\\\u0022([_-a-zA-Z0-9]+)[\\S]+").findAll.saveAs("popup_tokens"))
      .check(css("#edit-execbutton input[type='form_build_id']", "value").saveAs("build_id_popup"))
      .check(css("#edit-execbutton input[type='submit']", "name").saveAs("triggering_element_name_popup")))
      .doIfOrElse(session => session("elvalue").as[String].contentEquals("Checked")) {
        exec(session =>
          session)
      } {
        exec(session => session.set("formparam_num", formparam_num.getAndIncrement()))
          .exec(http("/system/ajax*")
          .post("/system/ajax")
          .formParamMap(params(formparam_num.intValue()))
          .formParam("form_build_id", "${popup_tokens(0)}")
          .formParam("form_token", "${popup_tokens(1)}")
          .formParam("form_id", "${popup_tokens(2)}")
          .formParam("_triggering_element_name", "${triggering_element_names(0)}")
          .formParam("_triggering_element_value", "popupNames(i)"))
      }
  }

  def uploadScansForm: ChainBuilder = {
    exec(http("/system/ajax/")
    .post("/system/ajax")
    .formParam("list[${list(0)}]", "${list(0)}")
    .formParam("form_build_id", "${build_id}")
    .formParam("form_token", "${form_token}")
    .formParam("form_id", "${form_id}")
    .formParam("_triggering_element_name", "${triggering_element_names(0)}")
    .formParam("_triggering_element_value", "${triggering_element_values(0)}")
    .headers(headers.headers_2)
      .check(regex("(?i)value=\\\\u0022([_-a-zA-Z0-9]+)[\\S]+").findAll.saveAs("popup_tokens"))
      .check(css("#edit-execbutton input[type = 'form_build_id']", "value").saveAs("build_id_popup"))
      .check(css("#edit-execbutton input[type = 'submit']", "name").saveAs("triggering_element_name_popup")))
  }

  def uploadScans: ChainBuilder = {
    exec(http("/system/ajax/")
      .post("/system/ajax")
      .formUpload("name[]", "${list(0)}.pdf")
      .formParam("form_build_id", "${popup_tokens(0)}")
      .formParam("form_token", "${popup_tokens(1)}")
      .formParam("form_id", "${popup_tokens(2)}")
      .headers(headers.headers_2))
  }

  def nextStage : ChainBuilder = {
    exec(http("/system/ajax")
      .post("/system/ajax")
      .formParam("list[${list(0)}]", "${list(0)}")
      .formParam("form_build_id", "${build_id}")
      .formParam("form_token", "${form_token}")
      .formParam("form_id", "downloadScans_form")

  }

}
