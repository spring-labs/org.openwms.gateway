/*
 * Copyright 2005-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openwms.gateway.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import java.net.URI;

import static org.ameba.LoggingCategories.BOOT;

/**
 * A ApiGatewaySecurityConfiguration.
 *
 * @author Heiko Scherrer
 */
@Configuration
@EnableWebFluxSecurity
class ApiGatewaySecurityConfiguration implements WebFluxConfigurer {

    private static final Logger BOOT_LOGGER = LoggerFactory.getLogger(BOOT);
    @Value("${owms.security.basic-auth}")
    private boolean basicAuthEnabled;
    @Value("${owms.security.logout-success-url}")
    private String logoutSuccessUrl;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("POST, GET, DELETE, PUT, PATCH")
                .allowedOrigins("*");
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        if (basicAuthEnabled) {
            BOOT_LOGGER.info("BASIC authentication is enabled");
            http
                    .authorizeExchange()
                    .pathMatchers("/actuator/**").permitAll()
                    .anyExchange().authenticated()
            ;
            http.httpBasic();
        } else {
            BOOT_LOGGER.info("BASIC authentication is disabled");
            http
                    .authorizeExchange()
                    .pathMatchers("/**").permitAll()
                    .anyExchange().authenticated()
            ;
            http.httpBasic().disable();
        }
        http
                .csrf().disable()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(logoutSuccessHandler(logoutSuccessUrl))
        ;
        return http.build();
    }

    public ServerLogoutSuccessHandler logoutSuccessHandler(String uri) {
        //        OidcClientInitiatedServerLogoutSuccessHandler successHandler = new OidcClientInitiatedServerLogoutSuccessHandler();
        var successHandler = new RedirectServerLogoutSuccessHandler();
        successHandler.setLogoutSuccessUrl(URI.create(uri));
        return successHandler;
    }
}
