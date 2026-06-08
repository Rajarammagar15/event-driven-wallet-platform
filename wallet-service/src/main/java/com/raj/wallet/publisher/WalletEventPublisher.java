package com.raj.wallet.publisher;

import com.raj.wallet.dto.WalletTransactionEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletEventPublisher {

    private final KafkaTemplate<
                String,
                WalletTransactionEvent> kafkaTemplate;

    private static final String TOPIC =
            "wallet-transactions";

    public void publish(
            WalletTransactionEvent event) {

        kafkaTemplate.send(
                TOPIC,
                event.getWalletId().toString(),
                event);
    }
}