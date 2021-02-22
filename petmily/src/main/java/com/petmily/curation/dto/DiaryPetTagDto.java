package com.petmily.curation.dto;

public class DiaryPetTagDto {
	private int dpt_id;  // PK
	private int user_id;
	private int diary_id;
	private int pet_id;
	
	
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getDpt_id() {
		return dpt_id;
	}
	public void setDpt_id(int dpt_id) {
		this.dpt_id = dpt_id;
	}
	public int getDiary_id() {
		return diary_id;
	}
	public void setDiary_id(int diary_id) {
		this.diary_id = diary_id;
	}
	public int getPet_id() {
		return pet_id;
	}
	public void setPet_id(int pet_id) {
		this.pet_id = pet_id;
	}
	@Override
	public String toString() {
		return "DiaryPetTagDto [dpt_id=" + dpt_id + ", user_id=" + user_id + ", diary_id=" + diary_id + ", pet_id="
				+ pet_id + "]";
	}

	
	
}
