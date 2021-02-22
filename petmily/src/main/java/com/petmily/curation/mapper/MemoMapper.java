package com.petmily.curation.mapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.petmily.curation.dto.MemoDto;

@Mapper
public interface MemoMapper {

	public void writeMemo(MemoDto memoDto) throws SQLException;
	public List<MemoDto> listMemo(int feed_id) throws SQLException;
	public void modifyMemo(MemoDto memoDto) throws SQLException;
	public void deleteMemo(int memo_id) throws SQLException;
	
	public boolean deleteMemoByUserId(int user_id) throws SQLException;
	
}
