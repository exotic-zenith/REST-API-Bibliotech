package com.bibliotech.service;

import com.bibliotech.model.Author;
import com.bibliotech.model.Book;
import com.bibliotech.model.Category;
import com.bibliotech.repository.AuthorRepository;
import com.bibliotech.repository.BookRepository;
import com.bibliotech.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;

    public Page<Book> findAvailableBooks(Pageable pageable) {
        return bookRepository.findByStockDisponibleGreaterThan(0, pageable);
    }

    public Book findById(Long id) {
        return bookRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Livre non trouve: " + id));
    }

    @Transactional
    public Book save(Book book) {
        bookRepository.findByIsbn(book.getIsbn())
            .ifPresent(existing -> {
                throw new IllegalStateException("ISBN deja existant.");
            });

        if (book.getAuthor() != null && book.getAuthor().getName() != null) {
            Author persistedAuthor = authorRepository.save(book.getAuthor());
            book.setAuthor(persistedAuthor);
        }

        Set<Category> persistedCategories = new HashSet<>();
        if (book.getCategories() != null) {
            for (Category c : book.getCategories()) {
                if (c.getLabel() != null && !c.getLabel().isBlank()) {
                    persistedCategories.add(categoryRepository.save(c));
                }
            }
        }
        book.setCategories(persistedCategories);

        return bookRepository.save(book);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Livre non trouve: " + id);
        }
        bookRepository.deleteById(id);
    }
}

