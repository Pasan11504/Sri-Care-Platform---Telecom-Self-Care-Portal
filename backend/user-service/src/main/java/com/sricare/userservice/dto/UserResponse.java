package com.sricare.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String phoneNumber;
    private String email;
    private String firstName;
    private String lastName;
    private String accountStatus;
}
