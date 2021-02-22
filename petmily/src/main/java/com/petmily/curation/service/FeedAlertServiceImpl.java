package com.petmily.curation.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petmily.curation.dto.FeedAlertDto;
import com.petmily.curation.mapper.FeedAlertMapper;

@Service
public class FeedAlertServiceImpl implements FeedAlertService{

	@Autowired
	FeedAlertMapper alertMapper;
	
	@Override
	public void createTagAlert(FeedAlertDto alertDto) throws SQLException {
		alertMapper.createTagAlert(alertDto);
	}
	
	@Override
	public void createCommentAlert(FeedAlertDto alertDto) throws SQLException {
		alertMapper.createCommentAlert(alertDto);
	}

	@Override
	public boolean deleteAlert(int alert_id) throws SQLException {
		return alertMapper.deleteAlert(alert_id);
	}

	@Override
	public List<FeedAlertDto> alertList(int user_id) {
		return alertMapper.alertList(user_id);
	}

	@Override
	public boolean clickFeedAlert(FeedAlertDto dto) {
		return alertMapper.clickFeedAlert(dto);
	}

	@Override
	public boolean deleteAllAlerts(int alert_to) {
		return alertMapper.deleteAllAlerts(alert_to);
	}

	@Override
	public int checkExisted(FeedAlertDto alertDto) {
		return alertMapper.checkExisted(alertDto);
	}

	@Override
	public void updateTagAlert(FeedAlertDto alertDto) {
		alertMapper.updateTagAlert(alertDto);
	}

	@Override
	public boolean deleteAlertUserId(int user_id) throws SQLException {
		return alertMapper.deleteAlertUserId(user_id);
	}
	
	

}
