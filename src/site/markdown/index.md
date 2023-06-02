# OpenWMS.org API Gateway
An API Gateway that serves as a central entry point into the whole microservice network and takes care about security and routing.

![Build status][ci-image]

# Build and Run locally
```
$ ./mvnw package
$ java -jar target/openwms-gateway-exec.jar 
```

# Release
```
$ ./mvnw release:prepare
$ ./mvnw release:perform
```

# Resources
[![Build status](https://github.com/spring-labs/org.openwms.gateway/actions/workflows/master-build.yml/badge.svg)](https://github.com/spring-labs/org.openwms.gateway/actions/workflows/master-build.yml)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)
[![Maven central](https://img.shields.io/maven-central/v/org.openwms/org.openwms.gateway)](https://search.maven.org/search?q=a:org.openwms.gateway)
[![Docker pulls](https://img.shields.io/docker/pulls/interface21/openwms-gateway)](https://hub.docker.com/r/interface21/openwms-gateway)
[![Join the chat at https://gitter.im/openwms/org.openwms](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/openwms/org.openwms?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
