package com.dmantz.lms.dto.response;

public class ScheduleTaskSummaryResponse {

    private Long taskId;
    private String topicName;
    private String taskTitle;

    public ScheduleTaskSummaryResponse() {
    }

    public ScheduleTaskSummaryResponse(Long taskId, String topicName, String taskTitle) {
        this.taskId = taskId;
        this.topicName = topicName;
        this.taskTitle = taskTitle;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }
}
