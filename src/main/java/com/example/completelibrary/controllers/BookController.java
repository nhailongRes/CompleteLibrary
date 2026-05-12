package com.example.completelibrary.controllers;

import com.example.completelibrary.entity.Book;
import com.example.completelibrary.repository.BookRepo;
import com.example.completelibrary.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;
    @GetMapping
    public List<Book> findAll(){
        return bookService.findAll();
    }
    @GetMapping("/{id}")
    public Book getById(@PathVariable Long id){
        return bookService.findById(id);
    }
    @GetMapping("/search")
    public List<Book> getByTitle(@RequestParam String keyword){
        return bookService.searchBooks(keyword);
    }
    @PostMapping
    public Book create(@RequestBody Book book){
        return bookService.save(book);
    }
    @PutMapping("/{id}")
    public Book update(@PathVariable Long id, @RequestBody Book book){
        return bookService.update(id, book);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        bookService.delete(id);
    }
}
