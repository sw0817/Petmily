package com.petmily.curation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.petmily.curation.dto.FeedAlertDto;
import com.petmily.curation.dto.FollowAlertDto;
import com.petmily.curation.service.FeedAlertService;
import com.petmily.curation.service.FollowAlertService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("AlertController V1")
@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/alert")
public class AlertController {
	
	@Autowired
	FollowAlertService alertService;
	
	@Autowired
	FeedAlertService feedAlertService;
	

	@ApiOperation(value = "팔로우 알람 삭제", notes = "알람 확인버튼을 클릭하면 알람테이블에서 지워진다.", response = List.class)
	@DeleteMapping("deletealert/{alert_id}")
	public boolean deleteAlert(@PathVariable(name = "alert_id") String alert_id) throws Exception {
		System.out.println("알람 삭제");
		boolean result=alertService.deleteAlert(Integer.parseInt(alert_id));
		return result==true;
	}
	
	@ApiOperation(value = "팔로우 알람 내용", notes = "나와 관련된 팔로우 알람을 모두 출력한다.", response = List.class)
	@PostMapping("{user_id}")
	public List<FollowAlertDto> alertList(@PathVariable("user_id") int user_id) throws NumberFormatException, Exception {
		System.out.println("알람 내용");
	    List<FollowAlertDto> result = alertService.alertList(user_id);
	    return result;
	}
	
	@ApiOperation(value = "피드 알람 내용", notes = "나와 관련된 피드 알람을 모두 출력한다.", response = List.class)
	@PostMapping("feed/{user_id}")
	public List<FeedAlertDto> FeedalertList(@PathVariable("user_id") int user_id) throws NumberFormatException, Exception {
		System.out.println("알람 내용");
	    List<FeedAlertDto> result = feedAlertService.alertList(user_id);
	    return result;
	}
	
	@ApiOperation(value = "피드 알람 삭제", notes = "알람 확인버튼을 클릭하면 알람테이블에서 지워진다.", response = List.class)
	@DeleteMapping("feeddeletealert/{alert_id}")
	public boolean FeeddeleteAlert(@PathVariable(name = "alert_id") String alert_id) throws Exception {
		System.out.println("알람 삭제");
		boolean result=feedAlertService.deleteAlert(Integer.parseInt(alert_id));
		return result==true;
	}
	
	@ApiOperation(value = "해당 피드 관련 알람 모두 삭제", notes = "피드 알람을 클릭하면 그 피드와 관련된 모든 알람이 알람테이블에서 지워진다.", response = List.class)
	@DeleteMapping("clickFeedAlert/{feed_id}/{alert_to}")
	public boolean clickFeedAlert(@PathVariable int feed_id,@PathVariable int alert_to) throws Exception {
		System.out.println("해당 피드 관련 알람 모두 삭제");
		FeedAlertDto dto = new FeedAlertDto();
		dto.setFeed_id(feed_id);
		dto.setAlert_to(alert_to);
		boolean result=feedAlertService.clickFeedAlert(dto);
		return result==true;
	}
	
	@ApiOperation(value = "모든 알람 삭제", notes = "모두지우기를 클릭하면 해당 아이디로 저장된 모든 알람이 알람테이블에서 지워진다.", response = List.class)
	@DeleteMapping("deleteall/{alert_to}")
	public boolean deleteall(@PathVariable int alert_to) throws Exception {
		System.out.println("알람 모두지우기");
		boolean result=alertService.deleteAllAlerts(alert_to);
		if(result) {
			result=feedAlertService.deleteAllAlerts(alert_to);
		}
		return result==true;
	}
	
	// http://localhost:8000/alert/feeddeletealertuserid/5
	@ApiOperation(value = "피드 알람 삭제", notes = "탈퇴회원과 관련된 모든 알람 삭제", response = List.class)
	@DeleteMapping("feeddeletealertuserid/{user_id}")
	public boolean FeeddeleteAlertUserId(@PathVariable(name = "user_id") String user_id) throws Exception {
		System.out.println("탈퇴회원 피드알람 삭제");
		boolean result=feedAlertService.deleteAlertUserId(Integer.parseInt(user_id));
		return result==true;
	}
	
	
	// http://localhost:8000/alert/deletealertuserid/5
	@ApiOperation(value = "팔로우 알람 삭제", notes = "탈퇴회원과 관련된 모든 알람 삭제", response = List.class)
	@DeleteMapping("deletealertuserid/{user_id}")
	public boolean deleteAlertUserId(@PathVariable(name = "user_id") String user_id) throws Exception {
		System.out.println("탈퇴회원 팔로우알람 삭제");
		boolean result=alertService.deleteAlertUserId(Integer.parseInt(user_id));
		return result==true;
	}
	
}
