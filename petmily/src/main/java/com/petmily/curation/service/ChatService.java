package com.petmily.curation.service;

import java.util.List;

import com.petmily.curation.dto.ChatDto;

public interface ChatService {
	public void doAddMessage(ChatDto chatDto);
	public List<ChatDto> getAllMessages();
	public List<ChatDto> getAllMessagesFrom(int my_id, int your_id, int from_chat_id);
	public int startChatId(int room_id);
	public void readTrue(int my_id, int your_id,int from_chat_id);
	public boolean deleteChatByUserId(int user_id); // 탈퇴회원 관련 채팅 삭제
	
}
