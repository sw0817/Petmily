package com.petmily.curation.mapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.petmily.curation.dto.FollowDto;
@Mapper
public interface FollowMapper {
	public void follow(FollowDto follow);
	public void unfollow(FollowDto follow);
	public int isFollow(FollowDto follow);
	public List<FollowDto> followingList(int from_id);
	public List<FollowDto> followerList(int to_id);
	public int checkF4F(FollowDto follow);
	public void followNotF4F(FollowDto follow);
	public void followF4F(FollowDto follow);
	public void updateF4F(FollowDto follow);
	public void deleteF4F(FollowDto followDto);
	
	public boolean deleteAllFollow(int user_id) throws SQLException; // 탈퇴회원 삭제
	
}
