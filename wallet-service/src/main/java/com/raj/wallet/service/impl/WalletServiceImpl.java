package com.raj.wallet.service.impl;

import com.raj.events.WalletTransactionEvent;
import com.raj.wallet.dto.*;
import com.raj.wallet.entity.TransactionType;
import com.raj.wallet.entity.Wallet;
import com.raj.wallet.entity.WalletTransaction;
import com.raj.wallet.mapper.WalletMapper;
import com.raj.wallet.publisher.WalletEventPublisher;
import com.raj.wallet.repository.TransactionRepository;
import com.raj.wallet.repository.WalletRepository;
import com.raj.wallet.service.WalletCacheService;
import com.raj.wallet.service.WalletService;
import com.raj.wallet.util.WalletValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl
        implements WalletService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final WalletValidator walletValidator;
    private final WalletCacheService walletCacheService;
    private final WalletEventPublisher eventPublisher;

    @Override
    public WalletResponse createWallet(
            CreateWalletRequest request) {

        Wallet wallet = Wallet.builder()
                .customerId(request.getCustomerId())
                .balance(BigDecimal.ZERO)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        wallet = walletRepository.save(wallet);

        return WalletMapper.toResponse(wallet);
    }

    @Transactional
    @Override
    public WalletResponse creditWallet(
            UUID walletId,
            CreditWalletRequest request) {

        Wallet wallet =
                walletRepository
                        .findByWalletIdForUpdate(walletId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Wallet not found"));

        wallet.setBalance(
                wallet.getBalance()
                        .add(request.getAmount()));

        wallet.setUpdatedAt(
                LocalDateTime.now());

        WalletTransaction transaction =
                WalletTransaction.builder()
                        .walletId(walletId)
                        .amount(request.getAmount())
                        .transactionType(
                                TransactionType.CREDIT)
                        .status("SUCCESS")
                        .createdAt(LocalDateTime.now())
                        .build();

        transactionRepository.save(transaction);

        walletRepository.save(wallet);

        walletCacheService.saveBalance(
                wallet.getWalletId(),
                wallet.getBalance());

        WalletTransactionEvent event =
                WalletTransactionEvent.builder()
                        .transactionId(
                                transaction.getTransactionId())
                        .walletId(wallet.getWalletId())
                        .customerId(
                                wallet.getCustomerId())
                        .amount(
                                request.getAmount())
                        .transactionType("CREDIT")
                        .status("SUCCESS")
                        .eventTime(LocalDateTime.now().toString())
                        .build();

        eventPublisher.publish(event);

        return WalletMapper.toResponse(wallet);
    }

    @Transactional
    @Override
    public WalletResponse debitWallet(
            UUID walletId,
            DebitWalletRequest request) {

        Wallet wallet =
                walletRepository
                        .findByWalletIdForUpdate(
                                walletId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Wallet not found"));

        walletValidator
                .validateWalletActive(wallet);

        walletValidator
                .validateSufficientBalance(
                        wallet,
                        request.getAmount());

        wallet.setBalance(
                wallet.getBalance()
                        .subtract(
                                request.getAmount()));

        wallet.setUpdatedAt(
                LocalDateTime.now());

        WalletTransaction transaction =
                WalletTransaction.builder()
                        .walletId(walletId)
                        .amount(request.getAmount())
                        .transactionType(
                                TransactionType.DEBIT)
                        .status("SUCCESS")
                        .createdAt(
                                LocalDateTime.now())
                        .build();

        transactionRepository.save(transaction);

        walletRepository.save(wallet);

        walletCacheService.saveBalance(
                wallet.getWalletId(),
                wallet.getBalance());

        WalletTransactionEvent event =
                WalletTransactionEvent.builder()
                        .transactionId(
                                transaction.getTransactionId())
                        .walletId(wallet.getWalletId())
                        .customerId(
                                wallet.getCustomerId())
                        .amount(
                                request.getAmount())
                        .transactionType("DEBIT")
                        .status("SUCCESS")
                        .eventTime(LocalDateTime.now().toString())
                        .build();

        eventPublisher.publish(event);
        return WalletMapper
                .toResponse(wallet);
    }

    @Override
    public BalanceResponse getBalance(
            UUID walletId) {

        BigDecimal cachedBalance =
                walletCacheService
                        .getBalance(walletId);

        if (cachedBalance != null) {

            System.out.println(
                    "Balance fetched from Redis");

            return BalanceResponse.builder()
                    .walletId(walletId)
                    .balance(cachedBalance)
                    .build();
        }

        Wallet wallet =
                walletRepository.findById(walletId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Wallet not found"));

        walletCacheService.saveBalance(
                walletId,
                wallet.getBalance());

        System.out.println(
                "Balance fetched from PostgreSQL");

        return BalanceResponse.builder()
                .walletId(walletId)
                .balance(wallet.getBalance())
                .build();
    }
}