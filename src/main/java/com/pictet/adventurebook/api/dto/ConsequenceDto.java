package com.pictet.adventurebook.api.dto;

import com.pictet.adventurebook.domain.ConsequenceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsequenceDto {
    private ConsequenceType type;
    private int value;
    private String text;
}
