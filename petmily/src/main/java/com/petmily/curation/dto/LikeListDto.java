package com.petmily.curation.dto;

public class LikeListDto {
	private int feed_id;
	private int user_id;
	private String user_nickname;
	public int getFeed_id() {
		return feed_id;
	}
	public void setFeed_id(int feed_id) {
		this.feed_id = feed_id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getUser_nickname() {
		return user_nickname;
	}
	public void setUser_nickname(String user_nickname) {
		this.user_nickname = user_nickname;
	}
	@Override
	public String toString() {
		return "LikeListDto [feed_id=" + feed_id + ", user_id=" + user_id + ", user_nickname=" + user_nickname + "]";
	}
	
	
}
