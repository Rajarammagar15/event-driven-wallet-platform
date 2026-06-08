package com.raj.wallet.controller;

import com.raj.wallet.dto.*;
import com.raj.wallet.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping
    public WalletResponse createWallet(
            @Valid @RequestBody
            CreateWalletRequest request) {

        return walletService.createWallet(request);
    }

    @PostMapping("/{walletId}/credit")
    public WalletResponse creditWallet(
            @PathVariable UUID walletId,
            @RequestBody @Valid
            CreditWalletRequest request) {

        return walletService.creditWallet(
                walletId,
                request);
    }

    @PostMapping("/{walletId}/debit")
    public WalletResponse debitWallet(
            @PathVariable UUID walletId,
            @RequestBody @Valid
            DebitWalletRequest request) {

        return walletService
                .debitWallet(
                        walletId,
                        request);
    }

    @GetMapping("/{walletId}/balance")
    public BalanceResponse getBalance(
            @PathVariable UUID walletId) {

        return walletService
                .getBalance(walletId);
    }
}