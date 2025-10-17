package com.learn.codingtask.repository;

import com.learn.codingtask.entity.LeaveDocument;
import com.learn.codingtask.entity.LeaveRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    Optional<LeaveRequest> findById(Long leaveId);


    List<LeaveRequest> findByStartDateGreaterThanEqual(Date date);

    List<LeaveRequest> findByEmployeeUserName(String userName);

    @Query("SELECT l FROM LeaveRequest l WHERE l.startDate >= CURRENT_DATE")
    Page<LeaveRequest> findUpcomingLeaves(Pageable pageable);

    @Query("SELECT l FROM LeaveRequest l WHERE l.employeeUserName = :userName")
    Page<LeaveRequest> findByEmployeeUserName(@Param("userName") String userName, Pageable pageable);



}
