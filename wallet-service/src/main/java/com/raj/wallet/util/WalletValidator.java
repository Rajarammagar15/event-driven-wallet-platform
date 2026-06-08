package com.raj.wallet.util;

import com.raj.wallet.entity.Wallet;
import com.raj.wallet.exception.InsufficientBalanceException;
import com.raj.wallet.exception.WalletInactiveException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class WalletValidator {

    public void validateWalletActive(
            Wallet wallet) {

        if (!"ACTIVE".equals(wallet.getStatus())) {
            throw new WalletInactiveException(
                    "Wallet is inactive");
        }
    }

    public void validateSufficientBalance(
            Wallet wallet,
            BigDecimal amount) {

        if (wallet.getBalance()
                .compareTo(amount) < 0) {

            throw new InsufficientBalanceException(
                    "Insufficient balance");
        }
    }
}