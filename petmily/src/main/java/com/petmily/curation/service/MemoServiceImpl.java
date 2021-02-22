package com.petmily.curation.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petmily.curation.dto.MemoDto;
import com.petmily.curation.mapper.MemoMapper;

@Service
public class MemoServiceImpl implements MemoService {
	
	@Autowired
	private MemoMapper memoMapper;
	
	@Override
	public void writeMemo(MemoDto memoDto) throws SQLException {
		memoMapper.writeMemo(memoDto);
	}

	@Override
	public List<MemoDto> listMemo(int feed_id) throws SQLException {
		return memoMapper.listMemo(feed_id);
	}

	@Override
	public void modifyMemo(MemoDto memoDto) throws SQLException {
		memoMapper.modifyMemo(memoDto);
	}

	@Override
	public void deleteMemo(int memo_id) throws SQLException {
		memoMapper.deleteMemo(memo_id);
	}

	@Override
	public boolean deleteMemoByUserId(int user_id) throws SQLException {
		return memoMapper.deleteMemoByUserId(user_id);
	}


}
