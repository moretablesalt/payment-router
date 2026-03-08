package com.prudentialguarantee.paymentrouter.controller;

import com.prudentialguarantee.paymentrouter.service.RouterService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/payment")
public class DatafeedController {

    private final RouterService routerService;

    @PostMapping(
            value = "/datafeed",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    public ResponseEntity<String> receive(
            @RequestBody MultiValueMap<String,String> formData) {

        logPayload(formData);

        routerService.saveEvent(formData);

        return ResponseEntity.ok("OK");
    }

    private void logPayload(MultiValueMap<String, String> formData) {

        log.info("======= PesoPay Datafeed =======");

        formData.forEach((key, value) ->
                log.info("{} = {}", key, value.isEmpty() ? null : value.get(0))
        );

        log.info("================================");
    }
}
