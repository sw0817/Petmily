package com.petmily.curation.dto;


public class FileDto {
   private int f_id; // 파일 번호
   private int user_id; // 로그인한 user 아이디
   private int feed_id;
   private String file_type; 
   private String file_name; // 저장할 파일명
   private String filter_name;
   private String file_oriname; // 원본 파일명
   private String file_url;
   
   
   public String getFilter_name() {
      return filter_name;
   }
   public void setFilter_name(String filter_name) {
      this.filter_name = filter_name;
   }
   public String getFile_type() {
      return file_type;
   }
   public void setFile_type(String file_type) {
      this.file_type = file_type;
   }
   public int getF_id() {
      return f_id;
   }
   public void setF_id(int f_id) {
      this.f_id = f_id;
   }
   public int getUser_id() {
      return user_id;
   }
   public void setUser_id(int user_id) {
      this.user_id = user_id;
   }
   public int getFeed_id() {
      return feed_id;
   }
   public void setFeed_id(int feed_id) {
      this.feed_id = feed_id;
   }
   public String getFile_name() {
      return file_name;
   }
   public void setFile_name(String file_name) {
      this.file_name = file_name;
   }
   public String getFile_oriname() {
      return file_oriname;
   }
   public void setFile_oriname(String file_oriname) {
      this.file_oriname = file_oriname;
   }
   public String getFile_url() {
      return file_url;
   }
   public void setFile_url(String file_url) {
      this.file_url = file_url;
   }
@Override
public String toString() {
	return "FileDto [f_id=" + f_id + ", user_id=" + user_id + ", feed_id=" + feed_id + ", file_type=" + file_type
			+ ", file_name=" + file_name + ", filter_name=" + filter_name + ", file_oriname=" + file_oriname
			+ ", file_url=" + file_url + "]";
}
   
   
}