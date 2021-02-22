package com.petmily.curation.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.petmily.curation.dto.FollowAlertDto;
import com.petmily.curation.dto.FollowDto;
import com.petmily.curation.dto.MemberDto;
import com.petmily.curation.service.FollowAlertService;
import com.petmily.curation.service.EmailService;
import com.petmily.curation.service.FollowService;
import com.petmily.curation.service.JwtService;
import com.petmily.curation.service.LoginService;
import com.petmily.curation.service.MemberService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/member")
public class MemberController {
	final String SUCC = "SUCCESS";
	final String FAIL = "FAIL";
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private LoginService loginSvc;
	
	@Autowired
	private MemberService memberService;

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private FollowService followService;
	
	@Autowired
	private FollowAlertService alertService;
	
	public static final Logger logger = LoggerFactory.getLogger(MemberController.class);

	

	@PostMapping("/follow")
	public FollowDto follow(@RequestBody FollowDto followDto) throws Exception {
		
		int from_id = followDto.getFrom_id();
		int to_id = followDto.getTo_id();

		//alert!
		//팔로우 버튼을 클릭할 때, to_id의 alert 테이블에 from_id님이 회원님을 팔로우 했습니다. 의 문구가 같이 저장되어야함
		FollowAlertDto alertDto = new FollowAlertDto();
		//set user_id랑 set alert_content
		alertDto.setAlert_to(to_id);
		alertDto.setFrom_id(from_id);
		alertDto.setFrom_nickname(followDto.getFrom_nickname());
		alertDto.setAlert_content("님이 회원님을 팔로우 했습니다.");
		int check1 = alertService.checkExisted(alertDto);
		if(check1==0)
			alertService.createAlert(alertDto);
		
		
		int check = followService.checkF4F(followDto);
		System.out.println("확인확인 : "+check);
		if(check>0) { //맞팔
			//1. insert f4f 1로
			followService.followF4F(followDto);
			//2. 기존 to->from 의 f4f도 1로 변경
			followService.updateF4F(followDto);
		}else { //맞팔 아님
			// insert f4f 0으로
			followService.followNotF4F(followDto);
		}

		return followDto;
	}
	
	@PostMapping("/unfollow")
	public FollowDto unfollow(@RequestBody FollowDto followDto) throws Exception {
		
		int from_id = followDto.getFrom_id();
		int to_id = followDto.getTo_id();
		boolean f4f = followDto.isF4f();

		if(f4f) { //서로 맞팔이면....
			//나는 삭제하면 그만이지만, 남은 나를 맞팔처리가 되어있으므로 수정해주어야함..
			followService.deleteF4F(followDto);
		}
		
		followService.unfollow(followDto);
		
		return followDto;
	}
	
