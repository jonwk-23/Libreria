package com.libreria.libreria.service;

import com.libreria.libreria.entity.BookModel;
import com.libreria.libreria.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) { this.bookRepository = bookRepository; }

    public BookModel save(BookModel book) { return bookRepository.save(book); }
    public List<BookModel> allBooks() { return bookRepository.findAll(); }
    public Optional<BookModel> getBookById(String id) { return bookRepository.findById(id); }
    public void deleteBook(String id) { bookRepository.deleteById(id); }

    public BookModel updateBook(BookModel book, String id) {
        return bookRepository.findById(id).map(existBook -> {
            existBook.setTitulo(book.getTitulo());
            existBook.setAutor(book.getAutor());
            existBook.setDisponibilidad(book.isDisponibilidad());
            return bookRepository.save(existBook);
        }).orElse(null);
    }
}