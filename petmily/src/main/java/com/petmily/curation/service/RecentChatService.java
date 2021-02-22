package com.petmily.curation.service;

import java.util.List;

import com.petmily.curation.dto.RecentChatDto;

public interface RecentChatService {
	public int isExistRoomId(RecentChatDto recentChatDto);
	public void createColumn(RecentChatDto recentChatDto); //room_id를 가진 칼럼이 없으면 생성
	public void updateColumn(RecentChatDto recentChatDto); //room_id를 가진 칼럼이 존재한다면 업데이트
	public List<RecentChatDto> getMyRecents(int my_id); //나의 채팅방 목록!!!!
	public void updateRecentChatId(RecentChatDto recentChatDto);
	public void recentIsreadTrue(int recentchat_id); 
	
	public boolean deleteRecentChatByUserId(int user_id); // 탈퇴회원 관련 채팅 삭제

}
