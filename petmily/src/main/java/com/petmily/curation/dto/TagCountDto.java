package com.petmily.curation.dto;

public class TagCountDto {
	private String tag_content;
	private int count;
	public String getTag_content() {
		return tag_content;
	}
	public void setTag_content(String tag_content) {
		this.tag_content = tag_content;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	@Override
	public String toString() {
		return "TagCountDto [tag_content=" + tag_content + ", count=" + count + "]";
	}
	
	
}
