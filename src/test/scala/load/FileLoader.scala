package load

import scala.concurrent.duration._
import io.gatling.core.Predef.{Simulation, exec, _}
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

/** Class with functions, which upload or download files
  */
class FileLoader {

  val headers = new Headers

  //метод-загрузчик

  def ajaxUpload(): ChainBuilder = exec(http("/system/ajax/*Upload*")
    .post("/system/ajax")
    .formUpload("files[mfile]", "testir.pdf")
    .formUpload("files[sfile]", "testir.pdf")
    .formParam("form_build_id", "${build_id_sign}")
    .formParam("form_token", "${form_token_sign}"))
    .formParam("form_id", "base_form")
    .headers(headers.headers_2))

  def ajaxDownload(): ChainBuilder = exec(http("/system/ajax/*Download*")
    .post("/system/ajax")
    .formParam("form_build_id", "${build_id_sign}")
    .formParam("form_token", "${form_token_sign}")
    .formParam("files[mfile]", "")
    .formParam("files[sfile]", "")
    .formParam("form_id", "base_form")
    .headers(headers.headers_2))

  def multiformActions: ChainBuilder = exec(http("/multiform-actions-todo/${r_token}")
    .post("/multiform-actions-todo/${r_token}")
    .formParam("actions[0][act]", "${act}")
    .formParam("actions[z][id]", "${id(0)}")
    .formParam("actions[0][z][created]", "${created(0)}")
    .formParam("actions[1][act]", "setnextstate")
    .formParam("actions[1][st]", "p")
    .formParam("actions[1][z][id]", "${id(0)}")
    .formParam("actions[1][z][created]", "${created(0)}")
      .formParam("actions[#token]", "${r_token}"))

}
