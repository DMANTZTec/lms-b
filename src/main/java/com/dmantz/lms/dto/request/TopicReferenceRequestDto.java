package com.dmantz.lms.dto.request;

import java.util.Map;

public class TopicReferenceRequestDto {
    private Map<String, Object> refValue;
    private String refBy;
    private Long refById;

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
