package com.bibliotech.controller;

import com.bibliotech.dto.BorrowingDTO;
import com.bibliotech.dto.CheckoutRequest;
import com.bibliotech.mapper.BorrowingMapper;
import com.bibliotech.service.BorrowingService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/borrowings")
@RequiredArgsConstructor
public class BorrowingController {

    private final BorrowingService borrowingService;
    private final BorrowingMapper borrowingMapper;

    @PostMapping("/checkout")
    public ResponseEntity<BorrowingDTO> checkout(@Valid @RequestBody CheckoutRequest request) {
        BorrowingDTO dto = borrowingMapper.toDto(
            borrowingService.processBorrowing(request.bookId(), request.userId())
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PostMapping("/return/{id}")
    public ResponseEntity<BorrowingDTO> returnBook(@PathVariable Long id) {
        return ResponseEntity.ok(borrowingMapper.toDto(borrowingService.returnBook(id)));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BorrowingDTO>> getUserBorrowings(@PathVariable Long userId) {
        List<BorrowingDTO> data = borrowingService.getActiveBorrowings(userId)
            .stream()
            .map(borrowingMapper::toDto)
            .toList();
        return ResponseEntity.ok(data);
    }
}

