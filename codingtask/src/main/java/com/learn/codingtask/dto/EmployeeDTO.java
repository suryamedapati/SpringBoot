package com.learn.codingtask.dto;

import jakarta.validation.constraints.*;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmployeeDTO {

    @NotBlank(message = "Username is required")
    private String userName; // Unique check will be done in service layer

    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
            message = "Password must contain at least one lowercase letter, one uppercase letter, and one number"
    )
    private String password;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^\\d{10}$",
            message = "Phone number must be 10 digits"
    )
    private String phoneNumber;

    @NotBlank(message = "Role is required")
    @Pattern(
            regexp = "^(ADMIN|USER)$",
            flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Role must be either ADMIN or USER"
    )
    private String role;

    private boolean isActive;
}

