package com.libreria.libreria.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.libreria.libreria.entity.UserModel;
import com.libreria.libreria.repository.UserRepository;
import com.libreria.libreria.util.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserModel save(UserModel user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.CLIENT);
        return userRepository.save(user);
    }

    public List<UserModel> allusers(){ return userRepository.findAll(); }
    public Optional<UserModel> getUserById(String id) {return userRepository.findById(id); }
    public void deleteUser(String id) { userRepository.deleteById(id); }

    public UserModel updateUserRole(String id, Role newRole) {
        return userRepository.findById(id).map(user -> {
            user.setRole(newRole);
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public String existsUser(String email, String password) {
        Optional<UserModel> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            if (passwordEncoder.matches(password, user.get().getPassword())) {
                return generarToken(user.get());
            } else {
                throw new RuntimeException("Invalid Credentials");
            }
        } else {
            throw new RuntimeException("Invalid Credentials");
        }
    }

    private String generarToken(UserModel user) {
        return JWT.create()
                .withSubject(user.getEmail())
                .withClaim("role", user.getRole().name())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 *10))
                .sign(Algorithm.HMAC256("SECRET_KEY"));
    }

    public Optional<UserModel> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean isUserOwner(String email, String id) {
        return userRepository.findById(id)
                .map(user -> user.getEmail().equals(email))
                .orElse(false);
    }
}