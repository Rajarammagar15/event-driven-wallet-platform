package com.raj.notification.service;

import com.raj.events.FraudAlertEvent;
import com.raj.notification.entity.NotificationEventLog;
import com.raj.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class FraudAlertProcessor {

    private final NotificationSender notificationSender;

    private final NotificationRepository repository;

    public void process(
            FraudAlertEvent event) {

        notificationSender.sendFraudAlert(event);

        NotificationEventLog log =
                NotificationEventLog.builder()
                        .customerId(event.getCustomerId())
                        .transactionId(event.getTransactionId())
                        .walletId(event.getWalletId())
                        .transactionType("DEBIT")
                        .message(
                            "Suspicious transaction detected. Rule="
                                    + event.getRule())
                        .notificationType("FRAUD_ALERT")
                        .status("SENT")
                        .createdAt(LocalDateTime.now())
                        .build();

        repository.save(log);
    }
}