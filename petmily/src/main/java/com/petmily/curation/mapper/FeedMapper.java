package com.petmily.curation.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.petmily.curation.dto.FeedDto;
import com.petmily.curation.dto.FriendTagDto;
import com.petmily.curation.dto.PetTagDto;
import com.petmily.curation.dto.TagCountDto;
import com.petmily.curation.dto.TagDto;

@Mapper
public interface FeedMapper {
	public FeedDto selectFeedOne(int feed_id) throws Exception; // 한 피드 관련 모든 정보 가져오기

	public int insertFeed(FeedDto feed) throws Exception; // c
	public List<FeedDto> selectFeedAll() throws Exception; // r 모든 feed가져오기
	public List<FeedDto> selectFeed(int id) throws Exception; // r 특정 유저의 feed가져오기
	public boolean updateFeed(FeedDto feed) throws Exception; // u
	public boolean deleteFeed(int id) throws Exception; // d
	
	public void registerTags(@Param("feed_id") int feed_id, @Param("tag_content") String tag_content) throws Exception; //태그 정보 추가
	public List<TagDto> selectAllTags() throws Exception;						// 모든 태그 정보 보기
	public List<TagDto> selectTag(int feed_id) throws Exception;				// 피드 하나의 태그 정보 보기
	public boolean updateTags(TagDto tag) throws Exception;					// 태그 업데이트
	public boolean deleteTags(int feed_id) throws Exception;					// 태그 삭제
	public List<TagCountDto> allCount() throws Exception;	// 태그 검색 + 개수
	
	public void registerPetTags(PetTagDto pet) throws Exception;	// 펫 태그 추가  
	public List<PetTagDto> selectAllPetTags() throws Exception;		// 모든 펫 태그 보기 
	public List<PetTagDto> selectPetTag(int feed_id) throws Exception; // 피드 하나의 펫 태그 보기
	public boolean updatePetTags(PetTagDto pettag) throws Exception;					// 펫 태그 업데이트
	public boolean deletePetTags(int feed_id) throws Exception;					// 펫 태그 삭제
	public boolean deletePetTagsByPet(int pet_id) throws Exception;
	
	public void registerFriendTags(FriendTagDto friend) throws Exception; // 친구 태그 추가
	public List<FriendTagDto> selectAllFriendTag() throws Exception; // 모든 친구 태그 보기 
	public List<FriendTagDto> selectFriendTag(int feed_id) throws Exception; // 피드 하나의 친구 태그 보기 
	public boolean updateFriendTags(FriendTagDto friendtag) throws Exception;					// 친구 태그 업데이트
	public boolean deleteFriendTags(int feed_id) throws Exception;					// 친구 태그 삭제
	
	
	public boolean deleteAllFeedUserId(int user_id) throws Exception; // 탈퇴회원 feed정보 삭제
	public boolean deleteAllFriend(int user_id) throws Exception; // 탈퇴회원 친구정보 삭제
	public boolean deleteAllPettags() throws Exception;
	public boolean deleteAlltags() throws Exception;
	
	
}
