package com.raj.audit.repository;

import com.raj.audit.entity.AuditTransactionLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuditRepository
        extends MongoRepository<AuditTransactionLog, String> {
}