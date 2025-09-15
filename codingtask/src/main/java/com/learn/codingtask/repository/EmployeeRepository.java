package com.learn.codingtask.repository;


import com.learn.codingtask.entity.Employee;
import com.learn.codingtask.entity.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Employee findByUserName(String userName);

    void deleteByUserName(String userName);

    boolean existsByUserName(String userName);

    boolean existsByEmail(String email);
}

