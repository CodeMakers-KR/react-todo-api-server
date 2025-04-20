package com.example.demo.todo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.todo.dao.TodoDao;
import com.example.demo.todo.vo.TodoVO;

@Service
public class TodoServiceImpl implements TodoService {

	@Autowired
	private TodoDao todoDao;
	
	@Override
	public List<TodoVO> getTaskList() {
		return todoDao.getTaskList();
	}

	@Override
	public TodoVO getTaskBy(String taskId) {
		return todoDao.getTaskBy(taskId);
	}

	@Override
	public boolean done(String taskId) {
		if (todoDao.getTaskBy(taskId) == null) {
			return false;
		}
		
		return todoDao.done(taskId) > 0;
	}

	@Override
	public boolean addTask(TodoVO todoVO) {
		return todoDao.addTask(todoVO) > 0;
	}

	@Override
	public boolean allDone() {
		return todoDao.allDone() > 0;
	}

}
