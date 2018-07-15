package com.example.slick

import io.micrometer.prometheus.{PrometheusConfig, PrometheusMeterRegistry}

object Metrics {
  val registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT)
}
