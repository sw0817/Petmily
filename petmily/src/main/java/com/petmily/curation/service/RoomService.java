package com.petmily.curation.service;

import com.petmily.curation.dto.RoomDto;

public interface RoomService {
	public int isRoom(RoomDto roomDto) throws Exception; //나와 상대방의 대화방이 이미 존재하는지 확인        
	public void createRoom(RoomDto roomDto); //my_id와 your_id로 방 만들기
	public RoomDto sendRoomId(RoomDto roomDto); //room_id를 전송
	public boolean deleteRoom(int user_id) throws Exception;
}
