package com.petmily.curation.service;

import java.util.List;

import com.petmily.curation.dto.LikeDto;
import com.petmily.curation.dto.LikeListDto;

public interface LikeService {
	public boolean insertLike(LikeDto like) throws Exception; // 좋아요 
	public boolean deleteLike(LikeDto like) throws Exception; // 좋아요 취소
	public boolean deleteLikeOne(int feed_id) throws Exception; // 좋아요 삭제
	public int countLike(int feed_id) throws Exception; // 좋아요 개수 세기; 해당 feed_id가 테이블에 있는 만큼이 좋아요 개수
	public int checkLike(int user_id,int feed_id) throws Exception; // 해당 user_id,feed_id에 해당하는 좋아요가 있는지 확인
	public List<LikeDto> selectLike(int feed_id) throws Exception;	// 피드 아이디로 좋아요 정보 모두 받음
	public List<LikeListDto> listLike() throws Exception; // 좋아요 테이블에 존재하는 유저리스트
	
	public boolean deleteLikeByUserId(int user_id) throws Exception; // 좋아요 삭제
}
