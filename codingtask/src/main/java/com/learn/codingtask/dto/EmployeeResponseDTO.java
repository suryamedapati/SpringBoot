package com.learn.codingtask.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDTO {

    String userName;
    String firstName;
    String lastName;
    String email;
    String phoneNumber;
    String role;
    boolean isActive;

}
