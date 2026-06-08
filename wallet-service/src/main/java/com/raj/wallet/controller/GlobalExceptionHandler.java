package com.raj.wallet.controller;

import com.raj.wallet.exception.InsufficientBalanceException;
import com.raj.wallet.exception.WalletInactiveException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(
            InsufficientBalanceException.class)
    public ResponseEntity<String>
    handleInsufficientBalance(
            InsufficientBalanceException ex) {

        return ResponseEntity
                .badRequest()
                .body(ex.getMessage());
    }

    @ExceptionHandler(
            WalletInactiveException.class)
    public ResponseEntity<String>
    handleWalletInactive(
            WalletInactiveException ex) {

        return ResponseEntity
                .badRequest()
                .body(ex.getMessage());
    }
}