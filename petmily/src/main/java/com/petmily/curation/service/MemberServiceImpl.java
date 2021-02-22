package com.petmily.curation.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.petmily.curation.dto.MemberDto;
import com.petmily.curation.mapper.MemberMapper;

@Service
public class MemberServiceImpl implements MemberService {

//	@Autowired
//	private SqlSession sqlSession;
	@Autowired
	private MemberMapper memberMapper;
	
	@Override
	public MemberDto login(MemberDto memberDto) throws Exception {
		return memberMapper.login(memberDto);
	}

	@Override
	public String getServerInfo() {
		return "사용자에게 전달하고 싶은 중요정보";
	}

	@Override //회원가입!
	public void signup(MemberDto memberDto) throws Exception {
		memberMapper.signup(memberDto);
	}

	@Override //회원정보 수정! ID 입력 후
	@Transactional
	public void userUpdate(MemberDto memberDto) {
		memberMapper.userUpdate(memberDto);
	}


	@Override
	public List<MemberDto> getAllMembers()throws Exception{
		List<MemberDto> result = new ArrayList<MemberDto>();
		result = memberMapper.getAllMembers();
		return result;
	}
	
	@Override
	public MemberDto email_certified_check(MemberDto user) {
		return memberMapper.email_certified_check(user);
	}

	@Override
	public void email_certified_update(MemberDto user) {
		memberMapper.email_certified_update(user);		
	}
	
	//비밀번호 변경!
	@Override
	public void findPassword(String user_email, String user_password) {
		memberMapper.findPassword(user_email,user_password);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MemberDto> findId(String user_name, String user_birth) throws Exception {
		System.out.println("생일? : "+user_birth);
		return memberMapper.findId(user_name, user_birth);
	}

	@Override
	public int changeNickname(String user_nickname) throws Exception{
		return memberMapper.changeNickname(user_nickname);
	}
	
	@Override //한 사람 정보 가져오기
	public MemberDto getSingleMember(int user_id) throws Exception {
		return memberMapper.getSingleMember(user_id);
	}

	@Override
	public boolean userDelete(String user_email) throws Exception {
		return memberMapper.userDelete(user_email);
	}


}
