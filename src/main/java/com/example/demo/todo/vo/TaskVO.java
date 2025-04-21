package com.example.demo.todo.vo;

import com.google.gson.Gson;

public class TaskVO {

	private String id;
	private String task;
	private String dueDate;
	private String priority;
	private boolean isDone;

	private String createAt;
	private String doneAt;

	public TaskVO() {
	}

	public TaskVO(TodoVO todoVO) {
		this.id = todoVO.getTaskId();
		this.task = todoVO.getTask();
		this.dueDate = todoVO.getDueDate();
		this.priority = todoVO.getPriority();
		this.isDone = todoVO.isDone();
		this.createAt = todoVO.getCreateAt();
		this.doneAt = todoVO.getDoneAt();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public boolean isDone() {
		return isDone;
	}

	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}

	public String getCreateAt() {
		return createAt;
	}

	public void setCreateAt(String createAt) {
		this.createAt = createAt;
	}

	public String getDoneAt() {
		return doneAt;
	}

	public void setDoneAt(String doneAt) {
		this.doneAt = doneAt;
	}

	public String toString() {
		return new Gson().toJson(this);
	}

}
