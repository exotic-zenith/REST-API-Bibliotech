package com.bibliotech.repository;

import com.bibliotech.model.Borrowing;
import com.bibliotech.model.BorrowingStatus;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowingRepository extends JpaRepository<Borrowing, Long> {

    List<Borrowing> findByUserIdAndStatus(Long userId, BorrowingStatus status);

    long countByUserIdAndStatus(Long userId, BorrowingStatus status);

    List<Borrowing> findByStatusAndBorrowDateBefore(BorrowingStatus status, LocalDate date);
}

