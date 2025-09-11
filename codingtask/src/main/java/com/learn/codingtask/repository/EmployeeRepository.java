package com.learn.codingtask.repository;


import com.learn.codingtask.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Employee findByUserName(String userName);
    void  deleteByUserName(String userName);
}

