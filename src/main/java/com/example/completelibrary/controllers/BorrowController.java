package com.example.completelibrary.controllers;

import com.example.completelibrary.entity.Book;
import com.example.completelibrary.services.BorrowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/borrow")
@RequiredArgsConstructor
public class BorrowController {
    private final BorrowService borrowService;

    @PostMapping("/{memberId}/book/{bookId}")
    public ResponseEntity<Void> borrowBook(
            @PathVariable Long memberId,
            @PathVariable Long bookId) {
        borrowService.borrowBook(memberId, bookId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/return/{memberId}/book/{bookId}")
    public ResponseEntity<Void> returnBook(
            @PathVariable Long memberId,
            @PathVariable Long bookId) {
        borrowService.returnBook(memberId, bookId);
        return ResponseEntity.noContent().build();
    }
}
