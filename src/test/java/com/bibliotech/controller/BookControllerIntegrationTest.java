package com.bibliotech.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bibliotech.dto.BookDTO;
import com.bibliotech.model.Book;
import com.bibliotech.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void createBookValidPayloadReturns201() throws Exception {
        BookDTO dto = new BookDTO(null, "978-2-07-036024-5", "Le Petit Prince", 3, null, null);

        mockMvc.perform(post("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.isbn").value("978-2-07-036024-5"));

        assertTrue(bookRepository.findByIsbn("978-2-07-036024-5").isPresent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createBookDuplicateIsbnReturns400() throws Exception {
        Book existing = new Book();
        existing.setIsbn("978-2-07-036024-5");
        existing.setTitle("Existant");
        existing.setStockDisponible(1);
        bookRepository.save(existing);

        BookDTO dto = new BookDTO(null, "978-2-07-036024-5", "Doublon", 2, null, null);

        mockMvc.perform(post("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest());
    }
}

