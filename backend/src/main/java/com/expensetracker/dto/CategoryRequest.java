package com.expensetracker.dto;
import jakarta.validation.constraints.*;

public class CategoryRequest {
    @NotBlank(message = "Category name is required")
    @Size(max = 100, message = "Name too long")
    private String name;
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
