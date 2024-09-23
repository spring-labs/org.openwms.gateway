# OpenLeap.io API Gateway
The OpenLeap.io API Gateway serves as a central entry point to the OpenLeap.io microservice network and takes care about security and
routing. It is build on top of [Spring Cloud Gateway](https://spring.io/projects/spring-cloud-gateway/) and is configured and customized in
a way to work well in the distributed OpenLeap.io microservice environment. In addition to the opensource version there is the OpenLeap.io
API Gateway ENTERPRISE edition that is extended with features to support [OAuth2.0](https://datatracker.ietf.org/doc/html/rfc6749) and
[OpenID Connect](https://openid.net/developers/how-connect-works/) authorization and authentication at the edge.

# Build and Run locally
```
$ ./mvnw package
$ java -jar target/openleap-gateway-exec.jar 
```

# Release
```
$ ./mvnw release:prepare
$ ./mvnw release:perform
```

# Resources
[![Build status](https://github.com/openleap-io/io.openleap.gateway/actions/workflows/master-build.yml/badge.svg)](https://github.com/openleap.io/io.openleap.gateway/actions/workflows/master-build.yml)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)
[![Maven central](https://img.shields.io/maven-central/v/io.openleap/io.openleap.gateway)](https://search.maven.org/search?q=a:io.openleap.gateway)
[![Docker pulls](https://img.shields.io/docker/pulls/openleap/io.openleap.gateway)](https://hub.docker.com/r/openleap/io.openleap.gateway)
