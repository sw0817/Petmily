package com.petmily.curation.service;

import java.util.List;

import com.petmily.curation.dto.FileDto;

public interface FileService {
	public boolean insertFileService(FileDto file) throws Exception; // c
	public List<FileDto> selectAllFileService() throws Exception; // r, 모든 이미지 가져오기
	public List<FileDto> selectFile(int id) throws Exception; // r, user_id로 이미지 가져오기
	public List<FileDto> selectFileByFeed(int id) throws Exception; // r
	public boolean updateFileService(FileDto file) throws Exception; // u
	public boolean deleteFileService(int id) throws Exception; // d
	
	public boolean deleteAllFileUserId(int user_id) throws Exception; // 탈퇴회원관련 모든 파일삭제
}
