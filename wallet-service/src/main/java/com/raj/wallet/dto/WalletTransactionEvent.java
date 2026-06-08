package com.raj.wallet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletTransactionEvent {

    private UUID transactionId;

    private UUID walletId;

    private String customerId;

    private BigDecimal amount;

    private String transactionType;

    private String status;

    private String eventTime;
}