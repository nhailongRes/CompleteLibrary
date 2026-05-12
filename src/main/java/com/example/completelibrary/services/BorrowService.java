package com.example.completelibrary.services;

import com.example.completelibrary.entity.Book;
import com.example.completelibrary.entity.BorrowRecord;
import com.example.completelibrary.entity.Member;
import com.example.completelibrary.exceptions.BookNotAvailableException;
import com.example.completelibrary.exceptions.ResourceNotFoundException;
import com.example.completelibrary.repository.BookRepo;
import com.example.completelibrary.repository.BorrowRecordRepo;
import com.example.completelibrary.repository.MemberRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BorrowService {
    private final BorrowRecordRepo borrowRecordRepo;
    private final BookRepo bookRepo;
    private final MemberRepo memberRepo;
    @Transactional
    public void borrowBook(Long memberId, Long bookId){
        Member member = memberRepo.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found: " + memberId));
        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found: " + bookId));
        if (book.getAvailableCopies() <= 0) throw new BookNotAvailableException("No available copies");
        Optional<BorrowRecord> existing = borrowRecordRepo.findByMemberAndBookAndReturnDateIsNull(member, book);
        if (existing.isPresent()) throw new BookNotAvailableException("Member is already borrowing this book");
        BorrowRecord borrowRecord = new BorrowRecord();
        borrowRecord.setMember(member);
        borrowRecord.setBook(book);
        borrowRecord.setBorrowDate(LocalDateTime.now());
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepo.save(book);
        borrowRecordRepo.save(borrowRecord);
    }
    @Transactional
    public void returnBook(Long memberId, Long bookId){
        Member member = memberRepo.findById(memberId).orElseThrow(() -> new ResourceNotFoundException("Member not found " + memberId));
        Book book = bookRepo.findById(bookId).orElseThrow(()->
                new ResourceNotFoundException("Book not found " + bookId)
        );
        Optional<BorrowRecord> activeRecord = borrowRecordRepo.findByMemberAndBookAndReturnDateIsNull(member, book);
        BorrowRecord record = activeRecord.orElseThrow(() -> new ResourceNotFoundException("No record found"));
        record.setReturnDate(LocalDateTime.now());
        book.setAvailableCopies(book.getAvailableCopies() +1);
        borrowRecordRepo.save(record);
        bookRepo.save(book);

    }

}
