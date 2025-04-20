package com.example.demo.todo.dao;

import java.util.List;

import com.example.demo.todo.vo.TodoVO;

public interface TodoDao {

	List<TodoVO> getTaskList();
	
	TodoVO getTaskBy(String taskId);
	
	int done(String taskId);
	
	int addTask(TodoVO todoVO);
	
}
