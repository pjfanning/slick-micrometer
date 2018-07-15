package com.example.slick

import com.typesafe.config.ConfigFactory
import slick.jdbc.{JdbcBackend, JdbcProfile}
import slick.util.ClassLoaderUtil

import scala.concurrent.Future

class ActionRunner extends JdbcProfile {

  import api._

  def run[T](actions: DBIOAction[T, NoStream, Effect.All]): Future[T] = {
    ActionRunner.db.run(actions.transactionally)
  }
}

object ActionRunner extends JdbcBackend {

  val SlickConfigPath = "slick.dbs.default.db"

  lazy val datasource =
    HikariCPJdbcDataSource.forConfig(ConfigFactory.load().getConfig(SlickConfigPath), null, "db", ClassLoaderUtil.defaultClassLoader)
  lazy val db = Database.forSource(datasource)
}