package com.email.writer;

import lombok.Data;


public class EmailRequest {
	
	private String emailContent;
	public String getEmailContent() {
		return emailContent;
	}
	public void setEmailContent(String emailContent) {
		this.emailContent = emailContent;
	}
	public String getTone() {
		return tone;
	}
	public void setTone(String tone) {
		this.tone = tone;
	}
	private String tone;

}
