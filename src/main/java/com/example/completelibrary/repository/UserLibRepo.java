package com.example.completelibrary.repository;

import com.example.completelibrary.entity.UserLib;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserLibRepo extends JpaRepository<UserLib,Long> {
    Optional<UserLib> findByName(String name);
    Optional<UserLib> findByRole(String role);
}
