package com.petmily.curation.service;

import java.sql.SQLException;
import java.util.List;

import com.petmily.curation.dto.MemoDto;

public interface MemoService {

	public void writeMemo(MemoDto memoDto) throws SQLException;
	public List<MemoDto> listMemo(int feed_id) throws SQLException;
	public void modifyMemo(MemoDto memoDto) throws SQLException;
	public void deleteMemo(int memo_id) throws SQLException;
	
	public boolean deleteMemoByUserId(int user_id) throws SQLException;
	
}
