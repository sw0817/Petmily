package com.petmily.curation.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.petmily.curation.dto.ChatDto;
import com.petmily.curation.dto.RecentChatDto;
import com.petmily.curation.dto.RoomDto;
import com.petmily.curation.service.ChatService;
import com.petmily.curation.service.RecentChatService;
import com.petmily.curation.service.RoomService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/chat")
public class ChatController {
	
	@Autowired
	ChatService chatService;
	
	@Autowired
	RoomService roomService;
	
	@Autowired
	RecentChatService recentChatService;
	
	@PostMapping("isroom")
	public RoomDto isRoom(@RequestBody RoomDto roomDto) throws Exception{ //방이 존재하는지 확인
		
		int cnt = roomService.isRoom(roomDto);
		if(cnt==0) { //방이 존재하지 않으면! 방을 만들어줌~
			roomService.createRoom(roomDto); //넘어온 my_id와 your_id로 방 만들기!
		}
		
		return roomService.sendRoomId(roomDto);
	}
	
	@PostMapping("doAddMessage")
	public void doAddMessage(@RequestBody ChatDto chatDto) {
		chatService.doAddMessage(chatDto);
		
		RecentChatDto recentChatDto = new RecentChatDto();
		recentChatDto.setRoom_id(chatDto.getRoom_id());
		recentChatDto.setSend_id(chatDto.getSend_id());
		recentChatDto.setSend_nickname(chatDto.getSend_nickname());
		recentChatDto.setReceive_id(chatDto.getReceive_id());
		recentChatDto.setReceive_nickname(chatDto.getReceive_nickname());
		recentChatDto.setBody(chatDto.getBody());
		recentChatDto.setChat_id(chatDto.getChat_id());
		recentChatDto.setIsread(chatDto.isIsread());
		//1. room_id가 존재하는지 먼저 확인
		int isExistRoomId = recentChatService.isExistRoomId(recentChatDto);
		//System.out.println(isExistRoomId);
		if(isExistRoomId==0) { //존재 안하면 insert
			recentChatService.createColumn(recentChatDto);
		}else { //존재하면 update
			recentChatService.updateColumn(recentChatDto);
		}
		//update recentchat의 chat_id
		System.out.println(recentChatDto.getSend_id()+" "+recentChatDto.getReceive_id());
		recentChatService.updateRecentChatId(recentChatDto);
	}
	
	@PostMapping("startChatId/{room_id}")
	public int startChatId(@PathVariable int room_id) {
		int num = chatService.startChatId(room_id);
		return num;
	}

	@RequestMapping("/getAllMessagesFrom/{from_chat_id}") //해당 room에 작성된 채팅의 개수..
	public List<ChatDto> getAllMessagesFrom(@RequestBody ChatDto chatDto, @PathVariable int from_chat_id) {
		int my_id=chatDto.getSend_id();
		int your_id=chatDto.getReceive_id();
		List<ChatDto> list = new ArrayList<ChatDto>();
		chatService.readTrue(my_id,your_id,from_chat_id);
		//메시지 리스트 보내기
		list = chatService.getAllMessagesFrom(my_id,your_id,from_chat_id);
		//System.out.println(list);
		return list;
	}
	
	//my_id를 기준으로 채팅의 최근 목록들을 불러옴!!! -> 채팅방 목록 페이지...
	@PostMapping("mychatlist/{my_id}")
	public List<RecentChatDto> getMyRecents(@PathVariable int my_id){
		List<RecentChatDto> list = new ArrayList<RecentChatDto>();
		list = recentChatService.getMyRecents(my_id);
		return list;
	}
	
	@PostMapping("recentIsreadTrue/{recentchat_id}")
	public void recentIsreadTrue(@PathVariable int recentchat_id) {
		recentChatService.recentIsreadTrue(recentchat_id);
	}
	
	// http://localhost:8000/chat/deleteroomuserid/5
	@ApiOperation(value = "탈퇴회원 room삭제", notes = "탈퇴회원과 관련된 모든 room 삭제", response = List.class)
	@DeleteMapping("deleteroomuserid/{user_id}")
	public boolean deleteRoomUserId(@PathVariable(name = "user_id") String user_id) throws Exception {
		boolean result=roomService.deleteRoom(Integer.parseInt(user_id));
		return result;
	}
	
	// http://localhost:8000/chat/deletechat/5
	@ApiOperation(value = "탈퇴회원 chat삭제", notes = "탈퇴회원과 관련된 모든 chat 삭제", response = List.class)
	@DeleteMapping("deletechat/{user_id}")
	public boolean deleteChatUserId(@PathVariable(name = "user_id") String user_id) throws Exception {
		boolean result=chatService.deleteChatByUserId(Integer.parseInt(user_id));
		return result;
	}
	
	// http://localhost:8000/chat/deleterecentchat/5
	@ApiOperation(value = "탈퇴회원 recentchat삭제", notes = "탈퇴회원과 관련된 모든 recentchat 삭제", response = List.class)
	@DeleteMapping("deleterecentchat/{user_id}")
	public boolean deleteRecentChatUserId(@PathVariable(name = "user_id") String user_id) throws Exception {
		boolean result=recentChatService.deleteRecentChatByUserId(Integer.parseInt(user_id));
		return result;
	}

}
