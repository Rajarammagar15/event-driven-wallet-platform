package com.raj.wallet.mapper;

import com.raj.wallet.dto.WalletResponse;
import com.raj.wallet.entity.Wallet;

public class WalletMapper {

    public static WalletResponse toResponse(
            Wallet wallet) {

        return WalletResponse.builder()
                .walletId(wallet.getWalletId())
                .customerId(wallet.getCustomerId())
                .balance(wallet.getBalance())
                .status(wallet.getStatus())
                .build();
    }
}