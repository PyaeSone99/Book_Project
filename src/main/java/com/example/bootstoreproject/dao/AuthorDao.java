package com.example.bootstoreproject.dao;

import com.example.bootstoreproject.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorDao extends JpaRepository<Author,Integer> {
}
