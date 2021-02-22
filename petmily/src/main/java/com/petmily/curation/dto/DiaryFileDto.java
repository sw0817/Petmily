package com.petmily.curation.dto;

public class DiaryFileDto {
	private int df_id;
	private int user_id;
	private int diary_id;
	private String file_type; 
	private String file_name;
	private String file_oriname;
	private String file_url;
	public int getDf_id() {
		return df_id;
	}
	public void setDf_id(int df_id) {
		this.df_id = df_id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getDiary_id() {
		return diary_id;
	}
	public void setDiary_id(int diary_id) {
		this.diary_id = diary_id;
	}
	public String getFile_type() {
		return file_type;
	}
	public void setFile_type(String file_type) {
		this.file_type = file_type;
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
		return "DiaryFileDto [df_id=" + df_id + ", user_id=" + user_id + ", diary_id=" + diary_id + ", file_type="
				+ file_type + ", file_name=" + file_name + ", file_oriname=" + file_oriname + ", file_url=" + file_url
				+ "]";
	}
	
	
}
