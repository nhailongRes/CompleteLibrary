package com.example.completelibrary.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminRegister {
    @NotNull(message = "Username cannot be null")
    @Size(min = 3, message = "Username must be more than 3 characters")
    @Pattern(regexp = "^\\S+$", message = "Username cannot contain spaces")
    private String adminUsername;
    @NotNull(message = "Password cannot be null")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[A-Z]).{6,}$",
            message = "Password must be at least 6 characters long and contain at least one number and one uppercase letter"
    )
    private String adminPassword;
    @NotNull(message = "Username cannot be null")
    @Size(min = 3, message = "Username must be more than 3 characters")
    @Pattern(regexp = "^\\S+$", message = "Username cannot contain spaces")
    private String newAdminUsername;
    @NotNull(message = "Password cannot be null")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[A-Z]).{6,}$",
            message = "Password must be at least 6 characters long and contain at least one number and one uppercase letter"
    )
    private String newAdminPassword;
}
