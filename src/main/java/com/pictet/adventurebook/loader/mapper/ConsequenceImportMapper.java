package com.pictet.adventurebook.loader.mapper;

import com.pictet.adventurebook.domain.Consequence;
import com.pictet.adventurebook.loader.dto.ConsequenceImportDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ConsequenceImportMapper {

    @Mapping(target = "value", source = "value", qualifiedByName = "valueOrZero")
    Consequence toConsequence(ConsequenceImportDto dto);

    @Named("valueOrZero")
    default int valueOrZero(Integer v) {
        return v != null ? v : 0;
    }
}
