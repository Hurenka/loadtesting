package load

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import scala.util.Random

/** @class with creation of different applications
	*
	*/
class Application {

	val headers = new Headers

	private val lotnums = Iterator.continually(Map("lotnum" -> (Random.alphanumeric.take(16).mkString)))

	def createNewApplication(feeder: FeederBuilder[_]): ChainBuilder = {
		exec(http("/bg/newapplication*GET*")
			.get("/bg/newapplication")
			.headers(headers.headers_0)
			.check(css("#client-form input[name='form_build_id']", "value").saveAs("form_build"))
			.check(css("#client-form input[name='form_token']", "value").saveAs("form_token")))
			.exec(http("/system/ajax/*POST*")
				.post("/system/ajax")
				.headers(headers.headers_0)
				.formParam("ztype", "ik")
				.formParam("form_build_id", "${form_build}")
				.formParam("form_token", "${form_token}")
				.formParam("form_id", "client_form")
				.formParam("username", "${username}")
				.formParam("_triggering_element_name", "username"))
			.exec(http("/bg/newapplication/*POST*")
				.post("/bg/newapplication")
				.headers(headers.headers_0)
				.formParam("form_build_id", "${form_build}")
				.formParam("form_token", "${form_token}")
				.formParam("form_id", "client_form"))
				.check(status.is(200))
				.check(css("span.z-num").saveAs("znum")))
	}


	def params(feeder: FeederBuilder[_], baseURL: String): ChainBuilder = {
		exec(http("/user/${id}/${znum}/edit/params")
			.get("/user/${id}/${znum}/edit/params")
			.headers(headers.headers_0)
			.check(css("#params-form input[name='form_build_id']", "value").saveAs("form_build_params"))
			.check(css("#params-form input[name='form_token']", "value").saveAs("form_token_params"))
			.check(css("#form input[name='form_build_id']", "value").saveAs("build_save"))
			.check(css("#form input[name='form_token']", "value").saveAs("token_save")))
			.exec(http("/system/ajax*Save*")
				.post("/system/ajax")
				.headers(headers.headers_84)
				.formParam("form_build_id", "${build_save}")
				.formParam("form_token", "${token_save}")
				.formParam("form_id", "form")
				.formParam("_triggering_element_name", "zsave")
				.formParam("_triggering_element_value", "Save"))
			.feed(lotnums)
			.exec(http("/user/${id}/${znum}/edit/params")
				.post("/user/${id}/${znum}/edit/params")
				.headers(headers.headers_0)
				.formParam("lotnum", "${lotnum}")
				.formParam("form_build_id", "${form_build_params}")
				.formParam("form_token", "${form_token_params}")
				.formParam("form_id", "params_form")
				.formParam("closed", "n")
				.formParam("dateFrom", "06.12.2018")
				.formParam("dateTo", "27.02.2019")
				.formParam("moneytype", "rub")
				.formParam("comment", "")
				.formParam("tosavebut", "Save")
				.formParam("totab", baseURL + "/user/${id}/${znum}/edit/next"))
	}

	def finance(feeder: FeederBuilder[_]): ChainBuilder ={
		exec(http("/user/$[id}/${znum}/edit/finance")
			.post("/user/${id}/${znum}/edit/finance")
			.headers(headers.headers_0)
			.body(RawFileBody("FinanceBody.txt"))
		      .formParam("form_build_id", "${build_buh}")
		      .formParam("form_token", "${token_buh}")
		      .formParam("form_id", "finance_form"))
	}
}
