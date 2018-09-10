package scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class HomePageSimulation extends Simulation {
	val concurrency:Int = 1
	val rampUpTime:Int =  0
	val holdForTime:Int = 0
	val iterationLimit:Int = 1

	val durationLimit:Int = rampUpTime + holdForTime


	var httpConf = http.baseURL("")
	)

	if (iterationLimit == null)
		scn = scn.forever{execution}
	else
		scn = scn.repeat(iterationLimit.toInt){execution}

	val virtualUsers =
		if (rampUpTime > 0)
			rampUsers(concurrency) over (rampUpTime seconds)
		else
			atOnceUsers(concurrency)

	var testSetup = setUp(scn.inject(virtualUsers).protocols(httpConf))

	if (durationLimit > 0)
		testSetup.maxDuration(durationLimit)

}
