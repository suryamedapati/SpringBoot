package com.learn.codingtask.controller;

import com.learn.codingtask.dto.ApiResponse;
import com.learn.codingtask.dto.EmployeeDTO;
import com.learn.codingtask.exception.CustomExceptions;
import com.learn.codingtask.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EmployeeDTO>> createNewEmployee(@RequestBody @Valid EmployeeDTO inputEmployee) {
        EmployeeDTO savedEmployee = service.saveEmployee(inputEmployee);
        ApiResponse<EmployeeDTO> response = new ApiResponse<>(true, "Employee created successfully", savedEmployee);
        return ResponseEntity.ok(response);

    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EmployeeDTO>>> getEmployees() {
        List<EmployeeDTO> employeeList = service.getAllEmployees();
        ApiResponse<List<EmployeeDTO>> response = new ApiResponse<>(true, "Employee List fetched successfully", employeeList);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userName}")
    public ResponseEntity<ApiResponse<EmployeeDTO>> getEmployee(@PathVariable String userName) {
        EmployeeDTO employee = service.getEmployeeByUserName(userName);
        if (employee != null) {
            ApiResponse<EmployeeDTO> response = new ApiResponse<>(true, "Employee details fetched successfully", employee);
            return ResponseEntity.ok(response);
        }
        throw new CustomExceptions.EmployeeNotFoundException(userName);
    }

    @DeleteMapping("/{userName}")
    public ResponseEntity<ApiResponse<String>> deleteEmployee(@PathVariable String userName) {
        EmployeeDTO employee = service.getEmployeeByUserName(userName);
        if (employee != null) {
            ApiResponse<String> response = new ApiResponse<>(true, "Employee" + userName + " deleted successfully", userName);
            service.deleteEmployee(userName);
            return ResponseEntity.ok(response);
        }
        throw new CustomExceptions.EmployeeNotFoundException(userName);
    }
}



