# Rest-Service (Data Injection)

[![GitHub commit](https://img.shields.io/github/last-commit/darkkillex/rest-service)](https://github.com/darkkillex/rest-service)

Rest-service is a spring-boot microservices application developed to inject 1 or n object (in this case, "car"), randomly generated,
from a python-service to a java-service.
Java-service is Restful API that allows the data availability and mapping the CRUD ops.
The application provides parallel and series data injection.
Prometheus and Grafana were used to monitor the performance of the application.

#### Settings:
- you can use [this](https://github.com/darkkillex/rest-service/blob/master/java-service_dashboard_micrometer.json) grafana dashboard (based on Grafana Dashboard ID:4701)
- grafana URL data source: http://prometheus:9090
- prometheus "application.properties" : management.endpoints.web.exposure.include=*

#### Notes:
After dockerization, you can reach the following default endpoints:
- java-service: http://localhost:8080/car
- python-service: http://localhost:5000/injectdataparallel; http://localhost:5000/injectdataseries
- prometheus: http://localhost:8080/actuator/prometheus
- grafana: http://localhost:3000
- jenkins http://localhost:8081
