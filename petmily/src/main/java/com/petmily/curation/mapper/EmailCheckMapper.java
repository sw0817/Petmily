package com.petmily.curation.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.petmily.curation.dto.MemberDto;

@Mapper
public interface EmailCheckMapper {
	public List<MemberDto> Userlist();
	public MemberDto readUser(String user_email);
	public List<String> readAuthority(String user_email);
	public void createUser(MemberDto user);
	public void createAuthority(MemberDto user);
	public void deleteUser(String user_email);
	public void deleteAuthority(String user_email);
	public String email_duplicate_check(MemberDto user);
	public MemberDto email_certified_check(MemberDto user);
	public void email_certified_update(MemberDto user);
}
