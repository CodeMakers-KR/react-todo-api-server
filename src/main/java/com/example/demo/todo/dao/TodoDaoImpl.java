package com.example.demo.todo.dao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.demo.todo.vo.TaskVO;
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
	public List<TaskVO> getTaskList() {
		return fileDBHandler.selectRows("todo", (line) -> {
			TaskVO todoVO = gson.fromJson(line, TaskVO.class);
			return todoVO;
		});
	}

	@Override
	public TaskVO getTaskBy(String taskId) {
		return fileDBHandler.selectRow("todo", (line) -> {
			TaskVO todoVO = gson.fromJson(line, TaskVO.class);
			if (todoVO.getId().equals(taskId)) {
				return todoVO;
			}
			return null;
		});
	}

	@Override
	public int done(String taskId) {
		return fileDBHandler.update("todo", line -> {
			TaskVO todoVO = gson.fromJson(line, TaskVO.class);
			if (todoVO.getId().equals(taskId)) {
				todoVO.setDone(true);
				todoVO.setDoneAt(this.now());
				return todoVO;
			}
			return null;
		});
	}

	@Override
	public int addTask(TaskVO todoVO) {
		todoVO.setCreateAt(this.now());
		
		todoVO.setId("task_" + fileDBHandler.nextSeq("todo"));
		return fileDBHandler.insert("todo", todoVO);
	}

	@Override
	public int allDone() {
		return fileDBHandler.update("todo", line -> {
			TaskVO todoVO = gson.fromJson(line, TaskVO.class);
			todoVO.setDone(true);
			todoVO.setDoneAt(this.now());
			return todoVO;
		});
	}
	
	private String now() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}
	
}
