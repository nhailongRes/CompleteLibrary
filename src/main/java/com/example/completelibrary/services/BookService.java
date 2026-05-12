package com.example.completelibrary.services;

import com.example.completelibrary.entity.Book;
import com.example.completelibrary.exceptions.BookNotAvailableException;
import com.example.completelibrary.exceptions.ResourceNotFoundException;
import com.example.completelibrary.repository.BookRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepo repo;

    public List<Book> findBookByAuthor(String author){
        return repo.findBooksByAuthor(author);
    }
    public List<Book> findBookByTitle(String title){
        return repo.findBooksByTitle(title);
    }
    public List<Book> findBooksByAvailableCopiesGreaterThan(int copies){
        return repo.findBooksByAvailableCopiesGreaterThan(copies);
    }
    public List<Book> findBooksByTitleContaining(String keyword){
        return repo.findBooksByTitleContaining(keyword);
    }
    public void deleteBooksByAuthor(String author){
        repo.deleteBooksByAuthor(author);
    }
    public void deleteBooksByTitle(String title){
        repo.deleteBooksByTitle(title);
    }
    public List<Book> findAllAvailableBooks() {
        return repo.findAllAvailableBooks();
    }

    public List<Book> searchBooks(String keyword) {
        return repo.searchBooks(keyword);
    }

    public List<Book> findOutOfStockBooks() {
        return repo.findOutOfStockBooks();
    }

    public List<Book> findAll() {
        return repo.findAll();
    }

    public Book findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book not found with: " + id));
    }
    public Book save(Book book  ){
        return repo.save(book);
    }
    public Book update(Long id, Book book) {
        repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found: " + id));
        book.setId(id);
        return repo.save(book); // dùng save thay vì repo.update
    }
    public void delete(Long id) {
        repo.deleteById(id); // dùng deleteById thay vì repo.delete
    }
}
