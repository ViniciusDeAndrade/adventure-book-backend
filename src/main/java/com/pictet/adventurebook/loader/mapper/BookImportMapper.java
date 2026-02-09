package com.pictet.adventurebook.loader.mapper;

import com.pictet.adventurebook.domain.Book;
import com.pictet.adventurebook.domain.Section;
import com.pictet.adventurebook.domain.SectionOption;
import com.pictet.adventurebook.loader.dto.BookImportDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = SectionImportMapper.class)
public interface BookImportMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categories", source = "type", qualifiedByName = "typeToCategories")
    @Mapping(target = "sections", source = "sections")
    Book toBook(BookImportDto dto);

    @org.mapstruct.Named("typeToCategories")
    default Set<String> typeToCategories(String type) {
        if (type == null || type.isBlank()) return new HashSet<>();
        return new HashSet<>(Collections.singletonList(type));
    }

    @AfterMapping
    default void setBackReferences(@MappingTarget Book book) {
        if (book.getSections() != null) {
            for (Section section : book.getSections()) {
                section.setBook(book);
                if (section.getOptions() != null) {
                    for (SectionOption option : section.getOptions()) {
                        option.setSection(section);
                    }
                }
            }
        }
    }
}
