package com.learn.codingtask.controller;

import com.learn.codingtask.dto.*;
import com.learn.codingtask.exception.CustomExceptions;
import com.learn.codingtask.service.EmployeeService;
import com.learn.codingtask.service.JwtService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
@SecurityRequirement(name = "bearerAuth")
public class EmployeeController {

    private final EmployeeService service;
    private final JwtService jwtService;

    public EmployeeController(EmployeeService service,JwtService jwtService) {
        this.service = service;
       this.jwtService = jwtService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EmployeeResponseDTO>> createNewEmployee(@RequestBody @Valid EmployeeDTO inputEmployee) {
        EmployeeResponseDTO savedEmployee = service.saveEmployee(inputEmployee);
        ApiResponse<EmployeeResponseDTO> response = new ApiResponse<>(true, "Employee created successfully", savedEmployee);
        return ResponseEntity.ok(response);

    }
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> login(@RequestBody LoginRequest loginRequest) {
        EmployeeResponseDTO employeeResponse = service.login(loginRequest);
        String token = jwtService.generateToken(employeeResponse);
        Map<String,Object> responseData = new HashMap<>();
        responseData.put("employee", employeeResponse);
        responseData.put("token", token);
        ApiResponse<Map<String, Object>> response = new ApiResponse<>(true, "Hi "+loginRequest.getUserName()+" you are in!", responseData);
        return ResponseEntity.ok(response);

    }

    @PutMapping("/{userName}")
    public ResponseEntity<ApiResponse<EmployeeResponseDTO>> updateEmployee(
            @PathVariable String userName,
            @RequestBody @Valid UpdateEmployeeDTO updatedEmployee,
            @RequestHeader("Authorization") String authorization) {

        // Only Admins can update employee details
        String token = authorization.substring(7);
        jwtService.checkAdmin(token);

        EmployeeResponseDTO updated = service.updateEmployee(userName, updatedEmployee);

        ApiResponse<EmployeeResponseDTO> response = new ApiResponse<>(
                true,
                "Employee '" + userName + "' updated successfully",
                updated
        );

        return ResponseEntity.ok(response);
    }


    @GetMapping
    public ResponseEntity<ApiResponse<List<EmployeeResponseDTO>>> getEmployees(@RequestHeader("Authorization") String authorization) {
        String token = authorization.substring(7);
        jwtService.checkAdmin(token);
        List<EmployeeResponseDTO> employeeList = service.getAllEmployees();
        ApiResponse<List<EmployeeResponseDTO>> response = new ApiResponse<>(true, "Employee List fetched successfully", employeeList);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userName}")
    public ResponseEntity<ApiResponse<EmployeeResponseDTO>> getEmployee(@PathVariable String userName,@RequestHeader("Authorization") String authorization) {
        String token = authorization.substring(7);
        jwtService.checkUserOrAdminForPolicy(token,userName);
        EmployeeResponseDTO employee = service.getEmployeeByUserName(userName);
            ApiResponse<EmployeeResponseDTO> response = new ApiResponse<>(true, "Employee details fetched successfully", employee);
            return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userName}")
    public ResponseEntity<ApiResponse<String>> deleteEmployee(@PathVariable String userName,@RequestHeader("Authorization") String authorization) {
        String token = authorization.substring(7);
        jwtService.checkAdmin(token);
        service.deleteEmployee(userName);
        ApiResponse<String> response = new ApiResponse<>(true, "Employee" + userName + " deleted successfully", userName);
            return ResponseEntity.ok(response);
        }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordDTO request) {
        String message = service.forgotPassword(request.getUserName()); //, request.getEmail()
        return ResponseEntity.ok(message);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDTO request) {
        String response= service.resetPassword(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDTO request){
        String response= service.updatePassword(request);
        return ResponseEntity.ok(response);
    }


}



