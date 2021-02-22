package com.petmily.curation.dto;

public class FeedDto {
   private int feed_id;
   private int user_id;
   private String feed_date;
   private String feed_content;
   
   public int getFeed_id() {
	   return feed_id;
   }
   public void setFeed_id(int feed_id) {
	   this.feed_id = feed_id;
   }
   public int getUser_id() {
	   return user_id;
   }
   public void setUser_id(int user_id) {
	   this.user_id = user_id;
   }
   public String getFeed_date() {
	   return feed_date;
   }
   public void setFeed_date(String feed_date) {
	   this.feed_date = feed_date;
   }
   public String getFeed_content() {
	   return feed_content;
   }
   public void setFeed_content(String feed_content) {
	   this.feed_content = feed_content;
   }
@Override
public String toString() {
	return "FeedDto [feed_id=" + feed_id + ", user_id=" + user_id + ", feed_date=" + feed_date + ", feed_content="
			+ feed_content + "]";
}
   
   
   
   
}