package com.petmily.curation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petmily.curation.dto.DiaryDto;
import com.petmily.curation.dto.DiaryPetTagDto;
import com.petmily.curation.mapper.DiaryMapper;

@Service
public class DiaryServiceImpl implements DiaryService {

	@Autowired
	DiaryMapper diaryMapper;
	
	@Override
	public void insertDiary(DiaryDto diary) throws Exception {
		diaryMapper.insertDiary(diary);
	}

	@Override
	public void updateDiary(DiaryDto diary) throws Exception {
		diaryMapper.updateDiary(diary);
	}

	@Override
	public void deleteDiary(int diary_id) throws Exception {
		diaryMapper.deleteDiary(diary_id);
	}

	@Override
	public void registerPetTags(DiaryPetTagDto pet) throws Exception {
		diaryMapper.registerPetTags(pet);
		
	}

	@Override
	public List<DiaryPetTagDto> selectAllPetTags(int user_id) throws Exception {
		return diaryMapper.selectAllPetTags(user_id);
	}

	@Override
	public List<DiaryDto> selectDiaryAll(int user_id) throws Exception {
		return diaryMapper.selectDiaryAll(user_id);
	}

	@Override
	public DiaryDto selectDiaryByDay(int user_id,int diary_year,int diary_month,int diary_day) throws Exception {
		return diaryMapper.selectDiaryByDay(user_id,diary_year,diary_month,diary_day);
	}

	@Override
	public List<DiaryPetTagDto> selectPet(int diary_id) throws Exception {
		return diaryMapper.selectPet(diary_id);
	}

	@Override
	public void deletePetTags(int diary_id) throws Exception {
		diaryMapper.deletePetTags(diary_id);
		
	}

	@Override
	public boolean deletePetTagsByPet(int pet_id) throws Exception {
		return diaryMapper.deletePetTagsByPet(pet_id);
	}

	@Override
	public boolean deleteDiaryByUserId(int user_id) throws Exception {
		return diaryMapper.deleteDiaryByUserId(user_id);
	}

}
