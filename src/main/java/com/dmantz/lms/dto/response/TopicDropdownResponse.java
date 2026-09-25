package com.dmantz.lms.dto.response;

public class TopicDropdownResponse {
    private Long id;
    private String topicNm;

    public TopicDropdownResponse(Long id, String topicNm) {
        this.id = id;
        this.topicNm = topicNm;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTopicNm() { return topicNm; }
    public void setTopicNm(String topicNm) { this.topicNm = topicNm; }
}