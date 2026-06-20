package com.raj.notification.service;

import com.raj.events.FraudAlertEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationSender {

    public void send(
            String customerId,
            String message) {

        log.info(
                "Sending notification to customer={} message={}",
                customerId,
                message);
    }

    public void sendFraudAlert(
            FraudAlertEvent event) {

        log.info("""
        =====================================
        FRAUD ALERT
        Customer : {}
        Rule     : {}
        Risk     : {}
        =====================================
        """,
                event.getCustomerId(),
                event.getRule(),
                event.getRiskScore());
    }
}