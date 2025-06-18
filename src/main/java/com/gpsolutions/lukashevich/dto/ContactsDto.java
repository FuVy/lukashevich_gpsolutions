package com.gpsolutions.lukashevich.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class ContactsDto {
    @NotNull(message = "Phone can't be null.")
    @NotEmpty(message = "Phone can't be empty.")
    @NotBlank(message = "Phone can't be empty.")
    String phone;
    @NotNull(message = "Email can't be null.")
    @Email
    @NotEmpty(message = "Email can't be empty.")
    @NotBlank(message = "Email can't be empty.")
    String email;
}