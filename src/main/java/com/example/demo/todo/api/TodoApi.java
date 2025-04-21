package com.example.demo.todo.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.todo.service.TodoService;
import com.example.demo.todo.vo.TaskVO;
import com.example.demo.todo.vo.TodoVO;
import com.example.demo.util.AjaxResponse;

@RestController
@RequestMapping("/api/v1")
public class TodoApi {

	@Autowired
	private TodoService todoService;
	
	@GetMapping("/task")
	public AjaxResponse getTasks() {
		List<TaskVO> todos = todoService.getTaskList();
		return AjaxResponse.OK(todos);
	}
	
	@PostMapping("/task")
	public AjaxResponse addTask(@RequestBody TodoVO task) {
		TaskVO tempTaskVO = new TaskVO(task);
		todoService.addTask(tempTaskVO);
		task.setTaskId(tempTaskVO.getId());
		
		return AjaxResponse.CREATED().setBody(task);
	}
	
	@GetMapping("/task/{taskId}")
	public AjaxResponse getTask(@PathVariable String taskId) {
		TaskVO todo = todoService.getTaskBy(taskId);
		if (todo == null) {
			return AjaxResponse.NOT_FOUND();
		}
		return AjaxResponse.OK(todo);
	}
	
	@PutMapping("/task/{taskId}")
	public AjaxResponse done(@PathVariable String taskId) {
		boolean isDone = todoService.done(taskId);
		if (isDone) {
			return AjaxResponse.OK(taskId);
		}
		return AjaxResponse.NOT_FOUND();
	}
	
	@PutMapping("/task")
	public AjaxResponse allDone() {
		todoService.allDone();
		return AjaxResponse.OK();
	}
	
}
