package com.libreria.libreria.controller;

import com.libreria.libreria.service.LoansService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/loans")
public class LoansController {
    private final LoansService loansService;

    public LoansController(LoansService loansService) { this.loansService = loansService; }

    @PostMapping("/prestar/{id}")
    public ResponseEntity<String> prestar(@PathVariable("id") String id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().iterator().next().getAuthority();
        String userId = auth.getName();

        if (role.equals("ROLE_CLIENT") || role.equals("ROLE_ADMIN")) {
            return loansService.loanBook(id, userId);
        } else {
            return new ResponseEntity<>("No tienes permiso para realizar esta acción", HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/devolver/{id}")
    public ResponseEntity<String> devolver(@PathVariable("id") String id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().iterator().next().getAuthority();
        String userId = auth.getName();

        if (role.equals("ROLE_CLIENT") || role.equals("ROLE_ADMIN")) {
            return loansService.returnDate(id, userId);
        } else {
            return new ResponseEntity<>("No tienes permiso para realizar esta acción", HttpStatus.FORBIDDEN);
        }
    }
}