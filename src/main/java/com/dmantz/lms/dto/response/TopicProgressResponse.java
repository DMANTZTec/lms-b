package com.dmantz.lms.dto.response;

public class TopicProgressResponse {

    private Long topicId;
    private String topicName;
    private Integer completedTopicReference;
    private Integer totalTopicReference;
    private Double ProgressPercentage;
    private Boolean completed;

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public Integer getCompletedTopicReference() {
        return completedTopicReference;
    }

    public void setCompletedTopicReference(Integer completedTopicReference) {
        this.completedTopicReference = completedTopicReference;
    }

    public Integer getTotalTopicReference() {
        return totalTopicReference;
    }

    public void setTotalTopicReference(Integer totalTopicReference) {
        this.totalTopicReference = totalTopicReference;
    }

    
    public Double getProgressPercentage() {
		return ProgressPercentage;
	}

	public void setProgressPercentage(Double progressPercentage) {
		ProgressPercentage = progressPercentage;
	}

	public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}