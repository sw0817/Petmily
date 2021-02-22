package com.petmily.curation.dto;

import io.swagger.annotations.ApiParam;

public class MemberDto {
	
	@ApiParam(value = "user ID", required = true)
	private int user_id;
	@ApiParam(value = "user email", required = true)
	private String user_email;
	@ApiParam(value = "user nickname", required = true)
	private String  user_nickname;
	@ApiParam(value = "user password", required = true)
	private String  user_password;
	@ApiParam(value = "user name", required = true)
	private String  user_name;
	@ApiParam(value="user_content",required=false)
	private String user_content;	
	@ApiParam(value = "user birth", required = true)
	private String  user_birth;
	@ApiParam(value = "user gender", required = true)
	private String  user_gender;
	@ApiParam(value = "user create date", required = false)
	private String  create_date;
	@ApiParam(value = "user certified", required = false)
	private String  certified; //이메일 인증 여부
	
	public String getCertified() {
		return certified;
	}
	public void setCertified(String certified) {
		this.certified = certified;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getUser_email() {
		return user_email;
	}
	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}
	public String getUser_nickname() {
		return user_nickname;
	}
	public void setUser_nickname(String user_nickname) {
		this.user_nickname = user_nickname;
	}
	public String getUser_password() {
		return user_password;
	}
	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_content() {
		return user_content;
	}
	public void setUser_content(String user_content) {
		this.user_content = user_content;
	}
	public String getUser_birth() {
		return user_birth;
	}
	public void setUser_birth(String user_birth) {
		this.user_birth = user_birth;
	}
	public String getUser_gender() {
		return user_gender;
	}
	public void setUser_gender(String user_gender) {
		this.user_gender = user_gender;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	@Override
	public String toString() {
		return "MemberDto [user_id=" + user_id + ", user_email=" + user_email + ", user_nickname=" + user_nickname
				+ ", user_password=" + user_password + ", user_name=" + user_name + ", user_content=" + user_content + ", user_birth=" + user_birth
				+ ", user_gender=" + user_gender + ", create_date=" + create_date + ", certified=" + certified + "]";
	}
	
	
	
}