	@PostMapping("/following/{from_id}")
	public List<FollowDto> following(@PathVariable int from_id)throws Exception{
		List<FollowDto> following = new ArrayList<>();
		following = followService.followingList(from_id);
//		for(FollowDto f : following) {
//			System.out.println(f.getTo_nickname());
//		}
		return following;
	}
	@PostMapping("/follower/{to_id}")
	public List<FollowDto> follower(@PathVariable int to_id)throws Exception{
		List<FollowDto> follower = new ArrayList<>();
		follower = followService.followerList(to_id);
//		for(FollowDto f : follower) {
//			System.out.println(f.getFrom_nickname());
//		}
		
		return follower;
	}
	
	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> login(@RequestBody MemberDto memberDto, HttpServletResponse response, HttpSession session) {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {
			MemberDto loginUser = loginSvc.login(memberDto);
			
			if(loginUser != null) {
//				jwt.io에서 확인
//				로그인 성공했다면 토큰을 생성한다.
				String token = jwtService.create(loginUser);
				logger.trace("로그인 토큰정보 : {}", token);
				System.out.println(loginUser + " 로그인");
//				토큰 정보는 response의 헤더로 보내고 나머지는 Map에 담는다.
//				response.setHeader("auth-token", token);
				resultMap.put("auth-token", token);
				resultMap.put("user-id", loginUser.getUser_id());
				resultMap.put("user-email", loginUser.getUser_email());
				resultMap.put("user-nickname", loginUser.getUser_nickname());
				resultMap.put("user-content", loginUser.getUser_content());
				resultMap.put("user-password", loginUser.getUser_password());
				resultMap.put("user-name", loginUser.getUser_name());
				resultMap.put("user-birth", loginUser.getUser_birth());
				resultMap.put("user-gender", loginUser.getUser_gender());
				status = HttpStatus.ACCEPTED;
			} else {
				resultMap.put("message", "로그인 실패");
				status = HttpStatus.ACCEPTED;
			}
		} catch (Exception e) {
			logger.error("로그인 실패 : {}", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@ApiOperation(value = "Signup",notes="회원 가입")
	@PostMapping("/signup") //회원가입
	public void signup(@RequestBody MemberDto memberDto) throws Exception {
		//만약에 certified가 Y가 아니면.... 회원가입 안되었다고 알려줘야지
		memberService.signup(memberDto);
	}
	
	@ResponseBody
	@RequestMapping(value="/getallmember", method = RequestMethod.GET)
	public List<MemberDto> getAllMembers() throws Exception{
		List<MemberDto> result = memberService.getAllMembers();
		return result;
	}
	
	@PostMapping("/getsinglemember/{user_id}") //user_id에 해당하는 회원의 정보만 가져오기
	public MemberDto getSingleMember(@PathVariable int user_id) throws Exception {
		MemberDto dto = new MemberDto();
		dto=memberService.getSingleMember(user_id);
		return dto;
	}

	@PutMapping("/userupdate/{user_email}")
	public ResponseEntity<String> userUpdate(@RequestBody MemberDto memberDto) {
		memberService.userUpdate(memberDto);
		if(memberDto!=null)
			return new ResponseEntity<String>(SUCC, HttpStatus.OK);
		return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT); 
	}
	
	// 회원 탈퇴 시 지워야 할 항목 
	@DeleteMapping("/userdelete/{user_email}")
	public ResponseEntity<String> userDelete(@PathVariable String user_email) throws Exception{
		
		if(memberService.userDelete(user_email)) {
			return new ResponseEntity<String>(SUCC, HttpStatus.OK);
		}
		return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT); 
	}
	
	
	// 아이디 찾기
	@PostMapping("/findId")
	public List<MemberDto> FindId(HttpServletRequest request)throws Exception{
		List<MemberDto> members = new ArrayList<MemberDto>();
		
		String user_name = request.getParameter("user_name");
		String user_birth = request.getParameter("user_birth");
		members = memberService.findId(user_name,user_birth);
	
		for(MemberDto member : members) {
			StringBuilder str = new StringBuilder(member.getUser_email());
			for(int i=4;str.charAt(i)!='@';i++) {
				str.setCharAt(i,'*');
			}
			member.setUser_email(str.toString());
		}
		return members;
	}
	
	//이메일 인증! - isMailExisted가 0이면 회원가입 인증, 1이면 비밀번호 찾기 인증
	@PostMapping(value = "/emailsend/{user_email}/{certified}/{isMailExisted}")
	public void sendmail(@PathVariable String user_email, @PathVariable String certified, @PathVariable int isMailExisted) throws MessagingException {
		StringBuffer emailcontent = new StringBuffer();
		System.out.println("들어오는 이메일 : "+user_email);
		System.out.println("isMailExisted : "+isMailExisted);
		emailcontent.append("<!DOCTYPE html>");
		emailcontent.append("<html>");
		emailcontent.append("<head>");
		emailcontent.append("</head>");
		emailcontent.append("<body>");
		if(isMailExisted==0) {
			emailcontent.append(
					" <div" 																																																	+ 
					"	style=\"font-family: 'Apple SD Gothic Neo', 'sans-serif' !important; width: 400px; height: 600px; border-top: 4px solid #9932CC; margin: 100px auto; padding: 30px 0; box-sizing: border-box;\">"		+ 
					"	<h1 style=\"margin: 0; padding: 0 5px; font-size: 28px; font-weight: 400;\">"																															+ 
					"		<span style=\"font-size: 15px; margin: 0 0 10px 3px;\">PETMILY</span><br />"																													+ 
					"		<span style=\"color: #9932CC\">메일인증</span> 안내입니다."																																				+ 
					"	</h1>\n"																																																+ 
					"	<p style=\"font-size: 16px; line-height: 26px; margin-top: 50px; padding: 0 5px;\">"																													+ 
					"		반려동물과의 일상 이야기 [PETMILY]에 가입해 주셔서 진심으로 감사드립니다.<br />"																																						+ 
					"		아래 <b style=\"color: #9932CC\">인증코드</b> 를 입력하여 회원가입을 완료해 주세요.<br />"																													+ 
					"		감사합니다."																																															+ 
					"	</p>"																																																	+ 
					"	<a style=\"color: #9932CC; text-decoration: none; text-align: center;\""																																	+
					"		<div"																																																	+
					"			style=\"display: inline-block; width: 210px; height: 45px; margin: 30px 5px 40px; background: #9932CC; line-height: 45px; vertical-align: middle; font-size: 16px;\">"							+ 
					""+certified+"</div>"																																														+ 
					"	</a>"																																																	+
					"	<div style=\"border-top: 4px solid #9932CC; padding: 5px;\"></div>"																																		+
					" </div>"
			);
		}else if(isMailExisted==1) { //비밀번호 찾기 위한 인증이면!
			emailcontent.append(
					" <div" 																																																	+ 
					"	style=\"font-family: 'Apple SD Gothic Neo', 'sans-serif' !important; width: 400px; height: 600px; border-top: 4px solid #9932CC; margin: 100px auto; padding: 30px 0; box-sizing: border-box;\">"		+ 
					"	<h1 style=\"margin: 0; padding: 0 5px; font-size: 28px; font-weight: 400;\">"																															+ 
					"		<span style=\"font-size: 15px; margin: 0 0 10px 3px;\">PETMILY</span><br />"																													+ 
					"		<span style=\"color: #9932CC\">메일인증</span> 안내입니다."																																				+ 
					"	</h1>\n"																																																+ 
					"	<p style=\"font-size: 16px; line-height: 26px; margin-top: 50px; padding: 0 5px;\">"																													+ 
					"		반려동물과의 일상 이야기 [PETMILY]입니다.<br />"																																						+ 
					"		아래 <b style=\"color: #9932CC\">인증코드</b> 를 입력하여 비밀번호 갱신을 완료해 주세요.<br />"																													+ 
					"		감사합니다."																																															+ 
					"	</p>"																																																	+ 
					"	<a style=\"color: #9932CC; text-decoration: none; text-align: center;\""																																	+
					"		<div"																																																	+
					"			style=\"display: inline-block; width: 210px; height: 45px; margin: 30px 5px 40px; background: #9932CC; line-height: 45px; vertical-align: middle; font-size: 16px;\">"							+ 
					""+certified+"</div>"																																														+ 
					"	</a>"																																																	+
					"	<div style=\"border-top: 4px solid #9932CC; padding: 5px;\"></div>"																																		+
					" </div>"
			);
		}
		emailcontent.append("</body>");
		emailcontent.append("</html>");
	
		emailService.sendMail(user_email, "[PETMILY 이메일 인증]", emailcontent.toString());
	}

	
	//비밀번호 변경 : 이메일(중복 안되니까)로 탐색해서 비밀번호 update
	@PostMapping(value = "/changepw/{user_email}/{user_password}")
	public void findPassword(@PathVariable String user_email, @PathVariable String user_password) throws MessagingException {
		memberService.findPassword(user_email,user_password);
	}

	//닉네임 중복검사 가능 여부 확인
	@GetMapping(value = "/changenickname/{user_nickname}")
	public int changeNickname(@PathVariable String user_nickname) throws Exception {
		return memberService.changeNickname(user_nickname); //중복검사했을 때 0이 나와야 되는거!
	}
	
	// http://localhost:8000/member/deleteallfollow/5
	@ApiOperation(value = "팔로우 삭제", notes = "탈퇴회원과 관련된 모든 팔로우정보 삭제", response = List.class)
	@DeleteMapping("deleteallfollow/{user_id}")
	public boolean deleteAllFollow(@PathVariable(name = "user_id") String user_id) throws Exception {
		boolean result=followService.deleteAllFollow(Integer.parseInt(user_id));
		return result==true;
	}
}

