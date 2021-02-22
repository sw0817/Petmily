package com.petmily.curation.service;

import java.sql.SQLException;
import java.util.List;

import com.petmily.curation.dto.FeedAlertDto;
import com.petmily.curation.dto.FollowAlertDto;

public interface FeedAlertService {
	public void createTagAlert(FeedAlertDto alertDto) throws SQLException; //알림 생성
	public void createCommentAlert(FeedAlertDto alertDto) throws SQLException; //알림 생성
	public boolean deleteAlert(int alert_id) throws SQLException; //알림 삭제
	public List<FeedAlertDto> alertList(int user_id); //해당 사람의 알림 전부 보여주기
	public boolean clickFeedAlert(FeedAlertDto dto);
	public boolean deleteAllAlerts(int alert_to);
	public int checkExisted(FeedAlertDto alertDto);
	public void updateTagAlert(FeedAlertDto alertDto);
	
	public boolean deleteAlertUserId(int user_id) throws SQLException; // 탈퇴회원 알림 삭제
}
