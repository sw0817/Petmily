package com.petmily.curation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petmily.curation.dto.RecentChatDto;
import com.petmily.curation.dto.RoomDto;
import com.petmily.curation.mapper.RecentChatMapper;

@Service
public class RecentChatServiceImpl implements RecentChatService {

	@Autowired
	RecentChatMapper recentChatMapper;

	@Override
	public void createColumn(RecentChatDto recentChatDto) {
		recentChatMapper.createColumn(recentChatDto);
	}

	@Override
	public void updateColumn(RecentChatDto recentChatDto) {
		recentChatMapper.updateColumn(recentChatDto);
	}

	@Override
	public int isExistRoomId(RecentChatDto recentChatDto) {
		return recentChatMapper.isExistRoomId(recentChatDto);
	}

	@Override
	public List<RecentChatDto> getMyRecents(int my_id) {
		return recentChatMapper.getMyRecents(my_id);
	}

	@Override
	public void updateRecentChatId(RecentChatDto recentChatDto) {
		recentChatMapper.updateRecentChatId(recentChatDto);
		
	}

	@Override
	public void recentIsreadTrue(int recentchat_id) {
		recentChatMapper.recentIsreadTrue(recentchat_id);
	}

	@Override
	public boolean deleteRecentChatByUserId(int user_id) {
		return recentChatMapper.deleteRecentChatByUserId(user_id);
	}
}
