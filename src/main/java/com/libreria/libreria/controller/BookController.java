package com.libreria.libreria.controller;

import com.libreria.libreria.entity.BookModel;
import com.libreria.libreria.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/book")
@Slf4j
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) { this.bookService = bookService; }

    @GetMapping("/all")
    public ResponseEntity<List<BookModel>> getAllBook() {
        List<BookModel> books = bookService.allBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookModel> getBookId(@PathVariable("id") String id) {
        Optional<BookModel> bookOpt = bookService.getBookById(id);
        return bookOpt.map(bookModel -> new ResponseEntity<>(bookModel, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<BookModel> createBook(@RequestBody BookModel book) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().iterator().next().getAuthority();

        if (role.equals("ROLE_ADMIN")) {
            BookModel savedBook = bookService.save(book);
            return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookModel> updateBook(@PathVariable("id") String id, @RequestBody BookModel book) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().iterator().next().getAuthority();

        if (role.equals("ROLE_ADMIN")) {
            BookModel updateBook = bookService.updateBook(book, id);

            if (updateBook != null) {
                return new ResponseEntity<>(updateBook, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable("id") String id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().iterator().next().getAuthority();

        if (role.equals("ROLE_ADMIN")) {
            bookService.deleteBook(id);
            return ResponseEntity.ok("Libro Eliminado Correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso Denegado: No tienes permiso para eliminar libros.");
        }
    }
}