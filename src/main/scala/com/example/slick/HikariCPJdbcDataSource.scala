package com.example.slick

import java.sql.{Connection, Driver}

import com.github.gquintana.metrics.sql.MetricsSql
import com.typesafe.config.Config
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import slick.jdbc.{JdbcDataSource, JdbcDataSourceFactory}
import slick.util.ConfigExtensionMethods._

/** A JdbcDataSource for a HikariCP connection pool.
  * See `slick.jdbc.JdbcBackend#Database.forConfig` for documentation on the config parameters. */
class HikariCPJdbcDataSource(val ds: HikariDataSource, val hconf: HikariConfig)
  extends JdbcDataSource {

  val monitoredDs = MetricsSql.forRegistry(Metrics.registry)
    .withDefaultNamingStrategy("h2")
    .wrap(ds)

  def createConnection(): Connection = monitoredDs.getConnection()

  def close(): Unit = ds.close()

  override val maxConnections: Option[Int] = Some(ds.getMaximumPoolSize)
}

object HikariCPJdbcDataSource extends JdbcDataSourceFactory {
  import com.zaxxer.hikari._

  def forConfig(c: Config, driver: Driver, name: String, classLoader: ClassLoader): HikariCPJdbcDataSource = {
    val hconf = new HikariConfig()

    // Connection settings
    if (c.hasPath("dataSourceClass")) {
      hconf.setDataSourceClassName(c.getString("dataSourceClass"))
    } else {
      Option(c.getStringOr("driverClassName", c.getStringOr("driver"))).map(hconf.setDriverClassName _)
    }
    hconf.setJdbcUrl(c.getStringOr("url", null))
    c.getStringOpt("user").foreach(hconf.setUsername)
    c.getStringOpt("password").foreach(hconf.setPassword)
    c.getPropertiesOpt("properties").foreach(hconf.setDataSourceProperties)

    // Pool configuration
    hconf.setConnectionTimeout(c.getMillisecondsOr("connectionTimeout", 1000))
    hconf.setValidationTimeout(c.getMillisecondsOr("validationTimeout", 1000))
    hconf.setIdleTimeout(c.getMillisecondsOr("idleTimeout", 600000))
    hconf.setMaxLifetime(c.getMillisecondsOr("maxLifetime", 1800000))
    hconf.setLeakDetectionThreshold(c.getMillisecondsOr("leakDetectionThreshold", 0))
    c.getStringOpt("connectionTestQuery").foreach(hconf.setConnectionTestQuery)
    c.getStringOpt("connectionInitSql").foreach(hconf.setConnectionInitSql)
    val numThreads = c.getIntOr("numThreads", 20)
    hconf.setMaximumPoolSize(c.getIntOr("maxConnections", numThreads * 5))
    hconf.setMinimumIdle(c.getIntOr("minConnections", numThreads))
    hconf.setPoolName(c.getStringOr("poolName", name))
    hconf.setRegisterMbeans(c.getBooleanOr("registerMbeans", false))

    // Equivalent of ConnectionPreparer
    hconf.setReadOnly(c.getBooleanOr("readOnly", false))
    c.getStringOpt("isolation").map("TRANSACTION_" + _).foreach(hconf.setTransactionIsolation)
    hconf.setCatalog(c.getStringOr("catalog", null))

    val ds = new HikariDataSource(hconf)
    new HikariCPJdbcDataSource(ds, hconf)
  }
}
