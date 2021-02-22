package com.petmily.curation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petmily.curation.dto.FileDto;
import com.petmily.curation.mapper.FileMapper;

@Service
public class FileServiceImpl implements FileService{

	@Autowired
	FileMapper fileMapper;
	
	@Override
	public boolean insertFileService(FileDto file) throws Exception {
		return fileMapper.insertFile(file);
	}

	@Override
	public List<FileDto> selectAllFileService() throws Exception {
		return fileMapper.selectAllFile();
	}

	@Override
	public boolean updateFileService(FileDto file) throws Exception {
		return fileMapper.updateFile(file);
	}

	@Override
	public boolean deleteFileService(int id) throws Exception {
		return fileMapper.deleteFile(id);
	}

	@Override
	public List<FileDto> selectFile(int id) throws Exception {
		return fileMapper.selectFile(id);
	}

	@Override
	public List<FileDto> selectFileByFeed(int id) throws Exception {
		return fileMapper.selectFileByFeed(id);
	}

	@Override
	public boolean deleteAllFileUserId(int user_id) throws Exception {
		return fileMapper.deleteAllFileUserId(user_id);
	}
	
	
}
