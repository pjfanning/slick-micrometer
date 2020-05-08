name := "slick-micrometer"

version := "0.1"

scalaVersion := "2.13.2"

resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.3.2",
  "com.zaxxer" % "HikariCP" % "3.4.3",
  "com.h2database" % "h2" % "1.4.200",
  "com.github.pjfanning" % "metrics-sql" % "4.0.0-SNAPSHOT",
  "io.micrometer" % "micrometer-registry-prometheus" % "1.5.0",
  "ch.qos.logback" % "logback-classic" % "1.2.3"
)
