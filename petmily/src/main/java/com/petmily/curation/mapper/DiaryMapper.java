package com.petmily.curation.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.petmily.curation.dto.DiaryDto;
import com.petmily.curation.dto.DiaryPetTagDto;
import com.petmily.curation.dto.PetTagDto;

@Mapper
public interface DiaryMapper {
	public void insertDiary(DiaryDto diary) throws Exception; // c
	public List<DiaryDto> selectDiaryAll(int user_id) throws Exception; // r 
	public DiaryDto selectDiaryByDay(int user_id,int diary_year,int diary_month,int diary_day) throws Exception; // r 
	
	public void updateDiary(DiaryDto diary) throws Exception; // u
	public void deleteDiary(int diary_id) throws Exception; // d
	
	public void registerPetTags(DiaryPetTagDto pet) throws Exception;
	public List<DiaryPetTagDto> selectAllPetTags(int user_id) throws Exception;
	public List<DiaryPetTagDto> selectPet(int diary_id) throws Exception;
	public void deletePetTags(int diary_id) throws Exception;
	public boolean deletePetTagsByPet(int pet_id) throws Exception;
	
	public boolean deleteDiaryByUserId(int user_id) throws Exception; // 탈퇴회원 관련 다이어리 삭제
	
}
