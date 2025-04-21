package com.example.demo.todo.dao;

import java.util.List;

import com.example.demo.todo.vo.TaskVO;

public interface TodoDao {

	List<TaskVO> getTaskList();
	
	TaskVO getTaskBy(String taskId);
	
	int done(String taskId);
	
	int addTask(TaskVO todoVO);

	int allDone();
	
}
