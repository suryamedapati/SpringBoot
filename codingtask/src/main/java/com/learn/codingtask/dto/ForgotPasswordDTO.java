package com.learn.codingtask.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ForgotPasswordDTO {
    private String userName;
    private String email;
}
