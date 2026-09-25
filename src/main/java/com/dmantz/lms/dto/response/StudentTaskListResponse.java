package com.dmantz.lms.dto.response;

import java.util.List;

public class StudentTaskListResponse {

	private int count;
	private List<StudentTaskResponse> tasks;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<StudentTaskResponse> getTasks() {
		return tasks;
	}

	public void setTasks(List<StudentTaskResponse> tasks) {
		this.tasks = tasks;
	}
}
