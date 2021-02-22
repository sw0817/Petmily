package com.petmily.curation.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.petmily.curation.dto.FileDto;

@Mapper
public interface FileMapper {
	public boolean insertFile(FileDto file) throws Exception; // c
	public List<FileDto> selectAllFile() throws Exception; // r
	public List<FileDto> selectFile(int id) throws Exception; // r
	public List<FileDto> selectFileByFeed(int id) throws Exception; // r
	public boolean updateFile(FileDto file) throws Exception; // u
	public boolean deleteFile(int id) throws Exception; // d
	
	public boolean deleteAllFileUserId(int user_id) throws Exception; // 탈퇴회원관련 모든 파일삭제
}
