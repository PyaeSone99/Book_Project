package com.example.bootstoreproject.dao;

import com.example.bootstoreproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDao extends JpaRepository<User,Integer> {
    Optional<User> findUserByUsername(String username);
}
