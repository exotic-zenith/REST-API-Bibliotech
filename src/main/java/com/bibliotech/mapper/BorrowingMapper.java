package com.bibliotech.mapper;

import com.bibliotech.dto.BorrowingDTO;
import com.bibliotech.model.Borrowing;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BorrowingMapper {

    @Mapping(source = "book.id", target = "bookId")
    BorrowingDTO toDto(Borrowing borrowing);
}

