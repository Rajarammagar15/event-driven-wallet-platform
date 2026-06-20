package com.raj.fraud.service;

import com.raj.events.FraudAlertEvent;
import com.raj.events.WalletTransactionEvent;
import com.raj.fraud.dto.FraudDecision;
import com.raj.fraud.entity.FraudEventLog;
import com.raj.fraud.producer.FraudAlertProducer;
import com.raj.fraud.repository.FraudRepository;
import com.raj.fraud.rules.RuleEngine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class FraudService {

    private final RedisService redisService;
    private final RuleEngine ruleEngine;
    private final FraudRepository fraudRepository;
    private final FraudAlertProducer fraudAlertProducer;

    public void process(
            WalletTransactionEvent event) {

        if (fraudRepository.existsByTransactionId(
                String.valueOf(event.getTransactionId()))) {

            log.info(
                    "Duplicate Event Ignored {}",
                    event.getTransactionId());
            return;
        }

        Long txnCount =
                redisService.incrementTransactionCount(
                        event.getCustomerId());

        BigDecimal dailyAmount =
                redisService.updateDailyAmount(
                        event.getCustomerId(),
                        event.getAmount());

        FraudDecision decision =
                ruleEngine.evaluate(
                        event,
                        txnCount,
                        dailyAmount);

        FraudEventLog logEntity =
                FraudEventLog.builder()
                        .transactionId(
                                String.valueOf(event.getTransactionId()))
                        .walletId(
                                String.valueOf(event.getWalletId()))
                        .customerId(
                                event.getCustomerId())
                        .amount(
                                event.getAmount())
                        .transactionType(
                                event.getTransactionType())
                        .triggeredRule(
                                decision.getRule())
                        .fraudStatus(
                                decision.isFraud()
                                        ? "FLAGGED"
                                        : "CLEAR")
                        .riskScore(
                                decision.getRiskScore())
                        .createdAt(
                                LocalDateTime.now())
                        .build();

        fraudRepository.save(logEntity);

        if (decision.isFraud()) {

            FraudAlertEvent alert =
                    FraudAlertEvent.builder()
                            .transactionId(
                                    event.getTransactionId())
                            .walletId(
                                    event.getWalletId())
                            .customerId(
                                    event.getCustomerId())
                            .rule(
                                    decision.getRule())
                            .riskScore(
                                    decision.getRiskScore())
                            .createdAt(
                                    LocalDateTime.now().toString())
                            .build();

            fraudAlertProducer.publishAlert(alert);
        }

        log.info(
                "Fraud Evaluation Complete : {}",
                decision);
    }
}