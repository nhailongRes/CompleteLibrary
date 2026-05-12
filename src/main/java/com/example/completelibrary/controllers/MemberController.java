package com.example.completelibrary.controllers;

import com.example.completelibrary.entity.Member;
import com.example.completelibrary.services.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;


    @GetMapping
    public ResponseEntity<List<Member>> getAll(){
        return ResponseEntity.ok(memberService.findAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Member> getById(@PathVariable Long id){
        return ResponseEntity.ok(memberService.findMemberById(id));
    }
    @GetMapping("/search")
    public ResponseEntity<Member> getByEmail(@RequestParam String email){
        return ResponseEntity.ok(memberService.findMemberByEmail(email));
    }

    @PostMapping
    public ResponseEntity<Member> create(@RequestBody Member member){
        return ResponseEntity.status(201).body(memberService.save(member));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Member> update(@PathVariable Long id, @RequestBody Member member){
        return ResponseEntity.ok(memberService.update(id, member));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        memberService.deleteById(id);
        return ResponseEntity.noContent().build();
    }



}
