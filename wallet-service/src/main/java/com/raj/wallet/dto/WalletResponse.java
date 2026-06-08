package com.raj.wallet.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class WalletResponse {

    private UUID walletId;
    private String customerId;
    private BigDecimal balance;
    private String status;
}