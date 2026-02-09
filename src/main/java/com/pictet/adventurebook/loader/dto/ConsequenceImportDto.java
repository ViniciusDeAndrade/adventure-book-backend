package com.pictet.adventurebook.loader.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pictet.adventurebook.domain.ConsequenceType;
import com.pictet.adventurebook.loader.IntOrStringDeserializer;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConsequenceImportDto {
    private ConsequenceType type;
    @JsonDeserialize(using = IntOrStringDeserializer.class)
    private Integer value;
    private String text;
}
