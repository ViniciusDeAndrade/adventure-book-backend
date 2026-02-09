package com.pictet.adventurebook.api.dto;

import com.pictet.adventurebook.domain.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDto {
    private Long id;
    private String title;
    private String author;
    private Difficulty difficulty;
    private Set<String> categories;
}
