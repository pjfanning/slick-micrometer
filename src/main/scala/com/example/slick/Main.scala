package com.example.slick

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

object Main extends App {
  val timeout = 10.seconds
  Await.result(ActionRunner.db.run(CountryCodeDao.createSchema), timeout)
  val ie = CountryCode(code = "ie", name = "Ireland")
  Await.result(ActionRunner.db.run(CountryCodeDao.insert(ie)), timeout)
  println(Await.result(ActionRunner.db.run(CountryCodeDao.all), timeout))
  println(Metrics.registry.scrape())
}
