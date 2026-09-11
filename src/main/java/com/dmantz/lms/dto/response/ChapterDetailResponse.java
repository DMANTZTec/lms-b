package com.dmantz.lms.dto.response;

import java.util.List;

public class ChapterDetailResponse {
	 private Long chapterId;
	    private int chapterNumber;
	    private String chapterTitle;
	    private List<TopicDetailResponse> topics;
		public Long getChapterId() {
			return chapterId;
		}
		public void setChapterId(Long chapterId) {
			this.chapterId = chapterId;
		}
		public int getChapterNumber() {
			return chapterNumber;
		}
		public void setChapterNumber(int chapterNumber) {
			this.chapterNumber = chapterNumber;
		}
		public String getChapterTitle() {
			return chapterTitle;
		}
		public void setChapterTitle(String chapterTitle) {
			this.chapterTitle = chapterTitle;
		}
		public List<TopicDetailResponse> getTopics() {
			return topics;
		}
		public void setTopics(List<TopicDetailResponse> topics) {
			this.topics = topics;
		}
	    
	    

}
