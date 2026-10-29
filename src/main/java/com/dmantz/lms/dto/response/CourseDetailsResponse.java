package com.dmantz.lms.dto.response;

import java.util.List;

public class CourseDetailsResponse {

	private String courseTitle;

	private String description;
	
	  private List<ChapterDetailResponse> chapters;

	  public String getCourseTitle() {
		  return courseTitle;
	  }

	  public void setCourseTitle(String courseTitle) {
		  this.courseTitle = courseTitle;
	  }

	  public String getDescription() {
		  return description;
	  }

	  public void setDescription(String description) {
		  this.description = description;
	  }

	  public List<ChapterDetailResponse> getChapters() {
		  return chapters;
	  }

	  public void setChapters(List<ChapterDetailResponse> chapters) {
		  this.chapters = chapters;
	  }
	  
	  
	
	
}
