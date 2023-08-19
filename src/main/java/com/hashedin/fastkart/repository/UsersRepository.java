package com.hashedin.fastkart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hashedin.fastkart.model.Users;

public interface UsersRepository extends JpaRepository<Users, Integer> {


    @Query(value = "SELECT * FROM users a WHERE a.username = ?1", nativeQuery = true)
    Users findByUsername(String username);
}
