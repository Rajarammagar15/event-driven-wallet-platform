package com.raj.fraud.rules;

import com.raj.events.WalletTransactionEvent;
import com.raj.fraud.dto.FraudDecision;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class RuleEngine {

    public FraudDecision evaluate(
            WalletTransactionEvent event,
            Long txnCountLastMinute,
            BigDecimal dailyAmount) {

        if (event.getAmount()
                .compareTo(BigDecimal.valueOf(50000)) > 0) {

            return FraudDecision.builder()
                    .fraud(true)
                    .rule("HIGH_AMOUNT")
                    .riskScore(80)
                    .build();
        }

        if (txnCountLastMinute > 3) {

            return FraudDecision.builder()
                    .fraud(true)
                    .rule("VELOCITY_CHECK")
                    .riskScore(70)
                    .build();
        }

        if (dailyAmount.compareTo(
                BigDecimal.valueOf(100000)) > 0) {

            return FraudDecision.builder()
                    .fraud(true)
                    .rule("DAILY_LIMIT")
                    .riskScore(90)
                    .build();
        }

        return FraudDecision.builder()
                .fraud(false)
                .rule("NONE")
                .riskScore(0)
                .build();
    }
}