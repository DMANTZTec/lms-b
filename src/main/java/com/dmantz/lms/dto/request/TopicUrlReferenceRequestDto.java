package com.dmantz.lms.dto.request;
public class TopicUrlReferenceRequestDto {
    private String title;      // display name for the URL
    private String url;        // the actual URL
    private String refBy;
    private String refById;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getRefBy() {
		return refBy;
	}
	public void setRefBy(String refBy) {
		this.refBy = refBy;
	}
	public String getRefById() {
		return refById;
	}
	public void setRefById(String refById) {
		this.refById = refById;
	}
    
}