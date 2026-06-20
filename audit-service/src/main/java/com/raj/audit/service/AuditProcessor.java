package com.raj.audit.service;

import com.raj.events.WalletTransactionEvent;
import com.raj.audit.entity.AuditTransactionLog;
import com.raj.audit.mapper.AuditMapper;
import com.raj.audit.repository.AuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditProcessor {

    private final AuditMapper auditMapper;
    private final AuditRepository auditRepository;

    public void process(
            WalletTransactionEvent event) {

        AuditTransactionLog auditLog =
                auditMapper.toEntity(event);

        auditRepository.save(auditLog);

        log.info(
                "Audit Log Stored For Transaction={}",
                event.getTransactionId());
    }
}