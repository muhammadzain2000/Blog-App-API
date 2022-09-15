package com.blog.app.payloads;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.blog.app.models.Category;
import com.blog.app.models.Comment;
import com.blog.app.models.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class PostDto {
	
	private Integer postId;
	private String title;
	private String content;
	
	private String imageName;
	private Date addedDate;
	
	
	private CategoryDto category;
	
	
	private UserDto user;
	
	//jab post fetch hogi to uske comments bhi ajyenge
	private Set<CommentDto> comments= new HashSet<>();
	
	

}
