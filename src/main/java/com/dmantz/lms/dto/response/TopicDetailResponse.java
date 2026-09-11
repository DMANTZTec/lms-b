package com.dmantz.lms.dto.response;

import java.util.List;

public class TopicDetailResponse {
	   private Long topicId;
	    private Long topicNum;
	    private String topicTitle;
	    private String duration;
	    private String topicDescription;
	    private TopicReferencesDetailResponse resources;
		public Long getTopicId() {
			return topicId;
		}
		public void setTopicId(Long topicId) {
			this.topicId = topicId;
		}
		public Long getTopicNum() {
			return topicNum;
		}
		public void setTopicNum(Long topicNum) {
			this.topicNum = topicNum;
		}
		public String getTopicTitle() {
			return topicTitle;
		}
		public void setTopicTitle(String topicTitle) {
			this.topicTitle = topicTitle;
		}
		public String getDuration() {
			return duration;
		}
		public void setDuration(String duration) {
			this.duration = duration;
		}
		public String getTopicDescription() {
			return topicDescription;
		}
		public void setTopicDescription(String topicDescription) {
			this.topicDescription = topicDescription;
		}
		public TopicReferencesDetailResponse getResources() {
			return resources;
		}
		public void setResources(TopicReferencesDetailResponse resources) {
			this.resources = resources;
		}
		
	    
	    
}
