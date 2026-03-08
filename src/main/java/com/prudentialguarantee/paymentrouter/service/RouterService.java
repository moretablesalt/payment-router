package com.prudentialguarantee.paymentrouter.service;

import com.prudentialguarantee.paymentrouter.domain.DatafeedEvent;
import com.prudentialguarantee.paymentrouter.repository.DatafeedEventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RouterService {
    private final DatafeedEventRepository repository;

    public void saveEvent(MultiValueMap<String,String> formData) {

        DatafeedEvent event = new DatafeedEvent();

        event.setReferenceNo(formData.getFirst("Ref"));
        event.setMerchantId(formData.getFirst("MerchantId"));
        event.setGatewayReference(formData.getFirst("PayRef"));
        event.setCurrency(formData.getFirst("Cur"));
        event.setSuccessCode(formData.getFirst("successcode"));

        String amt = formData.getFirst("Amt");
        if (amt != null) {
            event.setAmount(new BigDecimal(amt));
        }

        event.setPayloadRaw(buildRawPayload(formData));
        event.setPayloadDecoded(buildDecodedPayload(formData));

        event.setRoutedTo(determineRoute(event.getReferenceNo()));

        event.setStatus("RECEIVED");
        event.setRetryCount(0);
        event.setReceivedAt(Instant.now());

        repository.save(event);
    }
    private String buildRawPayload(MultiValueMap<String,String> formData) {

        return formData.entrySet()
                .stream()
                .flatMap(e -> e.getValue().stream()
                        .map(v -> e.getKey() + "=" + v))
                .collect(Collectors.joining("&"));
    }

    private String buildDecodedPayload(MultiValueMap<String,String> formData) {

        return formData.entrySet()
                .stream()
                .flatMap(e -> e.getValue().stream()
                        .map(v -> e.getKey() + "=" + v))
                .collect(Collectors.joining("\n"));
    }

    private String determineRoute(String ref) {

        if (ref == null) {
            return "UNKNOWN";
        }

        if (ref.startsWith("TRV")) {
            return "TRAVEL";
        }

        if (ref.startsWith("OFW")) {
            return "OFW";
        }

        return "UNKNOWN";
    }
}
