package com.dmantz.lms_b.dto.response;

public class ChapterProgressResponse {

    private Long chapterId;
    private String chapterName;
    private Integer completedTopics;
    private Integer totalTopics;
    private Double chapterPercentage;
    private Boolean completed;

    public Long getChapterId() {
        return chapterId;
    }

    public void setChapterId(Long chapterId) {
        this.chapterId = chapterId;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public Integer getCompletedTopics() {
        return completedTopics;
    }

    public void setCompletedTopics(Integer completedTopics) {
        this.completedTopics = completedTopics;
    }

    public Integer getTotalTopics() {
        return totalTopics;
    }

    public void setTotalTopics(Integer totalTopics) {
        this.totalTopics = totalTopics;
    }

    public Double getChapterPercentage() {
        return chapterPercentage;
    }

    public void setChapterPercentage(Double chapterPercentage) {
        this.chapterPercentage = chapterPercentage;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}