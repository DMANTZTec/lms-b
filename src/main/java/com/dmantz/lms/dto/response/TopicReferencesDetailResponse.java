package com.dmantz.lms.dto.response;

import java.util.List;

public class TopicReferencesDetailResponse {
	
	 private List<TopicReferenceResponseDto> documents;
	    private List<TopicReferenceResponseDto> videos;
	    private List<TopicReferenceResponseDto> urls;
		public List<TopicReferenceResponseDto> getDocuments() {
			return documents;
		}
		public void setDocuments(List<TopicReferenceResponseDto> documents) {
			this.documents = documents;
		}
		public List<TopicReferenceResponseDto> getVideos() {
			return videos;
		}
		public void setVideos(List<TopicReferenceResponseDto> videos) {
			this.videos = videos;
		}
		public List<TopicReferenceResponseDto> getUrls() {
			return urls;
		}
		public void setUrls(List<TopicReferenceResponseDto> urls) {
			this.urls = urls;
		}
	    
	    
}
