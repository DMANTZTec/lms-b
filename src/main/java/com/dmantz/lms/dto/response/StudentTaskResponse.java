package com.dmantz.lms.dto.response;

import java.util.List;

public class StudentTaskResponse {

    private String id;
    private String title;
    private String description;
    private List<String> tags;

    public StudentTaskResponse() {
    }

    public StudentTaskResponse(String id, String title, String description, List<String> tags) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.tags = tags;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}