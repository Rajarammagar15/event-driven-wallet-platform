package com.raj.wallet.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateWalletRequest {

    @NotBlank
    private String customerId;
}