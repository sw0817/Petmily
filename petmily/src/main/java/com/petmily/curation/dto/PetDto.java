package com.petmily.curation.dto;


public class PetDto {
	private int pet_id;
	private int user_id;
	private int pet_age;
	private String  pet_name;
	private String  pet_kind;
	private String  pet_gender;
	private String  pet_content;
	private String  create_date;

	private String file_name; // 저장할 파일명
	private String file_oriname; // 원본 파일명
	private String file_url;

	public PetDto() {
		
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
	public int getPet_id() {
		return pet_id;
	}
	public void setPet_id(int pet_id) {
		this.pet_id = pet_id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getPet_name() {
		return pet_name;
	}
	public void setPet_name(String pet_name) {
		this.pet_name = pet_name;
	}
	public String getPet_kind() {
		return pet_kind;
	}
	public void setPet_kind(String pet_kind) {
		this.pet_kind = pet_kind;
	}
	public String getPet_gender() {
		return pet_gender;
	}
	public void setPet_gender(String pet_gender) {
		this.pet_gender = pet_gender;
	}
	public int getPet_age() {
		return pet_age;
	}
	public void setPet_age(int pet_age) {
		this.pet_age = pet_age;
	}
	public String getPet_content() {
		return pet_content;
	}
	public void setPet_content(String pet_content) {
		this.pet_content = pet_content;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	
	@Override
	public String toString() {
		return "Pet [pet_id=" + pet_id + ", user_id=" + user_id + ", pet_name=" + pet_name + ", pet_kind=" + pet_kind
				+ ", pet_gender=" + pet_gender + ", pet_age=" + pet_age + ", pet_content=" + pet_content
				+ ", create_date=" + create_date + "]";
	}
	
	
}
