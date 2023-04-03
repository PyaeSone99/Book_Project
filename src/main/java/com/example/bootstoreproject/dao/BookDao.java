package com.example.bootstoreproject.dao;

import com.example.bootstoreproject.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookDao extends JpaRepository<Book,Integer> {
}
