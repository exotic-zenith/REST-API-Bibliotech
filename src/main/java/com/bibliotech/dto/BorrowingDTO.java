package com.bibliotech.dto;

import com.bibliotech.model.BorrowingStatus;
import java.time.LocalDate;

public record BorrowingDTO(
    Long id,
    Long bookId,
    Long userId,
    LocalDate borrowDate,
    LocalDate returnDate,
    BorrowingStatus status
) {
}

