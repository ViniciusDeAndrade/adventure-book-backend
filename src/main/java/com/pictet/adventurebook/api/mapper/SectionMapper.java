package com.pictet.adventurebook.api.mapper;

import com.pictet.adventurebook.api.dto.OptionDto;
import com.pictet.adventurebook.api.dto.SectionReadDto;
import com.pictet.adventurebook.domain.Section;
import com.pictet.adventurebook.domain.SectionOption;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = ConsequenceMapper.class)
public interface SectionMapper {

    @Mapping(target = "options", source = "options")
    SectionReadDto toReadDto(Section section);

    @Mapping(target = "consequence", source = "consequence")
    OptionDto toOptionDto(SectionOption option);

    List<OptionDto> toOptionDtos(List<SectionOption> options);
}
