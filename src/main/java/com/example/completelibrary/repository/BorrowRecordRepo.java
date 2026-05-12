package com.example.completelibrary.repository;

import com.example.completelibrary.entity.Book;
import com.example.completelibrary.entity.BorrowRecord;
import com.example.completelibrary.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BorrowRecordRepo extends JpaRepository<BorrowRecord, Long> {
   public List<BorrowRecord> findBorrowRecordByMember(Member member);
   public List<BorrowRecord> findBorrowRecordByBook(Book book);
   public List<BorrowRecord> findBorrowRecordByReturnDateIsNullAndMember(Member member);
  public List<BorrowRecord>  findBorrowRecordByReturnDateIsNull();

    Optional<BorrowRecord> findByMemberAndBookAndReturnDateIsNull(Member member, Book book);

}
