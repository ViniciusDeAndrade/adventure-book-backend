package com.pictet.adventurebook.api.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriesUpdateDto {
    @NotEmpty
    private Set<@NotBlank String> categories;
}
