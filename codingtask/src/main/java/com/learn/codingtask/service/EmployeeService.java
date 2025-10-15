package com.learn.codingtask.service;

import com.learn.codingtask.dto.*;
import com.learn.codingtask.entity.Employee;
import com.learn.codingtask.exception.CustomExceptions;
import com.learn.codingtask.repository.EmployeeRepository;

import com.learn.codingtask.security.SecurityConfig;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class EmployeeService {


    private final EmployeeRepository repository;
    public final ModelMapper modelMapper;
    public final PasswordEncoder passwordEncoder;
    public final MailService mailService;
    private final Map<String, PasswordResetOtp> tokenStore = new HashMap<>();



    public EmployeeService(EmployeeRepository repository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, MailService mailService) {
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }

    public EmployeeResponseDTO saveEmployee(EmployeeDTO employee)
    {
        if (repository.existsByUserName(employee.getUserName())) {
            throw new CustomExceptions.UsernameAlreadyExistsException(employee.getUserName());
        }
        if(repository.existsByEmail(employee.getEmail())){
            throw new CustomExceptions.EmailAlreadyExistsException(employee.getEmail());
        }
        employee.setActive(true);
        //employee.setPassword(passwordEncoder(employee.getPassword()).toString();
        Employee savedEmployee = modelMapper.map(employee, Employee.class);
        savedEmployee.setIsActive(true);
        savedEmployee.setPassword(passwordEncoder.encode(employee.getPassword()));
        Employee savedEmployeeEntity = repository.save(savedEmployee);
        mailService.userCreatedMail(savedEmployee);
        return modelMapper.map(savedEmployeeEntity, EmployeeResponseDTO.class);

    }
    public EmployeeResponseDTO login(LoginRequest loginRequest) {
        System.out.println(loginRequest.getUserName());
        Employee employee = repository.findByUserName(loginRequest.getUserName());
        //String password = passwordEncoder.encode(loginRequest.getPassword());
        //System.out.println(password);
        if(employee==null||!employee.getIsActive()){
            throw new CustomExceptions.InactiveProfileException("Your profile is inactive,please contact admin");
        }
        if (employee==null || !passwordEncoder.matches(loginRequest.getPassword(),employee.getPassword())) {
            throw new CustomExceptions.InvalidCredentialsException("Invalid credentials!!! Please try again.");
        }
        EmployeeResponseDTO response = modelMapper.map(employee,EmployeeResponseDTO.class);
        return response;
    }
    public EmployeeResponseDTO updateEmployee(String userName, UpdateEmployeeDTO updatedEmployee) {
        Employee existing = repository.findByUserName(userName);
         if(existing == null){
             throw new CustomExceptions.EmployeeNotFoundException(userName);
         }
        // ✅ Update allowed fields
        existing.setFirstName(updatedEmployee.getFirstName());
        existing.setLastName(updatedEmployee.getLastName());
        existing.setEmail(updatedEmployee.getEmail());
        existing.setPhoneNumber(updatedEmployee.getPhoneNumber());
        existing.setRole(updatedEmployee.getRole());
        existing.setIsActive(updatedEmployee.getIsActive());

        // ✅ If password provided, update with encryption
       /* if (updatedEmployee.getPassword() != null && !updatedEmployee.getPassword().isEmpty()) {
            existing.setPassword(passwordEncoder.encode(updatedEmployee.getPassword()));
        }*/

        Employee saved = repository.save(existing);

        return modelMapper.map(saved, EmployeeResponseDTO.class);
    }






    public List<EmployeeResponseDTO> getAllEmployees() {
        List<EmployeeResponseDTO> employeesList = repository.findAll().stream()
                .map(emp -> modelMapper.map(emp, EmployeeResponseDTO.class))
                .collect(Collectors.toList());

        return employeesList;
    }


    public EmployeeResponseDTO getEmployeeByUserName(String username) {
        if (repository.existsByUserName(username)){
        EmployeeResponseDTO employee = modelMapper.map(repository.findByUserName(username), EmployeeResponseDTO.class);

        return employee;
        }
        else
            throw new CustomExceptions.EmployeeNotFoundException(username);
    }

    @Transactional
    public String deleteEmployee(String username) {
        if (repository.existsByUserName(username)){
        repository.deleteByUserName(username);
        repository.flush();
        return username;
        }
        else {
            throw new CustomExceptions.EmployeeNotFoundException(username);
        }
    }
    public String forgotPassword(String userName) {
        Employee employee = repository.findByUserName(userName);

        if (employee == null) {
            throw new CustomExceptions.EmployeeNotFoundException("Employee not found");
        }
        String otp = String.format("%06d", new Random().nextInt(999999));
        tokenStore.put(otp, new PasswordResetOtp(userName, LocalDateTime.now().plusMinutes(10)));
        // Send email safely
        mailService.passwordResetMail(employee,otp);
        return "Reset OTP sent successfully to the following email: "+ employee.getEmail();
    }

    // Step 2: Reset password with token
    public String resetPassword(ResetPasswordDTO resetPasswordRequest) {
        PasswordResetOtp resetOtp = tokenStore.get(resetPasswordRequest.getOtp());
        if (resetOtp == null || resetOtp.getExpiry().isBefore(LocalDateTime.now())) {
            throw new CustomExceptions.UnauthorizedException("Invalid or expired OTP");
        }

        // Validate password strength
        //validationService.validatePassword(resetPasswordRequest.getNewPassword());
        if(!resetPasswordRequest.getNewPassword().equals(resetPasswordRequest.getConfirmNewPassword())) {
            throw new CustomExceptions.UnauthorizedException("Passwords do not match");
        }
        Employee employee = repository.findByUserName(resetOtp.getUserName());
        if (employee == null) {
            throw new CustomExceptions.EmployeeNotFoundException("Employee not found");
        }

        System.out.println("new password:"+resetPasswordRequest.getNewPassword());
        System.out.println("confirm new password:"+resetPasswordRequest.getNewPassword());
        System.out.println("encoded"+passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
        // Encode new password
        String updatedPassword=passwordEncoder.encode(resetPasswordRequest.getNewPassword());
        employee.setPassword(updatedPassword);
        System.out.println(employee.getPassword());
        repository.save(employee);

        // Remove token after use
        tokenStore.remove(resetPasswordRequest.getOtp());

        return "Password reset done successfully";
    }

    //updated Password /change password
    public String updatePassword(ChangePasswordDTO changePasswordRequest) {
        Employee employee = repository.findByUserName(changePasswordRequest.getUserName());
        if (employee == null || !passwordEncoder.matches(changePasswordRequest.getOldPassword(), employee.getPassword())) {
            throw new CustomExceptions.UnauthorizedException("Invalid old password credentials!!! Please try again.");
        }
        if(!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmNewPassword())) {
            throw new CustomExceptions.UnauthorizedException("Passwords do not match");
        }
        employee.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        repository.save(employee);
        return "Password updated successfully";
    }
    private static class PasswordResetOtp {
        private String userName;
        private LocalDateTime expiry;

        public PasswordResetOtp(String userName, LocalDateTime expiry) {
            this.userName = userName;
            this.expiry = expiry;
        }

        public String getUserName() { return userName; }
        public LocalDateTime getExpiry() { return expiry; }
    }
}



