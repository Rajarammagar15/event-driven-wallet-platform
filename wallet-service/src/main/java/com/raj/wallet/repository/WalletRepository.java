package com.raj.wallet.repository;

import com.raj.wallet.entity.Wallet;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepository
        extends JpaRepository<Wallet, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select w
            from Wallet w
            where w.walletId = :walletId
            """)
    Optional<Wallet> findByWalletIdForUpdate(
            UUID walletId);
}