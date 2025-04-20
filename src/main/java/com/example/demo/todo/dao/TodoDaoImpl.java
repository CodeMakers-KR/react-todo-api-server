package com.example.demo.todo.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.demo.todo.vo.TodoVO;
import com.example.demo.util.FileDBHandler;
import com.google.gson.Gson;

@Repository
public class TodoDaoImpl implements TodoDao {

	private Gson gson;

	@Autowired
	private FileDBHandler fileDBHandler;
	
	public TodoDaoImpl() {
		this.gson = new Gson();
	}

	@Override
	public List<TodoVO> getTaskList() {
		return fileDBHandler.selectRows("todo", (line) -> {
			TodoVO todoVO = gson.fromJson(line, TodoVO.class);
			return todoVO;
		});
	}

	@Override
	public TodoVO getTaskBy(String taskId) {
		return fileDBHandler.selectRow("todo", (line) -> {
			TodoVO todoVO = gson.fromJson(line, TodoVO.class);
			if (todoVO.getTaskId().equals(taskId)) {
				return todoVO;
			}
			return null;
		});
	}

	@Override
	public int done(String taskId) {
		return fileDBHandler.update("todo", line -> {
			TodoVO todoVO = gson.fromJson(line, TodoVO.class);
			if (todoVO.getTaskId().equals(taskId)) {
				todoVO.setDone(true);
				return todoVO;
			}
			return null;
		});
	}

	@Override
	public int addTask(TodoVO todoVO) {
		todoVO.setTaskId("task_" + fileDBHandler.nextSeq("todo"));
		return fileDBHandler.insert("todo", todoVO);
	}

	@Override
	public int allDone() {
		return fileDBHandler.update("todo", line -> {
			TodoVO todoVO = gson.fromJson(line, TodoVO.class);
			todoVO.setDone(true);
			return todoVO;
		});
	}
	
}
