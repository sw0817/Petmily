package com.petmily.curation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petmily.curation.dto.FeedDto;
import com.petmily.curation.dto.FriendTagDto;
import com.petmily.curation.dto.PetTagDto;
import com.petmily.curation.dto.TagCountDto;
import com.petmily.curation.dto.TagDto;
import com.petmily.curation.mapper.FeedMapper;

@Service
public class FeedServiceImpl implements FeedService{

	@Autowired
	FeedMapper feedMapper;
	
	@Override
	public int insertFeed(FeedDto feed) throws Exception {
		return feedMapper.insertFeed(feed);
	}

	@Override
	public List<FeedDto> selectFeed(int id) throws Exception {
		return feedMapper.selectFeed(id);
	}

	@Override
	public boolean updateFeed(FeedDto feed) throws Exception {
		return feedMapper.updateFeed(feed);
	}

	@Override
	public boolean deleteFeed(int id) throws Exception {
		return feedMapper.deleteFeed(id);
	}

	@Override
	public List<FeedDto> selectFeedAll() throws Exception {
		return feedMapper.selectFeedAll();
	}
	
	
	@Override
	public void registerPetTags(PetTagDto pet) throws Exception {
		feedMapper.registerPetTags(pet);
	}

	@Override
	public void registerTags(int feed_id, String tag_content) throws Exception {
		feedMapper.registerTags(feed_id, tag_content);
	}

	@Override
	public List<TagDto> selectAllTags() throws Exception {
		return feedMapper.selectAllTags();
	}

	@Override
	public List<PetTagDto> selectAllPetTags() throws Exception {
		return feedMapper.selectAllPetTags();
	}

	@Override
	public FeedDto selectFeedOne(int feed_id) throws Exception {
		return feedMapper.selectFeedOne(feed_id);
	}

	@Override
	public List<TagDto> selectTag(int feed_id) throws Exception {
		return feedMapper.selectTag(feed_id);
	}

	@Override
	public List<PetTagDto> selectPetTag(int feed_id) throws Exception {
		return feedMapper.selectPetTag(feed_id);
	}

	@Override
	public List<FriendTagDto> selectFriendTag(int feed_id) throws Exception {
		return feedMapper.selectFriendTag(feed_id);
	}

	@Override
	public void registerFriendTags(FriendTagDto friend) throws Exception {
		feedMapper.registerFriendTags(friend);
	}

	@Override
	public List<FriendTagDto> selectAllFriendTag() throws Exception {
		return feedMapper.selectAllFriendTag();
	}

	@Override
	public boolean updateTags(TagDto tag) throws Exception {
		return feedMapper.updateTags(tag);
	}

	@Override
	public boolean deleteTags(int feed_id) throws Exception {
		return feedMapper.deleteTags(feed_id);
	}

	@Override
	public boolean updatePetTags(PetTagDto pettag) throws Exception {
		return feedMapper.updatePetTags(pettag);
	}

	@Override
	public boolean deletePetTags(int feed_id) throws Exception {
		return feedMapper.deletePetTags(feed_id);
	}

	@Override
	public boolean updateFriendTags(FriendTagDto friendtag) throws Exception {
		return feedMapper.updateFriendTags(friendtag);
	}

	@Override
	public boolean deleteFriendTags(int feed_id) throws Exception {
		return feedMapper.deleteFriendTags(feed_id);
	}

	@Override
	public List<TagCountDto> allCount() throws Exception {
		return feedMapper.allCount();
	}

	@Override
	public boolean deletePetTagsByPet(int pet_id) throws Exception {
		return feedMapper.deletePetTagsByPet(pet_id);
	}

	@Override
	public boolean deleteAllFeedUserId(int user_id) throws Exception {
		return feedMapper.deleteAllFeedUserId(user_id);
	}

	@Override
	public boolean deleteAllFriend(int user_id) throws Exception {
		return feedMapper.deleteAllFriend(user_id);
	}

	@Override
	public boolean deleteAllPettags() throws Exception {
		return feedMapper.deleteAllPettags();
	}

	@Override
	public boolean deleteAlltags() throws Exception {
		return feedMapper.deleteAlltags();
	}
	
	
	
}
