package com.raj.notification.service;

import com.raj.notification.dto.WalletTransactionEvent;
import com.raj.notification.entity.NotificationEventLog;
import com.raj.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationProcessor {

    private final NotificationSender notificationSender;
    private final NotificationRepository notificationRepository;

    public void process(
            WalletTransactionEvent event) {

        String message = buildMessage(event);

        notificationSender.send(
                event.getCustomerId(),
                message);

        NotificationEventLog notificationLog =
                NotificationEventLog.builder()
                        .transactionId(
                                event.getTransactionId())
                        .walletId(
                                event.getWalletId())
                        .customerId(
                                event.getCustomerId())
                        .message(message)
                        .transactionType(
                                event.getTransactionType())
                        .status("SENT")
                        .createdAt(
                                LocalDateTime.now())
                        .build();

        notificationRepository.save(notificationLog);
    }

    private String buildMessage(
            WalletTransactionEvent event) {

        if ("CREDIT".equalsIgnoreCase(
                event.getTransactionType())) {

            return String.format(
                    "Wallet credited by ₹%s",
                    event.getAmount());
        }

        if ("DEBIT".equalsIgnoreCase(
                event.getTransactionType())) {

            return String.format(
                    "Wallet debited by ₹%s",
                    event.getAmount());
        }

        return "Wallet transaction processed";
    }
}