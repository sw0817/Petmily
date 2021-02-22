package com.petmily.curation.dto;

public class FriendTagDto {
	private int pt_id;
	private int feed_id;
	private int friend_id;
	private String friend_nickname;
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
	public int getFriend_id() {
		return friend_id;
	}
	public void setFriend_id(int friend_id) {
		this.friend_id = friend_id;
	}
	public String getFriend_nickname() {
		return friend_nickname;
	}
	public void setFriend_nickname(String friend_nickname) {
		this.friend_nickname = friend_nickname;
	}
	@Override
	public String toString() {
		return "FriendTagDto [pt_id=" + pt_id + ", feed_id=" + feed_id + ", friend_id=" + friend_id
				+ ", friend_nickname=" + friend_nickname + "]";
	}
	
	
}
