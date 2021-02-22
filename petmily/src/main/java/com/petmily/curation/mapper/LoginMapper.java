package com.petmily.curation.mapper;

import java.sql.SQLException;

import org.apache.ibatis.annotations.Mapper;

import com.petmily.curation.dto.MemberDto;
@Mapper
public interface LoginMapper {
	//String login(String id, String pass);
	
	public MemberDto login(MemberDto memberDto) throws SQLException;
}