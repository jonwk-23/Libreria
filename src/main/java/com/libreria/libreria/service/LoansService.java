package com.libreria.libreria.service;

import com.libreria.libreria.entity.BookModel;
import com.libreria.libreria.entity.LoansModel;
import com.libreria.libreria.repository.BookRepository;
import com.libreria.libreria.repository.LoansRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class LoansService {
    private final BookRepository bookRepository;
    private final LoansRepository loansRepository;

    @Autowired
    public LoansService(BookRepository bookRepository, LoansRepository loansRepository) {
        this.bookRepository = bookRepository;
        this.loansRepository = loansRepository;
    }

    public ResponseEntity<String> loanBook(String bookId, String userId) {
        Optional<BookModel> bookOpt = bookRepository.findById(bookId);

        if (bookOpt.isPresent() && bookOpt.get().isDisponibilidad()) {
            BookModel book = bookOpt.get();
            book.setDisponibilidad(false);
            bookRepository.save(book);

            LoansModel loan = new LoansModel();
            loan.setBookId(bookId);
            loan.setUserId(userId);
            loan.setPrestarFecha(new Date());
            loansRepository.save(loan);

            return ResponseEntity.ok("Libro Prestado");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El libro no está disponible o no existe");
    }

    public ResponseEntity<String> returnDate(String bookId, String userId) {
        Optional<BookModel> bookOpt = bookRepository.findById(bookId);

        if (bookOpt.isPresent()) {
            BookModel book = bookOpt.get();
            Optional<LoansModel> loansOpt = loansRepository.findByBookIdAndUserId(bookId, userId);

            if (loansOpt.isPresent()) {
                LoansModel loan = loansOpt.get();
                book.setDisponibilidad(true);
                bookRepository.save(book);
                loan.setDevolverFecha(new Date());
                loansRepository.save(loan);

                return ResponseEntity.ok("Libro Devuelto");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("No se encontró el préstamo para este libro y usuario");
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El libro no existe");
    }
}