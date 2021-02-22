package com.petmily.curation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petmily.curation.dto.RoomDto;
import com.petmily.curation.mapper.RoomMapper;

@Service
public class RoomServiceImpl implements RoomService {

	@Autowired
	RoomMapper roomMapper;

	@Override
	public int isRoom(RoomDto roomDto) throws Exception {
		return roomMapper.isRoom(roomDto);
	}

	@Override
	public void createRoom(RoomDto roomDto) {
		roomMapper.createRoom(roomDto);
	}

	@Override
	public RoomDto sendRoomId(RoomDto roomDto) {
		return roomMapper.sendRoomId(roomDto);
	}

	@Override
	public boolean deleteRoom(int user_id) throws Exception {
		return roomMapper.deleteRoom(user_id);
	}
}
