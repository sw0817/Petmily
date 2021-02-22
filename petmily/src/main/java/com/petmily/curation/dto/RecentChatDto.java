package com.petmily.curation.dto;
//0205

public class RecentChatDto {
	private int recentchat_id;
	private int room_id;
	private int send_id;
	private String send_nickname;
	private int receive_id;
	private String receive_nickname;
	private String body;
	private int chat_id;
	private boolean isread;
	
	
	public boolean isIsread() {
		return isread;
	}
	public void setIsread(boolean isread) {
		this.isread = isread;
	}
	public int getChat_id() {
		return chat_id;
	}
	public void setChat_id(int chat_id) {
		this.chat_id = chat_id;
	}
	public int getRecentchat_id() {
		return recentchat_id;
	}
	public void setRecentchat_id(int recentchat_id) {
		this.recentchat_id = recentchat_id;
	}
	public int getRoom_id() {
		return room_id;
	}
	public void setRoom_id(int room_id) {
		this.room_id = room_id;
	}
	public int getSend_id() {
		return send_id;
	}
	public void setSend_id(int send_id) {
		this.send_id = send_id;
	}
	public String getSend_nickname() {
		return send_nickname;
	}
	public void setSend_nickname(String send_nickname) {
		this.send_nickname = send_nickname;
	}
	public int getReceive_id() {
		return receive_id;
	}
	public void setReceive_id(int receive_id) {
		this.receive_id = receive_id;
	}
	public String getReceive_nickname() {
		return receive_nickname;
	}
	public void setReceive_nickname(String receive_nickname) {
		this.receive_nickname = receive_nickname;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	@Override
	public String toString() {
		return "RecentChatDto [recentchat_id=" + recentchat_id + ", room_id=" + room_id + ", send_id=" + send_id
				+ ", send_nickname=" + send_nickname + ", receive_id=" + receive_id + ", receive_nickname="
				+ receive_nickname + ", body=" + body + ", chat_id=" + chat_id + ", isread=" + isread + "]";
	}
	
}
