package com.pictet.adventurebook.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChoiceRequestDto {
    @NotNull
    private Integer gotoSectionId;
}
