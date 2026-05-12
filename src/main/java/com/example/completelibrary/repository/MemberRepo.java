package com.example.completelibrary.repository;

import com.example.completelibrary.entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepo extends JpaRepository<Member, Long> {
    Optional<Member> findMemberByEmail(String email);
    boolean existsByEmail(@NotBlank(message = "Email can not be blank") @Email(message = "Please enter the right format") String email);
}
