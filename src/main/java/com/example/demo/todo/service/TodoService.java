package com.example.demo.todo.service;

import java.util.List;

import com.example.demo.todo.vo.TodoVO;

public interface TodoService {

	List<TodoVO> getTaskList();
	
	TodoVO getTaskBy(String taskId);
	
	boolean done(String taskId);
	
	boolean addTask(TodoVO todoVO);
	
	
}
