package com.petmily.curation.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.petmily.curation.dto.ChatDto;

@Mapper
public interface ChatMapper {

   public void doAddMessage(ChatDto chatDto);
   public List<ChatDto> getAllMessages();
   public List<ChatDto> getAllMessagesFrom(@Param("my_id") int my_id, @Param("your_id") int your_id, @Param("from_chat_id") int from_chat_id);
   public int startChatId(int room_id);
   public void readTrue(@Param("my_id") int my_id, @Param("your_id") int your_id, @Param("from_chat_id") int from_chat_id);
   public boolean deleteChatByUserId(int user_id); // 탈퇴회원 관련 채팅 삭제
	
}