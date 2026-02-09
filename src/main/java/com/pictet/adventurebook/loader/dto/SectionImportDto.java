package com.pictet.adventurebook.loader.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pictet.adventurebook.domain.SectionType;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SectionImportDto {
    private Integer id;
    private String text;
    private SectionType type;
    private List<OptionImportDto> options;

    public boolean hasOptions() {
        return options != null && !options.isEmpty();
    }
}
