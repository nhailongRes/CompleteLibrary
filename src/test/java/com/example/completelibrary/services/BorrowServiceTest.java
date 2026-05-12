package com.example.completelibrary.services;

import com.example.completelibrary.entity.Book;
import com.example.completelibrary.entity.BorrowRecord;
import com.example.completelibrary.entity.Member;
import com.example.completelibrary.exceptions.BookNotAvailableException;
import com.example.completelibrary.exceptions.ResourceNotFoundException;
import com.example.completelibrary.repository.BookRepo;
import com.example.completelibrary.repository.BorrowRecordRepo;
import com.example.completelibrary.repository.MemberRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BorrowServiceTest {
    @Mock
    private BorrowRecordRepo borrowRecordRepo;
    @Mock
    private BookRepo bookRepo;
    @Mock
    private MemberRepo memberRepo;

    @InjectMocks
    private BorrowService borrowService;

    @Test
    void borrowBook() {
        Member member = new Member(1L, "Hai", "hai@gmail.com","1234");
        Book book = new Book(1L,"Chim","Alex","123",5);

        when(memberRepo.findById(1L)).thenReturn(Optional.of(member));
        when(bookRepo.findById(1L)).thenReturn(Optional.of(book));
        when(borrowRecordRepo.findByMemberAndBookAndReturnDateIsNull(member, book)).thenReturn(Optional.empty());
        borrowService.borrowBook(1L, 1L);
        assertEquals(4, book.getAvailableCopies());
        verify(bookRepo,times(1)).save(any());
        verify(borrowRecordRepo, times(1)).save(any());
    }

    @Test
    void borrowBookSuccess(){
        Member member = new Member(1L, "Hai", "hai@gmail.com","1234");
        Book book = new Book(1L,"Chim","Alex","123",5);
        when(memberRepo.findById(1L)).thenReturn(Optional.of(member));
        when(bookRepo.findById(1L)).thenReturn(Optional.of(book));
        borrowService.borrowBook(1L,1L);
        assertEquals(4, book.getAvailableCopies());
        verify(bookRepo,times(1)).save(any());
        verify(borrowRecordRepo, times(1)).save(any());

    }
    @Test
    void borrowBookWhenMemberNull() {
        when(memberRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> {
            borrowService.borrowBook(1L, 1L);
        });
    }
    @Test
    void borrowBookWhenNoCopies(){
        Member member = new Member(1L, "Hai", "hai@gmail.com","1234");
        Book book = new Book(1L,"Chim","Alex","123",0);

        when(memberRepo.findById(1L)).thenReturn(Optional.of(member));
        when(bookRepo.findById(1L)).thenReturn(Optional.of(book));

        assertThrows(BookNotAvailableException.class, ()->
                borrowService.borrowBook(1L,1L));
        verify(borrowRecordRepo, never()).save(any());

    }
    @Test
    void borrowBookWhenAlreadyBorrowed(){
        Member member = new Member(1L, "Hai", "hai@gmail.com","1234");
        Book book = new Book(1L,"Chim","Alex","123",5);
        BorrowRecord borrowRecord = new BorrowRecord(1L,book,member, LocalDateTime.now(),null);
        when(memberRepo.findById(1L)).thenReturn(Optional.of(member));
        when(bookRepo.findById(1L)).thenReturn(Optional.of(book));
        when(borrowRecordRepo.findByMemberAndBookAndReturnDateIsNull(member,book)).thenReturn(Optional.of(borrowRecord));

        assertThrows(BookNotAvailableException.class,()->
                borrowService.borrowBook(1L,1L)  );
        verify(borrowRecordRepo, never()).save(any());
    }
    @Test
    void returnBookAndRecordNotFound() {
        Member member = new Member(1L, "Hai", "hai@gmail.com","1234");
        Book book = new Book(1L,"Chim","Alex","123",5);
        when(memberRepo.findById(1L)).thenReturn(Optional.of(member));
        when(bookRepo.findById(1L)).thenReturn(Optional.of(book));
        when(borrowRecordRepo.findByMemberAndBookAndReturnDateIsNull(member,book)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                ()->borrowService.returnBook(1L,1L)
                );
        verify(bookRepo, never()).save(any());
        verify(borrowRecordRepo, never()).save(any());

    }
    @Test
    void returnBook() {
        Member member = new Member(1L, "Hai", "hai@gmail.com","1234");
        Book book = new Book(1L,"Chim","Alex","123",3);
        BorrowRecord borrowRecord = new BorrowRecord(1L,book,member, LocalDateTime.now(),null);
        when(memberRepo.findById(1L)).thenReturn(Optional.of(member));
        when(bookRepo.findById(1L)).thenReturn(Optional.of(book));
        when(borrowRecordRepo.findByMemberAndBookAndReturnDateIsNull(member,book)).thenReturn(Optional.of(borrowRecord));
        borrowService.returnBook(1L,1L);
        assertNotNull(borrowRecord.getReturnDate());
        assertEquals(4,book.getAvailableCopies());
        verify(borrowRecordRepo, times(1)).save(any());
        verify(bookRepo, times(1)).save(any());


    }
}