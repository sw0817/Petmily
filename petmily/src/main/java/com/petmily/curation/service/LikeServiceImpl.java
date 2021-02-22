package com.petmily.curation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petmily.curation.dto.LikeDto;
import com.petmily.curation.dto.LikeListDto;
import com.petmily.curation.mapper.LikeMapper;

@Service
public class LikeServiceImpl implements LikeService{

	@Autowired
	LikeMapper likeMapper;
	
	@Override
	public boolean insertLike(LikeDto like) throws Exception {
		return likeMapper.insertLike(like);
	}

	@Override
	public boolean deleteLike(LikeDto like) throws Exception {
		return likeMapper.deleteLike(like);
	}

	@Override
	public int countLike(int feed_id) throws Exception {
		return likeMapper.countLike(feed_id);
	}

	@Override
	public int checkLike(int user_id, int feed_id) throws Exception {
		return likeMapper.checkLike(user_id, feed_id);
	}

	@Override
	public List<LikeListDto> listLike() throws Exception {
		return likeMapper.listLike();
	}

	@Override
	public List<LikeDto> selectLike(int feed_id) throws Exception {
		return likeMapper.selectLike(feed_id);
	}

	@Override
	public boolean deleteLikeOne(int feed_id) throws Exception {
		return likeMapper.deleteLikeOne(feed_id);
	}

	@Override
	public boolean deleteLikeByUserId(int user_id) throws Exception {
		return likeMapper.deleteLikeByUserId(user_id);
	}
}
