package com.petmily.curation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petmily.curation.dto.MemberDto;
import com.petmily.curation.mapper.LoginMapper;

@Service
public class LoginServiceImpl implements LoginService {
	
//	@Autowired
//	private SqlSession sqlSession;
	@Autowired
	private LoginMapper loginMapper;
	
	@Override
	public MemberDto login(MemberDto memberDto) throws Exception {
		return loginMapper.login(memberDto);
	}

	@Override
	public String getServerInfo() {
		return "사용자에게 전달하고 싶은 중요정보";
	}

	
//	
//	@Autowired
//	private LoginRepository loginRepo;
//	
//	@Override
//	public String login(String id, String pass) {
//		return loginRepo.login(id, pass);
//	}
}
