package com.learn.codingtask.service;
import com.learn.codingtask.dto.LeaveRequestDTO;
import com.learn.codingtask.dto.LeaveResponseDTO;
import com.learn.codingtask.entity.Employee;
import com.learn.codingtask.entity.LeaveRequest;
import com.learn.codingtask.exception.CustomExceptions;
import com.learn.codingtask.repository.EmployeeRepository;
import com.learn.codingtask.repository.LeaveRequestRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;
    private final MailService mailService;
    //private final SecurityConfig securityConfig;

    public LeaveRequestService(LeaveRequestRepository leaveRequestRepository,
                               EmployeeRepository employeeRepository,
                               ModelMapper modelMapper,
                               MailService mailService) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
        this.mailService = mailService;
        //this.securityConfig = securityConfig;
    }

    public LeaveResponseDTO applyForLeave(LeaveRequestDTO dto) throws Exception {
        Employee employee = employeeRepository.findByUserName(dto.getEmployeeUserName());
        if (employee == null) {
            throw new CustomExceptions.EmployeeNotFoundException("Employee not found with username: " + dto.getEmployeeUserName());
        }

        LeaveRequest leaveRequest = modelMapper.map(dto, LeaveRequest.class);
        leaveRequest.setEmployeeUserName(employee.getUserName());
        leaveRequest.setStatus("PENDING");

        LeaveRequest saved = leaveRequestRepository.save(leaveRequest);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        mailService.sendLeaveAppliedMail(saved.getId(), employee.getUserName(),
                sdf.format(dto.getStartDate()),
                sdf.format(dto.getEndDate()),
                dto.getReason());

        return modelMapper.map(saved, LeaveResponseDTO.class);
    }

    public List<LeaveResponseDTO> getUpcomingLeaveRequests() {

        Date today = new Date();
        List<LeaveRequest> requests = leaveRequestRepository.findByStartDateGreaterThanEqual(today);
        return requests.stream()
                .map(req -> modelMapper.map(req, LeaveResponseDTO.class))
                .toList();
    }
    public Page<LeaveResponseDTO> getUpcomingLeaveRequests(Pageable pageable){
        Page leaveRequests = leaveRequestRepository.findUpcomingLeaves(pageable);
        Page<LeaveResponseDTO> leaveResponsePage = leaveRequests.map(
                leaveRequest -> modelMapper.map(leaveRequest, LeaveResponseDTO.class));
        return leaveResponsePage;
    }
   /* public Page<LeaveResponseDTO> getUpcomingLeaveRequests(Pageable pageable) {
        return leaveRequestRepository.findByDateAfter(LocalDate.now(), pageable)
                .map(leave ->modelMapper.map(leave, LeaveResponseDTO.class));
                        *//*LeaveResponseDTO.builder()
                        .id(leave.getId())
                        .employeeName(leave.getEmployee().getFirstName() + " " + leave.getEmployee().getLastName())
                        .startDate(leave.getStartDate())
                        .endDate(leave.getEndDate())
                        .status(leave.getStatus())
                        .build());*//*
    }*/

    public List<LeaveResponseDTO> getLeaveRequestsByUser(String username) {
        List<LeaveRequest> requests = leaveRequestRepository.findByEmployeeUserName(username);
        return requests.stream()
                .map(req -> modelMapper.map(req, LeaveResponseDTO.class))
                .toList();
    }

    public Page<LeaveResponseDTO> getLeaveRequestsByUser(String username , Pageable pageable){

        Page leaveRequests =  leaveRequestRepository.findByEmployeeUserName(username, pageable);
        Page<LeaveResponseDTO> leaveResponsePage = leaveRequests.map(
                leaveRequest -> modelMapper.map(leaveRequest, LeaveResponseDTO.class));
        return leaveResponsePage;
    }


    public LeaveResponseDTO decideLeave(Long leaveId, String adminUserName, String status) throws Exception {
        //  Check if admin exists
        Employee admin = employeeRepository.findByUserName(adminUserName);
        if (admin == null) {
            throw new Exception("Employee not found with username: " + adminUserName);
        }
        if (!"ADMIN".equalsIgnoreCase(admin.getRole())) {
            throw new RuntimeException("Only Admins can approve/reject leave requests");
        }

        // Check if leave exists
        LeaveRequest leave = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new CustomExceptions.LeaveNotFoundException(leaveId));

        // Validate status
        if (!status.equalsIgnoreCase("approved") && !status.equalsIgnoreCase("rejected")) {
            throw new CustomExceptions.InvalidStatusException(status);
        }
        // Update leave
        leave.setStatus(status);
        LeaveRequest updatedleave = leaveRequestRepository.save(leave);
        Employee user = employeeRepository.findByUserName(updatedleave.getEmployeeUserName());
        mailService.sendLeaveDecisionMail(user,updatedleave);
        return modelMapper.map(updatedleave, LeaveResponseDTO.class);
    }
}



