package com.petmily.curation.dto;


//@JsonDeserialize(as = TagDto.class)
public class TagDto {
	private int tag_id;
	private int feed_id;
	private String tag_content;
	private String  create_date;
	
	public int getTag_id() {
		return tag_id;
	}

	public void setTag_id(int tag_id) {
		this.tag_id = tag_id;
	}

	public int getFeed_id() {
		return feed_id;
	}

	public void setFeed_id(int feed_id) {
		this.feed_id = feed_id;
	}

	public String getTag_content() {
		return tag_content;
	}

	public void setTag_content(String tag_content) {
		this.tag_content = tag_content;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	@Override
	public String toString() {
		return "TagDto [tag_id=" + tag_id + ", feed_id=" + feed_id + ", tag_content=" + tag_content + ", create_date="
				+ create_date + "]";
	}
	

}
