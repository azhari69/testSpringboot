package com.faisal.testapi.api.repo;

import com.faisal.testapi.api.model.TypeUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TypeUserRepo extends JpaRepository<TypeUser, Integer> {
    @Query("select u from TypeUser u where u.role = ?1 ")
    TypeUser getTypeUserBy(String role);
}
