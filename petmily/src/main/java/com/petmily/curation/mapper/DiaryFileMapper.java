package com.petmily.curation.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.petmily.curation.dto.BackgroundDto;
import com.petmily.curation.dto.DiaryFileDto;
import com.petmily.curation.dto.FileDto;
@Mapper
public interface DiaryFileMapper {
	public boolean insertFile(DiaryFileDto file) throws Exception; // c
	public List<DiaryFileDto> selectFile(int diary_id) throws Exception; // r
	public List<DiaryFileDto> selectAllFile(int user_id) throws Exception;
	public boolean updateFile(DiaryFileDto file) throws Exception; // u
	public boolean deleteFile(int id) throws Exception; // d
	
	public void insertDiaryBg(BackgroundDto bg)throws Exception; //c
	public BackgroundDto selectDiaryBg(int user_id) throws Exception; //r
	public boolean updateDiaryBg(BackgroundDto bg)throws Exception; //u
	public boolean deleteDiaryBg(int user_id) throws Exception; //d
	
	public boolean deleteFileUserId(int user_id) throws Exception; // 탈퇴회원 관련 정보삭제
	
	
}
