package com.bibliotech.dto;

import jakarta.validation.constraints.NotNull;

public record CheckoutRequest(
    @NotNull Long bookId,
    @NotNull Long userId
) {
}

