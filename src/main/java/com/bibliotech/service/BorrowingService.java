package com.bibliotech.service;

import com.bibliotech.model.Book;
import com.bibliotech.model.Borrowing;
import com.bibliotech.model.BorrowingStatus;
import com.bibliotech.repository.BookRepository;
import com.bibliotech.repository.BorrowingRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BorrowingService {

    private final BorrowingRepository borrowingRepository;
    private final BookRepository bookRepository;

    @Transactional
    public Borrowing processBorrowing(Long bookId, Long userId) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new EntityNotFoundException("Livre non trouve: " + bookId));

        if (book.getStockDisponible() <= 0) {
            throw new IllegalStateException("Aucun exemplaire disponible pour ce livre.");
        }

        long activeCount = borrowingRepository.countByUserIdAndStatus(userId, BorrowingStatus.ONGOING);
        if (activeCount >= 3) {
            throw new IllegalStateException("Limite de 3 emprunts simultanes atteinte.");
        }

        book.setStockDisponible(book.getStockDisponible() - 1);
        bookRepository.save(book);

        Borrowing borrowing = new Borrowing();
        borrowing.setBook(book);
        borrowing.setUserId(userId);
        borrowing.setStatus(BorrowingStatus.ONGOING);
        return borrowingRepository.save(borrowing);
    }

    @Transactional
    public Borrowing returnBook(Long borrowingId) {
        Borrowing borrowing = borrowingRepository.findById(borrowingId)
            .orElseThrow(() -> new EntityNotFoundException("Emprunt non trouve: " + borrowingId));

        if (borrowing.getStatus() != BorrowingStatus.ONGOING && borrowing.getStatus() != BorrowingStatus.OVERDUE) {
            throw new IllegalStateException("Cet emprunt est deja retourne.");
        }

        borrowing.setStatus(BorrowingStatus.RETURNED);
        borrowing.setReturnDate(LocalDate.now());

        Book book = borrowing.getBook();
        book.setStockDisponible(book.getStockDisponible() + 1);
        bookRepository.save(book);

        return borrowingRepository.save(borrowing);
    }

    public List<Borrowing> getActiveBorrowings(Long userId) {
        return borrowingRepository.findByUserIdAndStatus(userId, BorrowingStatus.ONGOING);
    }
}

