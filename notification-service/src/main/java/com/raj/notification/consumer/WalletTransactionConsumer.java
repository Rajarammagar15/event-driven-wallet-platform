package com.raj.notification.consumer;

import com.raj.notification.dto.WalletTransactionEvent;
import com.raj.notification.service.NotificationProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WalletTransactionConsumer {

    private final NotificationProcessor notificationProcessor;

    @KafkaListener(
            topics = "wallet-transactions",
            groupId = "notification-group")
    public void consume(
            WalletTransactionEvent event) {

        log.info(
                "Received Event : {}",
                event);

        notificationProcessor.process(event);
    }
}