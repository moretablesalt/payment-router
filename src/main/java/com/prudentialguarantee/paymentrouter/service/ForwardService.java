package com.prudentialguarantee.paymentrouter.service;

import com.prudentialguarantee.paymentrouter.domain.DatafeedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class ForwardService {
    private final RestTemplate restTemplate;

    @Value("${backend.travel.url}")
    private String travelUrl;

    @Value("${backend.ofw.url}")
    private String ofwUrl;

    public ForwardService() {
        this.restTemplate = new RestTemplate();
    }

    @Async
    public void forward(DatafeedEvent event) {

        String targetUrl = determineTarget(event.getRoutedTo());

        if (targetUrl == null) {
            return;
        }

        log.info("Forwarding payment datafeed ref={} to {}",
                event.getReferenceNo(), targetUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> request =
                new HttpEntity<>(event.getPayloadRaw(), headers);

        log.debug("Payload being forwarded: {}", event.getPayloadRaw());

        ResponseEntity<String> response = restTemplate.postForEntity(targetUrl, request, String.class);

        log.info("Forward success ref={} status={}",
                event.getReferenceNo(), response.getStatusCode());
    }

    private String determineTarget(String route) {

        if ("TRAVEL".equals(route)) {
            return travelUrl;
        }

        if ("OFW".equals(route)) {
            return ofwUrl;
        }

        return null;
    }
}
