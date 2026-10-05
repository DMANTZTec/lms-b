package com.dmantz.lms.dto.response;

public class ClassTopicResponse {
    private Long id;
    private Long topicId;
    private String topicName;
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ClassTopicResponse{" +
                "id=" + id +
                ", topicId=" + topicId +
                ", topicName='" + topicName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
