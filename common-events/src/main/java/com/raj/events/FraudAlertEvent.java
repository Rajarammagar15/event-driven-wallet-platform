package com.raj.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FraudAlertEvent {

    private UUID transactionId;

    private UUID walletId;

    private String customerId;

    private String rule;

    private Integer riskScore;

    private String createdAt;
}