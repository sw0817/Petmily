package com.petmily.curation.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.petmily.curation.dto.MemberDto;

public interface MemberService extends UserDetailsService{

	public MemberDto login(MemberDto memberDto) throws Exception;
	public String getServerInfo();
	
	public void signup(MemberDto memberDto) throws Exception;
	public void userUpdate(MemberDto memberDto);
	public boolean userDelete(String user_email) throws Exception;
	public MemberDto getSingleMember(int user_id) throws SQLException, Exception;
	public List<MemberDto> getAllMembers() throws Exception;
	
	//아이디 찾기
	public List<MemberDto> findId(String user_name, String user_birth) throws Exception;
	//이메일 인증을 위한 함수들
	public MemberDto email_certified_check(MemberDto user);
	public void email_certified_update(MemberDto user);
	public void findPassword(String user_email, String user_password);
	public int changeNickname(String user_nickname) throws Exception;

}
