package com.petmily.curation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petmily.curation.dto.BackgroundDto;
import com.petmily.curation.dto.DiaryFileDto;
import com.petmily.curation.dto.FileDto;
import com.petmily.curation.mapper.DiaryFileMapper;

@Service
public class DiaryFileServiceImpl implements DiaryFileService {

	@Autowired
	DiaryFileMapper diaryFileMapper;
	
	@Override
	public boolean insertFile(DiaryFileDto file) throws Exception {
		return diaryFileMapper.insertFile(file);
	}


	@Override
	public List<DiaryFileDto> selectFile(int diary_id) throws Exception {
		return diaryFileMapper.selectFile(diary_id);
	}

	
	@Override
	public List<DiaryFileDto> selectAllFile(int user_id) throws Exception {
		return diaryFileMapper.selectAllFile(user_id);
	}
	
	@Override
	public boolean updateFile(DiaryFileDto file) throws Exception {
		return diaryFileMapper.updateFile(file);
	}

	@Override
	public boolean deleteFile(int id) throws Exception {
		return diaryFileMapper.deleteFile(id);
	}


	@Override
	public void insertDiaryBg(BackgroundDto bg) throws Exception {
		diaryFileMapper.insertDiaryBg(bg);
	}


	@Override
	public BackgroundDto selectDiaryBg(int user_id) throws Exception {
		return diaryFileMapper.selectDiaryBg(user_id);
	}


	@Override
	public boolean updateDiaryBg(BackgroundDto bg) throws Exception {
		return diaryFileMapper.updateDiaryBg(bg);
	}


	@Override
	public boolean deleteDiaryBg(int user_id) throws Exception {
		return diaryFileMapper.deleteDiaryBg(user_id);
	}


	@Override
	public boolean deleteFileUserId(int user_id) throws Exception {
		return diaryFileMapper.deleteFileUserId(user_id);
	}

}
