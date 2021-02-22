package com.petmily.curation.mapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.petmily.curation.dto.MemberDto;
@Mapper
public interface MemberMapper {

	public MemberDto login(MemberDto memberDto) throws SQLException;
	
	public void signup(MemberDto memberDto) throws SQLException;
	public void userUpdate(MemberDto memberDto);//회원정보 수정
	public boolean userDelete(String user_email) throws Exception;
	
	public MemberDto getSingleMember(int user_id) throws SQLException; //userid로 회원정보 검색
	
	public List<MemberDto> getAllMembers() throws Exception;
	
	public MemberDto email_certified_check(MemberDto user);
	public void email_certified_update(MemberDto user);

	public List<MemberDto> findId(@Param("user_name") String user_name, @Param("user_birth") String user_birth) throws Exception;

	public void findPassword(String user_email, String user_password);

	public int changeNickname(String user_nickname);


	
}
