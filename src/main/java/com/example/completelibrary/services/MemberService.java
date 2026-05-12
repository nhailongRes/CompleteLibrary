package com.example.completelibrary.services;

import com.example.completelibrary.entity.Member;
import com.example.completelibrary.exceptions.DuplicateEmailException;
import com.example.completelibrary.exceptions.ResourceNotFoundException;
import com.example.completelibrary.repository.MemberRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepo memberRepo;


    public Member findMemberByEmail(String email){
        return memberRepo.findMemberByEmail(email).orElseThrow(() ->new ResourceNotFoundException("Member not found"));
    }
    public Member findMemberById(Long id){

        return memberRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Not found" + id));
    }
    public Member save(Member member) {
        if (member.getId() == null && memberRepo.existsByEmail(member.getEmail())) {
            throw new DuplicateEmailException("Email already exists: " + member.getEmail());
        }
        return memberRepo.save(member);
    }
    public void deleteById(Long id){
        memberRepo.deleteById(id);
    }
    public List<Member> findAll() {
        return memberRepo.findAll();
    }

    public Member update(Long id, Member member) {
        memberRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found: " + id));
        member.setId(id);
        return memberRepo.save(member); // dùng save(member) thay vì save(id, member)
    }
}
