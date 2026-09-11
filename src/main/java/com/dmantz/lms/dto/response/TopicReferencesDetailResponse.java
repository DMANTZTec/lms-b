package com.dmantz.lms.dto.response;

import java.util.List;

public class TopicReferencesDetailResponse {

	private List<TopicReferenceDataDto> documents;
	private List<TopicReferenceDataDto> videos;
	private List<TopicReferenceDataDto> urls;

	public List<TopicReferenceDataDto> getDocuments() {
		return documents;
	}

	public void setDocuments(List<TopicReferenceDataDto> documents) {
		this.documents = documents;
	}

	public List<TopicReferenceDataDto> getVideos() {
		return videos;
	}

	public void setVideos(List<TopicReferenceDataDto> videos) {
		this.videos = videos;
	}

	public List<TopicReferenceDataDto> getUrls() {
		return urls;
	}

	public void setUrls(List<TopicReferenceDataDto> urls) {
		this.urls = urls;
	}
}