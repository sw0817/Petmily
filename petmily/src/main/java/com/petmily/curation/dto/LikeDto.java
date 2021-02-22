package com.petmily.curation.dto;

public class LikeDto {
	private int like_id;
	private int user_id;
	private int feed_id;
	

	public int getlike_id() {
		return like_id;
	}
	public void setlike_id(int like_id) {
		this.like_id = like_id;
	}
	public int getuser_id() {
		return user_id;
	}
	public void setuser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getFeed_id() {
		return feed_id;
	}
	public void setFeed_id(int feed_id) {
		this.feed_id = feed_id;
	}
	
	@Override
	public String toString() {
		return "LikeyDto [like_id=" + like_id + ", user_id=" + user_id + ", feed_id=" + feed_id +  "]";
	}
	
	
}
