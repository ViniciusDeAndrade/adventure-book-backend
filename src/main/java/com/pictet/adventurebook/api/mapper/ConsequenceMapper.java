package com.pictet.adventurebook.api.mapper;

import com.pictet.adventurebook.api.dto.ConsequenceDto;
import com.pictet.adventurebook.domain.Consequence;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ConsequenceMapper {

    ConsequenceDto toDto(Consequence consequence);
}
