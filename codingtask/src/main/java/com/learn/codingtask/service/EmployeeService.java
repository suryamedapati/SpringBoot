package com.learn.codingtask.service;
import com.learn.codingtask.dto.EmployeeDTO;
import com.learn.codingtask.entity.Employee;
import com.learn.codingtask.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {



        private final EmployeeRepository repository;
        public final ModelMapper modelMapper;

        public EmployeeService(EmployeeRepository repository, ModelMapper modelMapper) {
            this.repository = repository;
            this.modelMapper = modelMapper;
        }

  /*  public EmployeeDTO createNewEmployee(EmployeeDTO inputEmployee) {
//        to check if user is admin
//        log something
        EmployeeEntity toSaveEntity = modelMapper.map(inputEmployee, EmployeeEntity.class);
        EmployeeEntity savedEmployeeEntity = employeeRepository.save(toSaveEntity);
        return modelMapper.map(savedEmployeeEntity, EmployeeDTO.class);*/

    public EmployeeDTO saveEmployee(EmployeeDTO employee) {
            Employee savedEmployee = modelMapper.map(employee,Employee.class);
            Employee savedEmployeeEntity = repository.save(savedEmployee);
            return modelMapper.map(savedEmployeeEntity,EmployeeDTO.class);
        }

        public List<EmployeeDTO> getAllEmployees() {
        List<EmployeeDTO> employeesList =  repository.findAll().stream()
                .map(emp -> modelMapper.map(emp,EmployeeDTO.class))
                .collect(Collectors.toList());

            return employeesList;
        }


        public EmployeeDTO getEmployeeByUserName(String username) {
            EmployeeDTO employee =modelMapper.map(repository.findByUserName(username),EmployeeDTO.class);
            return employee;
        }

        public void deleteEmployee(String username) {
        repository.deleteByUserName(username);
        }
}


