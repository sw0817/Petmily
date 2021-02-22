package com.petmily.curation.mapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.petmily.curation.dto.FeedAlertDto;

@Mapper
public interface FeedAlertMapper {
	public void createTagAlert(FeedAlertDto alertDto) throws SQLException; //알림 생성
	public void createCommentAlert(FeedAlertDto alertDto);
	public boolean deleteAlert(int alert_id) throws SQLException; //알림 삭제
	public List<FeedAlertDto> alertList(int user_id); //해당 사람의 알림 전부 보여주기
	public boolean clickFeedAlert(FeedAlertDto dto);
	public boolean deleteAllAlerts(int alert_to);
	public int checkExisted(FeedAlertDto alertDto);
	public void updateTagAlert(FeedAlertDto alertDto);
	
	public boolean deleteAlertUserId(int user_id) throws SQLException; // 탈퇴회원 알림 삭제

}
