package com.pictet.adventurebook.loader.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OptionImportDto {
    private String description;
    private Integer gotoId;
    private ConsequenceImportDto consequence;

    public boolean hasConsequence() {
        return consequence != null;
    }
}
