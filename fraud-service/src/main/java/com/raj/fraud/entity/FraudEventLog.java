package com.raj.fraud.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "fraud_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FraudEventLog {

    @Id
    private String id;

    private String transactionId;

    private String walletId;

    private String customerId;

    private BigDecimal amount;

    private String transactionType;

    private String triggeredRule;

    private String fraudStatus;

    private Integer riskScore;

    private LocalDateTime createdAt;
}