package com.blog.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.app.models.Category;

public interface CategoryRepo extends JpaRepository<Category, Integer> {

}
