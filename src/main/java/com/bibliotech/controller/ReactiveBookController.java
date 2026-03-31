package com.bibliotech.controller;

import com.bibliotech.mongo.BookDocument;
import com.bibliotech.mongo.BookDocumentRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/reactive/books")
@RequiredArgsConstructor
@Profile("mongo")
public class ReactiveBookController {

    private final BookDocumentRepository bookDocumentRepository;

    @GetMapping
    public Flux<BookDocument> getAll() {
        return bookDocumentRepository.findAll();
    }

    @GetMapping("/{isbn}")
    public Mono<BookDocument> getByIsbn(@PathVariable String isbn) {
        return bookDocumentRepository.findByIsbn(isbn);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<BookDocument> create(@Valid @RequestBody BookDocument document) {
        return bookDocumentRepository.save(document);
    }
}

