package com.dmantz.lms.dto.request;

public class DocumentReferenceRequestDto {
	private String documentName;
	private String refBy;
	private String refById;

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
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