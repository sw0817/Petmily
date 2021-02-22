package com.petmily.curation.dto;
//0205

public class RoomDto {
	private int room_id;
	private int my_id;
	private int your_id;
	public int getRoom_id() {
		return room_id;
	}
	public void setRoom_id(int room_id) {
		this.room_id = room_id;
	}
	public int getMy_id() {
		return my_id;
	}
	public void setMy_id(int my_id) {
		this.my_id = my_id;
	}
	public int getYour_id() {
		return your_id;
	}
	public void setYour_id(int your_id) {
		this.your_id = your_id;
	}
	@Override
	public String toString() {
		return "RoomDto [room_id=" + room_id + ", my_id=" + my_id + ", your_id=" + your_id + "]";
	}

	
}
