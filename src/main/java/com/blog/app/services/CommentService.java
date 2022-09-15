package com.blog.app.services;

import com.blog.app.payloads.CommentDto;

public interface CommentService {
	
	public CommentDto createComment(CommentDto commentDto, Integer postId);
	
	void deleteComment(Integer commendId);

}
