# OpenWMS.org API Gateway
The OpenWMS.org API Gateway serves as a central entry point to the OpenWMS.org microservice network and takes care about security and
routing. It is build on top of [Spring Cloud Gateway](https://spring.io/projects/spring-cloud-gateway/) and is configured and customized in
a way to work well in the distributed OpenWMS.org microservice environment. In addition to the opensource version there is the OpenWMS.org
API Gateway ENTERPRISE edition that is extended with features to support [OAuth2.0](https://datatracker.ietf.org/doc/html/rfc6749) and
[OpenID Connect](https://openid.net/developers/how-connect-works/) authorization and authentication at the edge service.

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
