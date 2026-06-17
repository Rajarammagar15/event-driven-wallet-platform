package com.raj.audit.mapper;

import com.raj.audit.dto.WalletTransactionEvent;
import com.raj.audit.entity.AuditTransactionLog;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuditMapper {

    public AuditTransactionLog toEntity(
            WalletTransactionEvent event) {

        Map<String, String> payload = new HashMap<>();

        payload.put(
                "transactionId",
                String.valueOf(
                        event.getTransactionId()));

        payload.put(
                "walletId",
                String.valueOf(
                        event.getWalletId()));

        payload.put(
                "customerId",
                event.getCustomerId());

        payload.put(
                "amount",
                String.valueOf(
                        event.getAmount()));

        payload.put(
                "transactionType",
                event.getTransactionType());

        payload.put(
                "status",
                event.getStatus());

        payload.put(
                "eventTime",
                String.valueOf(
                        event.getEventTime()));

        return AuditTransactionLog.builder()
                .transactionId(String.valueOf(event.getTransactionId()))
                .walletId(String.valueOf(event.getWalletId()))
                .customerId(event.getCustomerId())
                .eventType(event.getTransactionType())
                .payload(payload)
                .sourceService("WALLET_SERVICE")
                .receivedAt(LocalDateTime.now())
                .build();
    }
}