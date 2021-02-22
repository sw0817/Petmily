package com.petmily.curation.dto;

public class CommentDto {
	private int comment_id;
	private int feed_id;
	private int praentcomment_id;
	private String comment_content;
	private int writer_id;
	private String comment_date;
	private String comment_isdeleted;
	
	
	public CommentDto(int comment_id, int feed_id, int praentcomment_id, String comment_content, int writer_id,
			String comment_date, String comment_isdeleted) {
		super();
		this.comment_id = comment_id;
		this.feed_id = feed_id;
		this.praentcomment_id = praentcomment_id;
		this.comment_content = comment_content;
		this.writer_id = writer_id;
		this.comment_date = comment_date;
		this.comment_isdeleted = comment_isdeleted;
	}
	public int getComment_id() {
		return comment_id;
	}
	public void setComment_id(int comment_id) {
		this.comment_id = comment_id;
	}
	public int getFeed_id() {
		return feed_id;
	}
	public void setFeed_id(int feed_id) {
		this.feed_id = feed_id;
	}
	public int getPraentcomment_id() {
		return praentcomment_id;
	}
	public void setPraentcomment_id(int praentcomment_id) {
		this.praentcomment_id = praentcomment_id;
	}
	public String getComment_content() {
		return comment_content;
	}
	public void setComment_content(String comment_content) {
		this.comment_content = comment_content;
	}
	public int getWriter_id() {
		return writer_id;
	}
	public void setWriter_id(int writer_id) {
		this.writer_id = writer_id;
	}
	public String getComment_date() {
		return comment_date;
	}
	public void setComment_date(String comment_date) {
		this.comment_date = comment_date;
	}
	public String getComment_isdeleted() {
		return comment_isdeleted;
	}
	public void setComment_isdeleted(String comment_isdeleted) {
		this.comment_isdeleted = comment_isdeleted;
	}
	
	@Override
	public String toString() {
		return "CommentDto [comment_id=" + comment_id + ", feed_id=" + feed_id + ", praentcomment_id="
				+ praentcomment_id + ", comment_content=" + comment_content + ", writer_id=" + writer_id
				+ ", comment_date=" + comment_date + ", comment_isdeleted=" + comment_isdeleted + "]";
	}
	
	
}
