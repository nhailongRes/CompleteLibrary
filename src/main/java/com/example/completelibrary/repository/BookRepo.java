package com.example.completelibrary.repository;

import com.example.completelibrary.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepo extends JpaRepository<Book, Long> {
    List<Book> findBooksByAuthor(String name);
    List<Book> findBooksByTitle(String title);
    List<Book> findBooksByAvailableCopiesGreaterThan(int copies);
    List<Book> findBooksByTitleContaining(String keyword);

    @Query("SELECT b FROM Book b WHERE b.author = :author")
    List<Book> findByAuthorJPQL(@Param("author") String author);

    @Query("SELECT b FROM Book b WHERE b.availableCopies > 0")
    List<Book> findAllAvailableBooks();

    @Query("SELECT b FROM Book b WHERE b.title LIKE %:keyword% OR b.author LIKE %:keyword%")
    List<Book> searchBooks(@Param("keyword") String keyword);

    @Query(value = "SELECT * FROM book WHERE available_copies = 0", nativeQuery = true)
    List<Book> findOutOfStockBooks();

    void deleteBooksByAuthor(String author);
    void deleteBooksByTitle(String title);

}
