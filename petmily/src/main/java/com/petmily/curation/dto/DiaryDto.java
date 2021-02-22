package com.petmily.curation.dto;

public class DiaryDto {
	
	private int diary_id;
	private int user_id;
	private String diary_title;
	private String diary_year;
	private String diary_month;
	private String diary_day;
	private String diary_content;
	private String diary_feeling;
	private String diary_weather;
	private String diary_activity;
	private String create_date;
	
	
	public String getDiary_title() {
		return diary_title;
	}
	public void setDiary_title(String diary_title) {
		this.diary_title = diary_title;
	}
	public int getDiary_id() {
		return diary_id;
	}
	public void setDiary_id(int diary_id) {
		this.diary_id = diary_id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getDiary_year() {
		return diary_year;
	}
	public void setDiary_year(String diary_year) {
		this.diary_year = diary_year;
	}
	public String getDiary_month() {
		return diary_month;
	}
	public void setDiary_month(String diary_month) {
		this.diary_month = diary_month;
	}
	public String getDiary_day() {
		return diary_day;
	}
	public void setDiary_day(String diary_day) {
		this.diary_day = diary_day;
	}
	public String getDiary_content() {
		return diary_content;
	}
	public void setDiary_content(String diary_content) {
		this.diary_content = diary_content;
	}
	public String getDiary_feeling() {
		return diary_feeling;
	}
	public void setDiary_feeling(String diary_feeling) {
		this.diary_feeling = diary_feeling;
	}
	public String getDiary_weather() {
		return diary_weather;
	}
	public void setDiary_weather(String diary_weather) {
		this.diary_weather = diary_weather;
	}
	public String getDiary_activity() {
		return diary_activity;
	}
	public void setDiary_activity(String diary_activity) {
		this.diary_activity = diary_activity;
	}

	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	@Override
	public String toString() {
		return "DiaryDto [diary_id=" + diary_id + ", user_id=" + user_id + ", diary_title=" + diary_title
				+ ", diary_year=" + diary_year + ", diary_month=" + diary_month + ", diary_day=" + diary_day
				+ ", diary_content=" + diary_content + ", diary_feeling=" + diary_feeling + ", diary_weather="
				+ diary_weather + ", diary_activity=" + diary_activity  + ", create_date="
				+ create_date + "]";
	}

	
	
}