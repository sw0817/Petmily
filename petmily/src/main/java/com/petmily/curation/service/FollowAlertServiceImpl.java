package com.petmily.curation.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petmily.curation.dto.FollowAlertDto;
import com.petmily.curation.mapper.FollowAlertMapper;

@Service
public class FollowAlertServiceImpl implements FollowAlertService{

	@Autowired
	FollowAlertMapper alertMapper;
	
	@Override
	public void createAlert(FollowAlertDto alertDto) throws SQLException {
		alertMapper.createAlert(alertDto);
	}

	@Override
	public boolean deleteAlert(int alert_id) throws SQLException {
		return alertMapper.deleteAlert(alert_id);
	}

	@Override
	public List<FollowAlertDto> alertList(int user_id) {
		return alertMapper.alertList(user_id);
	}

	@Override
	public boolean deleteAllAlerts(int alert_to) {
		return alertMapper.deleteAllAlerts(alert_to);
	}

	@Override
	public int checkExisted(FollowAlertDto alertDto) {
		return alertMapper.checkExisted(alertDto);
	}

	@Override
	public boolean deleteAlertUserId(int user_id) throws SQLException {
		return alertMapper.deleteAlertUserId(user_id);
	}
}
