package com.raj.fraud.repository;

import com.raj.fraud.entity.FraudEventLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FraudRepository extends MongoRepository<FraudEventLog, String> {
    boolean existsByTransactionId(String transactionId);
}
