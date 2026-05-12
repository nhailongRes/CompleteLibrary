package com.example.completelibrary.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendRequest {
    @NotBlank
    private String genre;
    @NotBlank
    private String mood;
}
