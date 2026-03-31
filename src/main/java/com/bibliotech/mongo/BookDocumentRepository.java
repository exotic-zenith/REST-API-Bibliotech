package com.bibliotech.mongo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface BookDocumentRepository extends ReactiveMongoRepository<BookDocument, String> {

    Mono<BookDocument> findByIsbn(String isbn);
}

