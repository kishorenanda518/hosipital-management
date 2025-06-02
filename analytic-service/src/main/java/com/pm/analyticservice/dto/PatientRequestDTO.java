package com.pm.analyticservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PatientRequestDTO {

    @NotBlank(message = "name is required")
    @Size(max = 100 , message = "The name should be greater than 0")
    private String name;

    @NotBlank(message =  "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "DateOfBirth is required")
    private String dateOfBirth;

    @NotBlank(  message = "Registered date is required")
    private String  registeredDate;

    public @NotBlank(message = "name is required") @Size(max = 100, message = "The name should be greater than 0") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "name is required") @Size(max = 100, message = "The name should be greater than 0") String name) {
        this.name = name;
    }

    public @NotBlank(message = "Email is required") @Email(message = "Email should be valid") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "Email is required") @Email(message = "Email should be valid") String email) {
        this.email = email;
    }

    public @NotBlank(message = "Address is required") String getAddress() {
        return address;
    }

    public void setAddress(@NotBlank(message = "Address is required") String address) {
        this.address = address;
    }

    public @NotBlank(message = "DateOfBirth is required") String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(@NotBlank(message = "DateOfBirth is required") String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public @NotNull(message = "Registered date is required") String getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(@NotNull(message = "Registered date is required") String registeredDate) {
        this.registeredDate = registeredDate;
    }
}
