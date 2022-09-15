package com.blog.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.app.models.Category;
import com.blog.app.models.Post;
import com.blog.app.models.User;

public interface PostRepo extends JpaRepository<Post, Integer>{


	List<Post> findByUser(User user);
	
	List<Post> findByCategory(Category category);
	
	//searching method
	//LIKE query will be generated at the backend
	List<Post> findByTitleContaining(String title);

}
