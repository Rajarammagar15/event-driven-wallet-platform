package com.raj.audit.consumer;

import com.raj.audit.dto.WalletTransactionEvent;
import com.raj.audit.service.AuditProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuditConsumer {

    private final AuditProcessor auditProcessor;

    @KafkaListener(
            topics = "wallet-transactions",
            groupId = "audit-group")
    public void consume(
            WalletTransactionEvent event) {

        log.info(
                "Received Event={}",
                event);

        auditProcessor.process(event);
    }
}