package com.petmily.curation.dto;

public class BackgroundDto {
	private int bg_id;
	private int user_id;
	private String file_name;
	private String file_oriname;
	private String file_url;
	public int getBg_id() {
		return bg_id;
	}
	public void setBg_id(int bg_id) {
		this.bg_id = bg_id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getFile_name() {
		return file_name;
	}
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
	public String getFile_oriname() {
		return file_oriname;
	}
	public void setFile_oriname(String file_oriname) {
		this.file_oriname = file_oriname;
	}
	public String getFile_url() {
		return file_url;
	}
	public void setFile_url(String file_url) {
		this.file_url = file_url;
	}
	@Override
	public String toString() {
		return "BackgroundDto [bg_id=" + bg_id + ", user_id=" + user_id + ", file_name=" + file_name + ", file_oriname="
				+ file_oriname + ", file_url=" + file_url + "]";
	}
	
	
}
