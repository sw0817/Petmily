package com.petmily.curation.dto;

public class PetTagDto {
	private int pt_id;  // PK
	private int feed_id;
	private int pet_id;
	private String pet_name;
	
	public int getPt_id() {
		return pt_id;
	}
	public void setPt_id(int pt_id) {
		this.pt_id = pt_id;
	}
	public int getFeed_id() {
		return feed_id;
	}
	public void setFeed_id(int feed_id) {
		this.feed_id = feed_id;
	}
	public int getPet_id() {
		return pet_id;
	}
	public void setPet_id(int pet_id) {
		this.pet_id = pet_id;
	}
	public String getPet_name() {
		return pet_name;
	}
	public void setPet_name(String pet_name) {
		this.pet_name = pet_name;
	}
	@Override
	public String toString() {
		return "PetTagDto [pt_id=" + pt_id + ", feed_id=" + feed_id + ", pet_id=" + pet_id + ", pet_name=" + pet_name
				+ "]";
	}
		
	
	
}
