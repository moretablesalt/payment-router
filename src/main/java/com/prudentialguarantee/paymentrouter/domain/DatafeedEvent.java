package com.prudentialguarantee.paymentrouter.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "datafeed_event")
public class DatafeedEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String referenceNo;

    private String merchantId;

    private String gatewayReference;

    private BigDecimal amount;

    private String currency;

    private String successCode;

    @Column(columnDefinition = "TEXT")
    private String payloadRaw;

    @Column(columnDefinition = "TEXT")
    private String payloadDecoded;

    private String routedTo;

    private String status;

    private Instant receivedAt;

    private Integer retryCount;
}
