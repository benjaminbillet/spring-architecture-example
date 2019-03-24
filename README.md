# Monitoring
For our monitoring, we want to export metrics to the [Prometheus](https://prometheus.io) time series database. It takes only a few minutes to run it locally: https://prometheus.io/docs/prometheus/latest/getting_started


Add this to `scrape configs` in `prometheus.yaml` (in your prometheus folder):
```
- job_name: 'myapp'
  scrape_interval: 1m
  metrics_path: '/actuator/prometheus'
  static_configs:
    - targets: ['localhost:8080']
```

Prometheus is based on pull-based metrics collection; your application expose an HTTP endpoint that is scraped regularly by Prometheus. Look at http://localhost:8080/actuator/prometheus to see the metrics exported to the prometheus format.

Prometheus exposes a web console (port 9090) that enables you to, e.g., [query data](https://prometheus.io/docs/prometheus/latest/querying/basics) and list targets: http://localhost:9090/targets. You should see the "myapp" target in the list.


## Spring Boot Actuator
The `/actuator/prometheus` endpoint is exposed using [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#production-ready), a module that can add monitoring (metrics exposure), auditing and health checking your application.

Actuator services are listed at http://localhost:8080/actuator, they are enabled using the application properties.


## Grafana
From prometheus, you can feed [Grafana](https://grafana.com) and create custom dashboards to monitor your spring application. Running a standalone grafana is easy too: http://docs.grafana.org/installation


## Micrometer
[Micrometer](https://micrometer.io) is, basically, the slf4j of monitoring: it provides you abstract meter concepts (jauges, timers, counters, etc.) and then let you plug a monitoring system behind it.

As an example, we add the micrometer `@Timed` annotation to all our REST endpoints to measure their execution time. By running localhost:8080/api/time you should see the following lines appears in the exported metrics (http://localhost:8080/actuator/prometheus): 

```
# TYPE method_timed_seconds summary
method_timed_seconds_count{class="my.app.api.UtilsEndpoint",exception="none",method="getTime",} 6.0
method_timed_seconds_sum{class="my.app.api.UtilsEndpoint",exception="none",method="getTime",} 0.005580141
# HELP method_timed_seconds_max  
# TYPE method_timed_seconds_max gauge
method_timed_seconds_max{class="my.app.api.UtilsEndpoint",exception="none",method="getTime",} 0.0
```
