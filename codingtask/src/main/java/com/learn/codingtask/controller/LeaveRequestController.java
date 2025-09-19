package com.learn.codingtask.controller;

import com.learn.codingtask.dto.*;
import com.learn.codingtask.entity.LeaveRequest;
import com.learn.codingtask.exception.CustomExceptions;
import com.learn.codingtask.service.EmployeeService;
import com.learn.codingtask.service.JwtService;
import com.learn.codingtask.service.LeaveRequestService;
import jakarta.validation.Valid;
import org.apache.tomcat.util.http.parser.Authorization;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/leaves")
public class LeaveRequestController {


    private final LeaveRequestService leaveRequestService;

    private final EmployeeService employeeService;

    private final ModelMapper modelMapper;

    private final JwtService jwtService;

    public LeaveRequestController(LeaveRequestService leaveRequestService, EmployeeService employeeService, ModelMapper modelMapper, JwtService jwtService) {
        this.leaveRequestService = leaveRequestService;
        this.employeeService = employeeService;
        this.modelMapper = modelMapper;
        this.jwtService = jwtService;
    }


    @PostMapping("/apply")
    public ResponseEntity<ApiResponse<LeaveResponseDTO>> applyForLeave(@Valid @RequestBody LeaveRequestDTO dto,
                                                                       @RequestHeader("Authorization") String authorization) throws Exception {
        System.out.println("HEllooooooooooo");
        System.out.println(authorization);

            String token = authorization.substring(7);
            System.out.print("user verified");
            jwtService.checkUser(token, dto.getEmployeeUserName());
                System.out.print("user verified");
                LeaveResponseDTO response = leaveRequestService.applyForLeave(dto);
                return ResponseEntity.ok(new ApiResponse<>(true, "Leave applied successfully", response));
    }



    @GetMapping("/upcoming")
    public ResponseEntity<ApiResponse<List<LeaveResponseDTO>>> getUpcomingLeaveRequests(@RequestHeader("Authorization") String authorization) {
        String token = authorization.substring(7);
        jwtService.checkAdmin(token);
        List<LeaveResponseDTO> leaveRequests = leaveRequestService.getUpcomingLeaveRequests();
        if (!CollectionUtils.isEmpty(leaveRequests)) {
            ApiResponse<List<LeaveResponseDTO>> response = ApiResponse.<List<LeaveResponseDTO>>builder()
                    .success(true)
                    .message("Upcoming leave requests fetched successfully")
                    .data(leaveRequests)
                    .build();
            return ResponseEntity.ok(response);
        } else {
            throw new CustomExceptions.NoExistingLeaveRequestException();
        }
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<ApiResponse<List<LeaveResponseDTO>>> getLeaveRequestsByUser(@PathVariable String username,
                                                                                      @RequestHeader("Authorization") String authorization) {
        String token = authorization.substring(7);
        jwtService.checkUserOrAdminForPolicy(token,username);
        List<LeaveResponseDTO> leaveRequests = leaveRequestService.getLeaveRequestsByUser(username);
        if (!CollectionUtils.isEmpty(leaveRequests)) {
            ApiResponse<List<LeaveResponseDTO>> response = ApiResponse.<List<LeaveResponseDTO>>builder()
                    .success(true)
                    .message("Leave requests for user: " + username + " fetched successfully")
                    .data(leaveRequests)
                    .build();

            return ResponseEntity.ok(response);
        } else {
            throw new CustomExceptions.NoLeaveRequestForEmployeeException(username);
        }
    }

    @PutMapping("/{leaveId}/decision")
    public ResponseEntity<ApiResponse<LeaveResponseDTO>> decideLeave(
            @PathVariable Long leaveId,
            @RequestParam String adminUserName,
            @RequestParam String status,
            @RequestHeader("Authorization") String authorization) throws Exception {
        String token = authorization.substring(7);
        jwtService.checkAdmin(token);

        LeaveResponseDTO updatedLeave = leaveRequestService.decideLeave(leaveId, adminUserName, status);

        ApiResponse<LeaveResponseDTO> response = ApiResponse.<LeaveResponseDTO>builder()
                .success(true)
                .message("Leave request " + status + " successfully")
                .data(updatedLeave)
                .build();

        return ResponseEntity.ok(response);
    }


}