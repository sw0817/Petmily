package com.petmily.curation.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petmily.curation.dto.FollowDto;
import com.petmily.curation.mapper.FollowMapper;
@Service
public class FollowServiceImpl implements FollowService {

	@Autowired
	private FollowMapper followMapper;
	
	@Override
	public void follow(FollowDto follow) {
		followMapper.follow(follow);
	}

	@Override
	public void unfollow(FollowDto follow) {
		followMapper.unfollow(follow);
	}

	@Override
	public int isFollow(FollowDto follow) {
		return followMapper.isFollow(follow);
	}

	@Override
	public List<FollowDto> followingList(int from_id) {
		return followMapper.followingList(from_id);
	}

	@Override
	public List<FollowDto> followerList(int to_id) {
		return followMapper.followerList(to_id);
	}

	@Override
	public int checkF4F(FollowDto follow) {
		return followMapper.checkF4F(follow);
	}

	@Override
	public void followNotF4F(FollowDto follow) {
		followMapper.followNotF4F(follow);
	}

	@Override
	public void followF4F(FollowDto follow) {
		followMapper.followF4F(follow);
	}

	@Override
	public void updateF4F(FollowDto follow) {
		followMapper.updateF4F(follow);
	}

	@Override
	public void deleteF4F(FollowDto followDto) {
		followMapper.deleteF4F(followDto);
	}

	@Override
	public boolean deleteAllFollow(int user_id) throws SQLException {
		return followMapper.deleteAllFollow(user_id);
	}

	
}
