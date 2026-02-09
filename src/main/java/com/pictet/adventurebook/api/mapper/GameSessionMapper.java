package com.pictet.adventurebook.api.mapper;

import com.pictet.adventurebook.api.dto.GameSessionDto;
import com.pictet.adventurebook.domain.GameSession;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GameSessionMapper {

    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "currentSectionId", source = "currentSection.sectionId")
    GameSessionDto toDto(GameSession session);
}
