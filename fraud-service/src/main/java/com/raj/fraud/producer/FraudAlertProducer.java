package com.raj.fraud.producer;

import com.raj.events.FraudAlertEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FraudAlertProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC =
            "fraud-alerts";

    public void publishAlert(
            FraudAlertEvent event) {

        kafkaTemplate.send(
                TOPIC,
                event.getCustomerId(),
                event);

        log.info(
                "Fraud Alert Published : {}",
                event);
    }
}