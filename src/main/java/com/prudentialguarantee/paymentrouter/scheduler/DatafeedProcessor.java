package com.prudentialguarantee.paymentrouter.scheduler;

import com.prudentialguarantee.paymentrouter.domain.DatafeedEvent;
import com.prudentialguarantee.paymentrouter.repository.DatafeedEventRepository;
import com.prudentialguarantee.paymentrouter.service.ForwardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatafeedProcessor {
    private final DatafeedEventRepository repository;
    private final ForwardService forwardService;

    @Scheduled(fixedDelay = 5000)
    public void processEvents() {

        List<DatafeedEvent> events =
                repository.findTop50ByStatusOrderByReceivedAt("RECEIVED");

        if(events.isEmpty()) {
            return;
        }

        log.info("Processing {} pending datafeed events", events.size());

        for (DatafeedEvent event : events) {

            try {

                event.setStatus("PROCESSING");
                repository.save(event);
                log.info("Started processing ref={}", event.getReferenceNo());
                forwardService.forward(event);

                log.info("Successfully processed ref={}", event.getReferenceNo());
                event.setStatus("FORWARDED");
                repository.save(event);

            } catch (Exception ex) {

                log.error("Failed processing ref={}", event.getReferenceNo(), ex);

                event.setStatus("FAILED");
                repository.save(event);
            }
        }
    }
}
