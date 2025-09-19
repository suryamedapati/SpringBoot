package com.learn.codingtask.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.learn.codingtask.entity.Employee;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveRequestDTO {

    //private Long id;  // For response only, not required on create

    private String employeeUserName; // Populated via ModelMapper for response
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be today or in the future")
    private Date startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "End date is required")
    @FutureOrPresent(message = "End date must be today or in the future")
    private Date endDate;

    @NotBlank(message = "Reason is required")
    @Size(max = 500, message = "Reason cannot exceed 500 characters")
    private String reason;

}

