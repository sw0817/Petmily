package com.petmily.curation.dto;
//팔로우 관련 alert
public class FollowAlertDto {
	private int alert_id;
	private int alert_to;
	private String create_date;
	private int from_id;
	private String from_nickname;
	private String alert_content;
	public int getAlert_id() {
		return alert_id;
	}
	public void setAlert_id(int alert_id) {
		this.alert_id = alert_id;
	}
	public int getAlert_to() {
		return alert_to;
	}
	public void setAlert_to(int alert_to) {
		this.alert_to = alert_to;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public int getFrom_id() {
		return from_id;
	}
	public void setFrom_id(int from_id) {
		this.from_id = from_id;
	}
	public String getFrom_nickname() {
		return from_nickname;
	}
	public void setFrom_nickname(String from_nickname) {
		this.from_nickname = from_nickname;
	}
	public String getAlert_content() {
		return alert_content;
	}
	public void setAlert_content(String alert_content) {
		this.alert_content = alert_content;
	}
	@Override
	public String toString() {
		return "FollowAlertDto [alert_id=" + alert_id + ", alert_to=" + alert_to + ", create_date=" + create_date
				+ ", from_id=" + from_id + ", from_nickname=" + from_nickname + ", alert_content=" + alert_content
				+ "]";
	}

	
}
