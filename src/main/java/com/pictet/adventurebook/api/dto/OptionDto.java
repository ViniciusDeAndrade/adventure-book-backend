package com.pictet.adventurebook.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OptionDto {
    private Long id;
    private String description;
    private Integer gotoSectionId;
    private ConsequenceDto consequence;
}
