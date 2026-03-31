package com.bibliotech.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

public record BookDTO(
    Long id,
    @NotBlank String isbn,
    @NotBlank String title,
    @NotNull @Min(0) Integer stockDisponible,
    String authorName,
    Set<String> categories
) {
}

