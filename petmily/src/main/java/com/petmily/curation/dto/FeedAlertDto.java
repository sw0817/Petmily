package com.petmily.curation.dto;
//피드 관련 alert
public class FeedAlertDto {
	private int alert_id;
	private int alert_to;
	private String create_date;
	private int alert_kind; //1이면 게시글에서 친구태그, 2면 내 게시글에 댓글 알림
	private int from_id; //~님이 언급했습니다~ 할때 "~님"의 아이디
	private String from_nickname;
	private String alert_content;
	private int feed_id; //나를 언급한 게시물 or 댓글이 달린 내가 올린 게시물
	private String feed_content; //나를 언급한 게시물의 내용이 뭔지
	private int memo_id; //나를 언급한 게시물 or 댓글이 달린 내가 올린 게시물
	private String comment; //내 게시물에 달린 댓글
	
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
	public int getAlert_kind() {
		return alert_kind;
	}
	public void setAlert_kind(int alert_kind) {
		this.alert_kind = alert_kind;
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
	public int getFeed_id() {
		return feed_id;
	}
	public void setFeed_id(int feed_id) {
		this.feed_id = feed_id;
	}
	public String getFeed_content() {
		return feed_content;
	}
	public void setFeed_content(String feed_content) {
		this.feed_content = feed_content;
	}
	public int getMemo_id() {
		return memo_id;
	}
	public void setMemo_id(int memo_id) {
		this.memo_id = memo_id;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	@Override
	public String toString() {
		return "FeedAlertDto [alert_id=" + alert_id + ", alert_to=" + alert_to + ", create_date=" + create_date
				+ ", alert_kind=" + alert_kind + ", from_id=" + from_id + ", from_nickname=" + from_nickname
				+ ", alert_content=" + alert_content + ", feed_id=" + feed_id + ", feed_content=" + feed_content
				+ ", memo_id=" + memo_id + ", comment=" + comment + "]";
	}

}
