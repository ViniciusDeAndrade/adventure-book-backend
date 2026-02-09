package com.pictet.adventurebook.loader.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pictet.adventurebook.domain.Difficulty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookImportDto {
    private String title;
    private String author;
    private Difficulty difficulty;
    private String type;
    private List<SectionImportDto> sections;

}
