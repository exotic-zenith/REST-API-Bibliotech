package com.bibliotech.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bibliotech.model.Book;
import com.bibliotech.model.Borrowing;
import com.bibliotech.model.BorrowingStatus;
import com.bibliotech.repository.BookRepository;
import com.bibliotech.repository.BorrowingRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BorrowingServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BorrowingRepository borrowingRepository;

    @InjectMocks
    private BorrowingService borrowingService;

    @Test
    void processBorrowingSuccess() {
        Book book = new Book();
        book.setId(1L);
        book.setStockDisponible(5);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(borrowingRepository.countByUserIdAndStatus(1L, BorrowingStatus.ONGOING)).thenReturn(1L);
        when(bookRepository.save(any(Book.class))).thenAnswer(inv -> inv.getArgument(0));
        when(borrowingRepository.save(any(Borrowing.class))).thenAnswer(inv -> inv.getArgument(0));

        Borrowing result = borrowingService.processBorrowing(1L, 1L);

        assertNotNull(result);
        assertEquals(BorrowingStatus.ONGOING, result.getStatus());
        assertEquals(4, book.getStockDisponible());
        verify(borrowingRepository, times(1)).save(any(Borrowing.class));
    }

    @Test
    void processBorrowingOutOfStockThrowsException() {
        Book book = new Book();
        book.setStockDisponible(0);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        IllegalStateException ex = assertThrows(IllegalStateException.class,
            () -> borrowingService.processBorrowing(1L, 1L));

        assertEquals("Aucun exemplaire disponible pour ce livre.", ex.getMessage());
        verify(borrowingRepository, never()).save(any(Borrowing.class));
    }
}

