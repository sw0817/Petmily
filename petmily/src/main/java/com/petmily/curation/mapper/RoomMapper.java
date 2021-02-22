package com.petmily.curation.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.petmily.curation.dto.RoomDto;

@Mapper
public interface RoomMapper {
	public int isRoom(RoomDto roomDto) throws Exception; //나와 상대방의 대화방이 이미 존재하는지 확인         
	public void createRoom(RoomDto roomDto);
	public RoomDto sendRoomId(RoomDto roomDto);
	public boolean deleteRoom(int user_id) throws Exception;
	
}
