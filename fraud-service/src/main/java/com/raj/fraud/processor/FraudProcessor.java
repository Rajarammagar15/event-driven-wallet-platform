package com.raj.fraud.processor;

import com.raj.events.WalletTransactionEvent;
import com.raj.fraud.service.FraudService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FraudProcessor {

    private final FraudService fraudService;

    public void process(
            WalletTransactionEvent event) {

        fraudService.process(event);
    }
}