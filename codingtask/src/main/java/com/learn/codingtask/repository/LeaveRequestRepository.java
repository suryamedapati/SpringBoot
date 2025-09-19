package com.learn.codingtask.repository;

import com.learn.codingtask.entity.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    List<LeaveRequest> findByStartDateGreaterThanEqual(Date date);

    List<LeaveRequest> findByEmployeeUserName(String userName);
}
