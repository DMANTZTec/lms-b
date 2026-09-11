package com.dmantz.lms.dto.response;

public class ChapterDropdownResponse {
    private Long id;
    private String chapterNm;

    public ChapterDropdownResponse(Long id, String chapterNm) {
        this.id = id;
        this.chapterNm = chapterNm;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getChapterNm() { return chapterNm; }
    public void setChapterNm(String chapterNm) { this.chapterNm = chapterNm; }
}