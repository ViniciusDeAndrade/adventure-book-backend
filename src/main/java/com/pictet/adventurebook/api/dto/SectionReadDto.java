package com.pictet.adventurebook.api.dto;

import com.pictet.adventurebook.domain.SectionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SectionReadDto {
    private Long id;
    private Integer sectionId;
    private String text;
    private SectionType type;
    private List<OptionDto> options;
}
