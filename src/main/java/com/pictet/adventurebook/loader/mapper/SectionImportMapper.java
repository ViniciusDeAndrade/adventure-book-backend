package com.pictet.adventurebook.loader.mapper;

import com.pictet.adventurebook.domain.Section;
import com.pictet.adventurebook.loader.dto.SectionImportDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = OptionImportMapper.class)
public interface SectionImportMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "book", ignore = true)
    @Mapping(target = "sectionId", source = "id")
    @Mapping(target = "options", source = "options", defaultExpression = "java(new java.util.ArrayList<>())")
    Section toSection(SectionImportDto dto);
}
