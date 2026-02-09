package com.pictet.adventurebook.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameSessionDto {
    private Long id;
    private Long bookId;
    private Integer currentSectionId;
    private int health;
}
