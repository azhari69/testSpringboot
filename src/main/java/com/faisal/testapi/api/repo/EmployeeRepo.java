package com.faisal.testapi.api.repo;

import com.faisal.testapi.api.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmployeeRepo extends JpaRepository<Employee, Integer> {

    @Query("select u from Employee u where u.id = ?1 ")
    Employee getUserById(Integer id);

    @Query("delete from Employee u where u.id = ?1")
    void deleteUserById(Integer id);

}
