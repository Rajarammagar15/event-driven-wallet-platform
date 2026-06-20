package com.raj.fraud.consumer;

import com.raj.events.WalletTransactionEvent;
import com.raj.fraud.processor.FraudProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FraudConsumer {

    private final FraudProcessor fraudProcessor;

    @KafkaListener(
            topics = "wallet-transactions",
            groupId = "fraud-group")
    public void consume(
            WalletTransactionEvent event) {

        if (!"DEBIT".equals(event.getTransactionType())) {
            return;
        }

        fraudProcessor.process(event);
    }
}
