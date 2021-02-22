package com.petmily.curation.dto;

public class FollowDto {
	private int follow_id;
	private int from_id;	// 팔로우 거는 사람 (following)
	private int to_id;		// 팔로우 당한 사람(follower)
	private String follow_date;
	private String from_nickname;
	private String to_nickname;
	private boolean f4f; // 맞팔여부
	
	public int getFollow_id() {
		return follow_id;
	}
	public void setFollow_id(int follow_id) {
		this.follow_id = follow_id;
	}
	public int getFrom_id() {
		return from_id;
	}
	public void setFrom_id(int from_id) {
		this.from_id = from_id;
	}
	public int getTo_id() {
		return to_id;
	}
	public void setTo_id(int to_id) {
		this.to_id = to_id;
	}
	public String getFollow_date() {
		return follow_date;
	}
	public void setFollow_date(String follow_date) {
		this.follow_date = follow_date;
	}
	public String getFrom_nickname() {
		return from_nickname;
	}
	public void setFrom_nickname(String from_nickname) {
		this.from_nickname = from_nickname;
	}
	public String getTo_nickname() {
		return to_nickname;
	}
	public void setTo_nickname(String to_nickname) {
		this.to_nickname = to_nickname;
	}
	public boolean isF4f() {
		return f4f;
	}
	public void setF4f(boolean f4f) {
		this.f4f = f4f;
	}
	@Override
	public String toString() {
		return "FollowDto [follow_id=" + follow_id + ", from_id=" + from_id + ", to_id=" + to_id + ", follow_date="
				+ follow_date + ", from_nickname=" + from_nickname + ", to_nickname=" + to_nickname + ", f4f=" + f4f
				+ "]";
	}

}
	