package com.learn.codingtask.dto;

import lombok.*;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {
    private String userName;
    private String password;
}
