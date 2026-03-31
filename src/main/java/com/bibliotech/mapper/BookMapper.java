package com.bibliotech.mapper;

import com.bibliotech.dto.BookDTO;
import com.bibliotech.model.Author;
import com.bibliotech.model.Book;
import com.bibliotech.model.Category;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(source = "author", target = "authorName", qualifiedByName = "authorToName")
    @Mapping(source = "categories", target = "categories", qualifiedByName = "categoriesToLabels")
    BookDTO toDto(Book book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "author", source = "authorName", qualifiedByName = "nameToAuthor")
    @Mapping(source = "categories", target = "categories", qualifiedByName = "labelsToCategories")
    Book toEntity(BookDTO dto);

    @Named("authorToName")
    default String authorToName(Author author) {
        return author == null ? null : author.getName();
    }

    @Named("nameToAuthor")
    default Author nameToAuthor(String authorName) {
        if (authorName == null || authorName.isBlank()) {
            return null;
        }
        Author author = new Author();
        author.setName(authorName.trim());
        return author;
    }

    @Named("categoriesToLabels")
    default Set<String> categoriesToLabels(Set<Category> categories) {
        if (categories == null) {
            return Collections.emptySet();
        }
        return categories.stream().map(Category::getLabel).collect(Collectors.toSet());
    }

    @Named("labelsToCategories")
    default Set<Category> labelsToCategories(Set<String> labels) {
        if (labels == null) {
            return Collections.emptySet();
        }
        Set<Category> categories = new HashSet<>();
        for (String label : labels) {
            if (label != null && !label.isBlank()) {
                Category category = new Category();
                category.setLabel(label.trim());
                categories.add(category);
            }
        }
        return categories;
    }
}

