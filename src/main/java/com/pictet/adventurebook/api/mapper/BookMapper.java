package com.pictet.adventurebook.api.mapper;

import com.pictet.adventurebook.api.dto.BookDetailDto;
import com.pictet.adventurebook.api.dto.BookDto;
import com.pictet.adventurebook.domain.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookMapper {

    BookDto toListDto(Book book);

    BookDetailDto toDetailDto(Book book);
}
