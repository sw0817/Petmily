package com.petmily.curation.service;

import java.sql.SQLException;

import com.petmily.curation.dto.MemberDto;

public interface LoginService {
	
	public MemberDto login(MemberDto memberDto) throws SQLException, Exception;
	public String getServerInfo();
}