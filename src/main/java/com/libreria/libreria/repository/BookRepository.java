package com.libreria.libreria.repository;

import com.libreria.libreria.entity.BookModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends MongoRepository<BookModel, String> {

}