package com.example.completelibrary.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title can not be blank")
    private String title;

    @NotBlank(message = "Please enter the author name")
    private String author;

    @NotBlank(message = "ISBN can not be blank")
    private String isbn;

    @Min(value = 0, message = "Available copies can not be negative")
    private int availableCopies;
}
