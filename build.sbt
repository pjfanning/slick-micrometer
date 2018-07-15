name := "slick-micrometer"

version := "0.1"

scalaVersion := "2.12.6"

resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.2.3",
  "com.zaxxer" % "HikariCP" % "3.2.0",
  "com.h2database" % "h2" % "1.4.197",
  "com.github.pjfanning" % "metrics-sql" % "4.0.0-SNAPSHOT",
  "io.micrometer" % "micrometer-registry-prometheus" % "1.0.5",
  "ch.qos.logback" % "logback-classic" % "1.2.3"
)
