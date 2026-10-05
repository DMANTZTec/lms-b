package com.dmantz.lms.entity;

public class GitDetail {

	private String githubUrl;
	private String commitMessage;

	public GitDetail() {
	}

	public GitDetail(String githubUrl, String commitMessage) {
		this.githubUrl = githubUrl;
		this.commitMessage = commitMessage;
	}

	public String getGithubUrl() {
		return githubUrl;
	}

	public void setGithubUrl(String githubUrl) {
		this.githubUrl = githubUrl;
	}

	public String getCommitMessage() {
		return commitMessage;
	}

	public void setCommitMessage(String commitMessage) {
		this.commitMessage = commitMessage;
	}

}
