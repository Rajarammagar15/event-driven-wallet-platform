package com.raj.audit.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "audit_logs")
public class AuditTransactionLog {

    @Id
    private String id;

    private String transactionId;

    private String walletId;

    private String customerId;

    private String eventType;

    private Map<String, String> payload;

    private String sourceService;

    private LocalDateTime receivedAt;
}