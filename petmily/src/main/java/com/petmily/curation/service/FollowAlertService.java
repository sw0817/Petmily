package com.petmily.curation.service;

import java.sql.SQLException;
import java.util.List;

import com.petmily.curation.dto.FollowAlertDto;

public interface FollowAlertService {
	public void createAlert(FollowAlertDto alertDto) throws SQLException; //알림 생성
	public boolean deleteAlert(int alert_id) throws SQLException; //알림 삭제
	public List<FollowAlertDto> alertList(int user_id); //해당 사람의 알림 전부 보여주기
	public boolean deleteAllAlerts(int alert_to); //모두지우기
	public int checkExisted(FollowAlertDto alertDto);
	
	public boolean deleteAlertUserId(int user_id) throws SQLException; // 탈퇴회원알림 삭제
}
