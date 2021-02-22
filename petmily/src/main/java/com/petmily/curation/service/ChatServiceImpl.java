package com.petmily.curation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petmily.curation.dto.ChatDto;
import com.petmily.curation.mapper.ChatMapper;

@Service
public class ChatServiceImpl implements ChatService {

	@Autowired
	ChatMapper chatMapper;
	
	@Override
	public void doAddMessage(ChatDto chatDto) {
		chatMapper.doAddMessage(chatDto);
	}

	@Override
	public List<ChatDto> getAllMessages() {
		System.out.println("여기 실행됨");
		return chatMapper.getAllMessages();
	}

	@Override
	public List<ChatDto> getAllMessagesFrom(int my_id, int your_id, int from_chat_id) {
		return chatMapper.getAllMessagesFrom(my_id,your_id,from_chat_id);
	}

	@Override
	public int startChatId(int room_id) {
		return chatMapper.startChatId(room_id);
	}

	@Override
	public void readTrue(int my_id, int your_id, int from_chat_id) {
		chatMapper.readTrue(my_id,your_id, from_chat_id);
		
	}

	@Override
	public boolean deleteChatByUserId(int user_id) {
		return chatMapper.deleteChatByUserId(user_id);
	}

}
