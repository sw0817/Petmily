package com.petmily.curation.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.petmily.curation.dto.FeedAlertDto;
import com.petmily.curation.dto.FeedDto;
import com.petmily.curation.dto.MemberDto;
import com.petmily.curation.dto.MemoDto;
import com.petmily.curation.service.FeedAlertService;
import com.petmily.curation.service.FeedService;
import com.petmily.curation.service.MemberService;
import com.petmily.curation.service.MemoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("MemoController V1")
@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/memo")
public class MemoController {

	private static final Logger logger = LoggerFactory.getLogger(MemoController.class);
//	private static final String SUCCESS = "success";
//	private static final String FAIL = "fail";
	@Autowired
	private MemoService memoService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private FeedService feedService;
	@Autowired
	private FeedAlertService feedAlertService;
	
	@ApiOperation(value = "댓글작성", notes = "새로운 댓글 정보를 입력한 후 댓글목록을 반환한다.", response = List.class)
	@PostMapping
	public ResponseEntity<List<MemoDto>> writeMemo(@RequestBody MemoDto memoDto) throws Exception {
		logger.info("writeMemo - 호출");
		memoService.writeMemo(memoDto);
		//댓글 작성 후 댓글을 작성했음을 게시글작성자에게 알려줘야함
		//alert_to : 게시글작성자 id / alert_kind : 2 / from_id : 댓글작성자 id
		//from_nickname : 댓글작성자 nickname / alert_content : 회원님의 게시글에 ~님이 댓글을 입력했습니다.
		//feed_id : 게시글 id / memo_id : 댓글 id / comment : 댓글 내용
		FeedAlertDto alertDto=new FeedAlertDto();
		alertDto.setFeed_id(memoDto.getFeed_id());
		//이 feed_id를 토대로... 게시글을 누가 작성했는지 정보를 알아와야 함
		FeedDto feedDto = feedService.selectFeedOne(memoDto.getFeed_id()); //여기서 게시글작성자 id 가져오기
		System.out.println(feedDto.getUser_id());
		alertDto.setAlert_to(feedDto.getUser_id());
		alertDto.setAlert_kind(2);
		alertDto.setAlert_content("님이 회원님의 게시글에 댓글을 입력했습니다.");
		alertDto.setFrom_id(memoDto.getUser_id());
		//댓글 입력 id를 기반으로 닉네임 찾기
		MemberDto m = memberService.getSingleMember(memoDto.getUser_id());
		alertDto.setFrom_nickname(m.getUser_nickname());
		alertDto.setMemo_id(memoDto.getMemo_id());
		alertDto.setComment(memoDto.getComment());
		if(alertDto.getAlert_to()!=alertDto.getFrom_id()) //글작성자와 댓글작성자가 다를때만!
			feedAlertService.createCommentAlert(alertDto);
		return new ResponseEntity<List<MemoDto>>(memoService.listMemo(memoDto.getFeed_id()), HttpStatus.OK);
	}

	@ApiOperation(value = "댓글목록", notes = "댓글 목록을 반환한다.", response = List.class)
	@GetMapping("{feed_id}")
	public ResponseEntity<List<MemoDto>> listMemo(@PathVariable("feed_id") int feed_id) throws Exception {
		logger.debug("listMemo - 호출");
		return new ResponseEntity<List<MemoDto>>(memoService.listMemo(feed_id), HttpStatus.OK);
	}
	
	@ApiOperation(value = "댓글수정", notes = "댓글 정보를 수정한 후 댓글목록을 반환한다.", response = List.class)
	@PutMapping
	public ResponseEntity<List<MemoDto>> modifyMemo(@RequestBody MemoDto memoDto) throws Exception {
		logger.debug("modifyMemo - 호출");
		memoService.modifyMemo(memoDto);
		return new ResponseEntity<List<MemoDto>>(memoService.listMemo(memoDto.getFeed_id()), HttpStatus.OK);
	}
	
	@ApiOperation(value = "댓글삭제", notes = "댓글 정보를 삭제한 후 댓글목록을 반환한다.", response = List.class)
	@DeleteMapping("{feed_id}/{memo_id}")
	public ResponseEntity<List<MemoDto>> deleteMemo(@PathVariable("feed_id") int feed_id, @PathVariable("memo_id") int memo_id) throws Exception {
		logger.debug("deleteMemo - 호출");
		memoService.deleteMemo(memo_id);
		return new ResponseEntity<List<MemoDto>>(memoService.listMemo(feed_id), HttpStatus.OK);
	}
	
	
	// http://localhost:8000/memo/deletememouserid/5
	@ApiOperation(value = "탈퇴회원 댓글삭제", notes = "탈퇴회원과 관련된 모든 댓글 삭제", response = List.class)
	@DeleteMapping("deletememouserid/{user_id}")
	public boolean deleteMemoUserId(@PathVariable(name = "user_id") String user_id) throws Exception {
		boolean result=memoService.deleteMemoByUserId(Integer.parseInt(user_id));
		return result;
	}
}
