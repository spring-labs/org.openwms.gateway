package io.openleap.gateway.event;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.openleap.gateway.config.ClientRegistrationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientPropertiesMapper;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Component
public class ShutdownEvent implements ApplicationListener<ContextClosedEvent> {
    Logger logger = LoggerFactory.getLogger(ShutdownEvent.class);

    ClientRegistrationProperties clientRegistrationProperties;
    OAuth2ClientProperties clientProperties;

    public ShutdownEvent(ClientRegistrationProperties clientRegistrationProperties, OAuth2ClientProperties clientProperties) {
        this.clientRegistrationProperties = clientRegistrationProperties;
        this.clientProperties = clientProperties;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        logger.info("Shutting down application");
        Map<String, ClientRegistration> staticClients = (new OAuth2ClientPropertiesMapper(clientProperties)).asClientRegistrations();
        staticClients.forEach((key, value) -> unRegister(key, clientRegistrationProperties.getInstanceId()));
    }

    private void unRegister(String registrationId, String instanceId) {
        logger.info("Unregistering client with registrationId: {} and instanceId {}", registrationId, instanceId);
        String url = clientRegistrationProperties.getUnregistrationEndpoint();

        String bearerToken = createRegistrationToken();
        if (bearerToken == null) {
            logger.error("Failed to retrieve access token. Unable to unregister client with registrationId {} for instanceId {}. Exiting unregistration process.", registrationId, instanceId);
            return;
        }
        var headers = new HttpHeaders();
        headers.setBearerAuth(bearerToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate
                    .exchange(url.concat("/".concat(registrationId).concat("/").concat(instanceId)), HttpMethod.GET, requestEntity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("Successfully unregistered client: {}", registrationId);
            } else {
                logger.error("Failed to fetch resource. HTTP Status: {}", response.getStatusCode());
            }
        } catch (Exception e) {
            logger.error("Error occurred: {}", e.getMessage());
        }
    }

    private String createRegistrationToken() {
        URI url = clientRegistrationProperties.getTokenEndpoint();

        var headers = new HttpHeaders();
        headers.setBasicAuth(clientRegistrationProperties.getRegistrationUsername(), clientRegistrationProperties.getRegistrationPassword());
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        var requestBody = new LinkedMultiValueMap<String, String>();
        requestBody.put("grant_type", List.of("client_credentials"));
        requestBody.put("scope", List.of("client.create"));

        var request = new RequestEntity<>(
                requestBody,
                headers,
                HttpMethod.POST,
                url);

        RestTemplate restTemplate = new RestTemplate();

        String accessToken = null;
        try {
            var result = restTemplate.exchange(request, ObjectNode.class);
            if (result.getStatusCode().is2xxSuccessful()) {
                var body = result.getBody();
                if (body == null || body.get("access_token") == null) {
                    logger.error("Failed to retrieve access token from response body.");
                    return null;
                }
                accessToken = body.get("access_token").asText();
            } else {
                logger.error("Failed to retrieve access token. HTTP Status: {}", result.getStatusCode());
            }
        } catch (Exception e) {
            logger.error("Error occurred: {}", e.getMessage());
        }
        return accessToken;
    }
}