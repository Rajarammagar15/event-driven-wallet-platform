package com.raj.wallet.service;

import com.raj.wallet.dto.*;

import java.util.UUID;

public interface WalletService {

    WalletResponse createWallet(
            CreateWalletRequest request);

    WalletResponse creditWallet(
            UUID walletId,
            CreditWalletRequest request);

    WalletResponse debitWallet(
            UUID walletId,
            DebitWalletRequest request);

    BalanceResponse getBalance(
            UUID walletId);
}