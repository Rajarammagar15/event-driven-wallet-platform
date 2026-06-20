package com.raj.notification.consumer;

import com.raj.events.FraudAlertEvent;
import com.raj.notification.service.FraudAlertProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FraudAlertConsumer {

    private final FraudAlertProcessor fraudAlertProcessor;

    @KafkaListener(
            topics = "fraud-alerts",
            groupId = "notification-group")
    public void consume(
            FraudAlertEvent event) {

        log.info(
                "Fraud Alert Received : {}",
                event);

        fraudAlertProcessor.process(event);
    }
}