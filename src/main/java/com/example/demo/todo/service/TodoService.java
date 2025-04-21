package com.example.demo.todo.service;

import java.util.List;

import com.example.demo.todo.vo.TaskVO;

public interface TodoService {

	List<TaskVO> getTaskList();
	
	TaskVO getTaskBy(String taskId);
	
	boolean done(String taskId);
	
	boolean addTask(TaskVO todoVO);

	boolean allDone();
	
	
}
