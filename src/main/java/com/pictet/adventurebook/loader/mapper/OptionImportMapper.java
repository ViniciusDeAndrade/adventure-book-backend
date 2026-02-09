package com.pictet.adventurebook.loader.mapper;

import com.pictet.adventurebook.domain.SectionOption;
import com.pictet.adventurebook.loader.dto.OptionImportDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = ConsequenceImportMapper.class)
public interface OptionImportMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "section", ignore = true)
    @Mapping(target = "gotoSectionId", source = "gotoId")
    @Mapping(target = "consequence", source = "consequence")
    SectionOption toSectionOption(OptionImportDto dto);
}
