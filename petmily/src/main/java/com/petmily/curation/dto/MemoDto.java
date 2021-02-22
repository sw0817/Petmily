package com.petmily.curation.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "MemoDto : 댓글 정보", description = "게시글내 댓글정보를 나타낸다.")
public class MemoDto {

	@ApiModelProperty(value = "댓글 번호")
	private int memo_id;
	@ApiModelProperty(value = "작성자 아이디")
	private int user_id;
	@ApiModelProperty(value = "댓글내용")
	private String comment;
	@ApiModelProperty(value = "댓글 작성일")
	private String create_date;
	@ApiModelProperty(value = "댓글을 포함하는 게시글 번호")
	private int feed_id;
	public int getMemo_id() {
		return memo_id;
	}
	public void setMemo_id(int memo_id) {
		this.memo_id = memo_id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public int getFeed_id() {
		return feed_id;
	}
	public void setFeed_id(int feed_id) {
		this.feed_id = feed_id;
	}
	@Override
	public String toString() {
		return "MemoDto [memo_id=" + memo_id + ", user_id=" + user_id + ", comment=" + comment + ", create_date="
				+ create_date + ", feed_id=" + feed_id + "]";
	}
	
	
}
	