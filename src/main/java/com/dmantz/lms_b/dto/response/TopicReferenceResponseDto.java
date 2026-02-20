package com.dmantz.lms_b.dto.response;

import java.util.Map;

public class TopicReferenceResponseDto {
    private Long id;
    private Long topicId;
    private String refType;
    private Map<String, Object> refValue;
    private String refBy;
    private Long refById;

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

    public String getRefType() {
        return refType;
    }

    public void setRefType(String refType) {
        this.refType = refType;
    }

    public Map<String, Object> getRefValue() {
        return refValue;
    }

    public void setRefValue(Map<String, Object> refValue) {
        this.refValue = refValue;
    }

    public String getRefBy() {
        return refBy;
    }

    public void setRefBy(String refBy) {
        this.refBy = refBy;
    }

    public Long getRefById() {
        return refById;
    }

    public void setRefById(Long refById) {
        this.refById = refById;
    }
}
