/*
 * Copyright 2005-2023 the original author or authors.
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
package io.openleap.gateway.app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import java.net.URI;

/**
 * A ApiGatewaySecurityConfiguration.
 *
 * @author Heiko Scherrer
 */
@Configuration
@EnableWebFluxSecurity
class ApiGatewaySecurityConfiguration implements WebFluxConfigurer {

    @Value("${oleap.security.basic-auth}")
    private boolean basicAuthEnabled;
    @Value("${oleap.security.logout-success-url}")
    private String logoutSuccessUrl;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("POST, GET, DELETE, PUT, PATCH")
                .allowedOrigins("*");
    }

    @Bean
    @Profile("!BasicAuth & !oauth2")
    public SecurityWebFilterChain springNoAuthSecurityFilterChain(ServerHttpSecurity http) {
        http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange( auth -> auth.anyExchange().permitAll());
        return http.build();
    }

    @Bean
    @Profile("BasicAuth")
    public SecurityWebFilterChain springBasicAuthSecurityFilterChain(ServerHttpSecurity http) {
        http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(Customizer.withDefaults())
                .authorizeExchange( auth -> auth.pathMatchers("/actuator/**").permitAll()
                        .anyExchange().authenticated());

//        if (basicAuthEnabled) {
            http.httpBasic(Customizer.withDefaults());
//        }
        return http.build();
    }

    @Bean
    @Profile("oauth2")
    public SecurityWebFilterChain springIDPSecurityFilterChain(ServerHttpSecurity http) {
        http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .oauth2Login(Customizer.withDefaults())
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()))
                .authorizeExchange( auth -> auth.pathMatchers("/actuator/**").permitAll()
                        .anyExchange().authenticated());
        return http.build();
    }

    public ServerLogoutSuccessHandler logoutSuccessHandler(String uri) {
        //        OidcClientInitiatedServerLogoutSuccessHandler successHandler = new OidcClientInitiatedServerLogoutSuccessHandler();
        var successHandler = new RedirectServerLogoutSuccessHandler();
        successHandler.setLogoutSuccessUrl(URI.create(uri));
        return successHandler;
    }
}
