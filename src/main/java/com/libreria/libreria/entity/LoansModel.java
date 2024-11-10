package com.libreria.libreria.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "loans")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoansModel {
    @Id
    private String id;

    private String bookId;
    private String userId;
    private Date prestarFecha;
    private Date devolverFecha;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getPrestarFecha() {
        return prestarFecha;
    }

    public void setPrestarFecha(Date prestarFecha) {
        this.prestarFecha = prestarFecha;
    }

    public Date getDevolverFecha() {
        return devolverFecha;
    }

    public void setDevolverFecha(Date devolverFecha) {
        this.devolverFecha = devolverFecha;
    }
}