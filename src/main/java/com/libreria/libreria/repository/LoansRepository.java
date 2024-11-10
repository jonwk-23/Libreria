package com.libreria.libreria.repository;

import com.libreria.libreria.entity.LoansModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoansRepository extends MongoRepository<LoansModel, String> {
    Optional<LoansModel> findByBookIdAndUserId(String bookId, String userId);
}