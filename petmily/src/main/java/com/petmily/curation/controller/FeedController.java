package com.petmily.curation.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.petmily.curation.dto.FeedAlertDto;
import com.petmily.curation.dto.FeedDto;
import com.petmily.curation.dto.FileDto;
import com.petmily.curation.dto.FriendTagDto;
import com.petmily.curation.dto.LikeDto;
import com.petmily.curation.dto.MemberDto;
import com.petmily.curation.dto.PetTagDto;
import com.petmily.curation.dto.TagCountDto;
import com.petmily.curation.dto.TagDto;
import com.petmily.curation.service.FeedAlertService;
import com.petmily.curation.service.FeedService;
import com.petmily.curation.service.FileService;
import com.petmily.curation.service.LikeService;
import com.petmily.curation.service.MemberService;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/feed")
public class FeedController {

	@Autowired
	MemberService memberService;
	@Autowired
	FeedService feedService;
	@Autowired
	FeedAlertService feedAlertService;
	@Autowired
	FileService fileService;

	@Autowired
	LikeService likeService;

	// 피드 정보 입력
	// http://localhost:8000/feed/uploadFeed
	@PostMapping("uploadFeed")
	public void uploadFeed(@RequestPart(name = "filesupload", required = false) List<MultipartFile> filesupload,
			@RequestParam(name = "content", required = false) String content,
			@RequestParam(name = "user_id", required = false) int user_id,
			@RequestParam(name = "pet_id", required = false) List<Integer> pet_id,
			@RequestParam(name = "pet_name", required = false) List<String> pet_name,
			@RequestParam(name = "tag_things", required = false) List<String> tag_things,
			@RequestParam(name = "fileTypes", required = false) List<String> file_types,
			@RequestParam(name = "image_filters", required = false) List<String> filter_name,
			@RequestParam(name = "friend_id", required = false) List<Integer> friend_id,
			@RequestParam(name = "friend_nickname", required = false) List<String> friend_nickname) throws Exception {

		//우선 user_id를 이용해서 alert 테이블의 from_nickname을 찾아야함!
		MemberDto m = memberService.getSingleMember(user_id);
		String user_nickname=m.getUser_nickname();
		
		FeedDto feed = new FeedDto();
		feed.setUser_id(user_id);
		feed.setFeed_content(content);
		feedService.insertFeed(feed);

		// #해시태그 등록
		if (tag_things != null) {
			for (int i = 0; i < tag_things.size(); i++) {
				feedService.registerTags(feed.getFeed_id(), tag_things.get(i));
			}
		}
		// #동물태그
		if (pet_id != null) {
			PetTagDto ptg = new PetTagDto();
			for (int i = 0; i < pet_id.size(); i++) {
				ptg.setFeed_id(feed.getFeed_id());
				ptg.setPet_id(pet_id.get(i));
				ptg.setPet_name(pet_name.get(i));

				feedService.registerPetTags(ptg);
			}
		}
	
		// 친구태그
		if (friend_id != null) {
			FriendTagDto friendtag = new FriendTagDto();
			for (int i = 0; i < friend_id.size(); i++) {
				friendtag.setFeed_id(feed.getFeed_id());
				friendtag.setFriend_id(friend_id.get(i));
				friendtag.setFriend_nickname(friend_nickname.get(i));

				feedService.registerFriendTags(friendtag);
				
				FeedAlertDto alertDto=new FeedAlertDto();
				alertDto.setAlert_to(friend_id.get(i));
				alertDto.setAlert_kind(1);
				alertDto.setFrom_id(user_id);
				alertDto.setFrom_nickname(user_nickname);
				alertDto.setFeed_id(feed.getFeed_id());
				alertDto.setAlert_content("님이 게시글에 회원님을 태그했습니다.");
				alertDto.setFeed_content(content);
				
				feedAlertService.createTagAlert(alertDto);

			}
		}

		// 이미지 저장
		if(filesupload != null) {
			FileDto file = new FileDto();
			for(int i=0; i<filesupload.size(); i++) {
				String sourceFileName = filesupload.get(i).getOriginalFilename();  // 파일 원본 이름
		        String sourceFileNameExtension = FilenameUtils.getExtension(sourceFileName).toLowerCase(); // 동일한 이름으로 업로드될때를 대비해서 파일이름 중복되지 않게 변환
		        File destinationFile; 
		        String destinationFileName;
				        
		        String fileUrl = "/home/ubuntu/images/";	
//		        String fileUrl = "C:/Petmily/frontend/src/assets/images/FeedImg/";
		        destinationFileName = RandomStringUtils.randomAlphanumeric(32) + "." + sourceFileNameExtension; 
		        destinationFile = new File(fileUrl + destinationFileName); 
		        if(!destinationFile.exists()) {
		        	destinationFile.getParentFile().mkdirs(); 
		        }
		        filesupload.get(i).transferTo(destinationFile); 
				        
				file.setFeed_id(feed.getFeed_id());
				file.setUser_id(feed.getUser_id());
				file.setFile_type(file_types.get(i));
				file.setFilter_name(filter_name.get(i));
				file.setFile_name(destinationFileName);
				file.setFile_oriname(sourceFileName);
				file.setFile_url(fileUrl);
				fileService.insertFileService(file);
			}
		}
	}
	// http://localhost:8000/feed/update
	@PutMapping("update")
	   public boolean updateFeed(
	         @RequestParam(name = "feed_id", required = true) String feed_id,
	         @RequestParam(name = "content", required = false) String content,
	         @RequestParam(name = "user_id", required = false) int user_id,
	         @RequestParam(name = "pet_id", required = false) List<Integer> pet_id,
	         @RequestParam(name = "pet_name", required = false) List<String> pet_name,
	         @RequestParam(name = "tag_things", required = false) List<String> tag_things,
	         @RequestParam(name = "friend_id", required = false) List<Integer> friend_id,
	         @RequestParam(name = "friend_nickname", required = false) List<String> friend_nickname) throws Exception {

	      FeedDto feed = new FeedDto();
	      feed.setFeed_content(content);
	      feed.setFeed_id(Integer.parseInt(feed_id));
	      feed.setUser_id(user_id);

	      // 피드 업데이트
	      boolean retFeed = feedService.updateFeed(feed);

	      // 해시태그 수정 ( 원래 태그 리스트 삭제 후 새로운 태그 리스트 등록 )
	      
	      if (tag_things != null) {
	         feedService.deleteTags(Integer.parseInt(feed_id)); // 원래 태그 리스트 삭제 
	         for (int i = 0; i < tag_things.size(); i++) {
	            feedService.registerTags(Integer.parseInt(feed_id), tag_things.get(i));
	         }
	         
	      }

	      // 동물태그 수정
	      if (pet_id != null) {
	         feedService.deletePetTags(Integer.parseInt(feed_id));
	         PetTagDto ptg = new PetTagDto();
	         for (int i = 0; i < pet_id.size(); i++) {
	            ptg.setFeed_id(feed.getFeed_id());
	            ptg.setPet_id(pet_id.get(i));
	            ptg.setPet_name(pet_name.get(i));

	            feedService.registerPetTags(ptg);
	         }
	      }

	      // 친구태그 수정
	      if (friend_id != null) {
	         feedService.deleteFriendTags(Integer.parseInt(feed_id));
	         FriendTagDto friendtag = new FriendTagDto();
	         for (int i = 0; i < friend_id.size(); i++) {
	            friendtag.setFeed_id(feed.getFeed_id());
	            friendtag.setFriend_id(friend_id.get(i));
	            friendtag.setFriend_nickname(friend_nickname.get(i));

	            feedService.registerFriendTags(friendtag);
	            
	    		//우선 user_id를 이용해서 alert 테이블의 from_nickname을 찾아야함!
	    		MemberDto m = memberService.getSingleMember(user_id);
	    		String user_nickname=m.getUser_nickname();
	            
				FeedAlertDto alertDto=new FeedAlertDto();
				alertDto.setAlert_to(friend_id.get(i));
				alertDto.setAlert_kind(1);
				alertDto.setFrom_id(user_id);
				alertDto.setFrom_nickname(user_nickname);
				alertDto.setFeed_id(feed.getFeed_id());
				alertDto.setAlert_content("님이 게시글에 회원님을 태그했습니다.");
				alertDto.setFeed_content(content);
				
				//근데 이미 관련된 알림이 있다면 내용만 수정해줘야하지 않을까? ㅎㅎ
				//관련 알람이 존재하는지 확인하는 sql문을 만들고 존재한다면 update문을 하나 더 만들어야할듯
				int check = feedAlertService.checkExisted(alertDto);
				if(check==0)
					feedAlertService.createTagAlert(alertDto);
				else
					feedAlertService.updateTagAlert(alertDto); //feed_content만 업데이트
	         }
	      }

	      System.out.println("feed업데이트!");
	      return retFeed;
	   }
	// 모든 feed정보 가져오기
	@GetMapping("allFeed")
	public List<FeedDto> selectAllFeed() throws Exception {
		List<FeedDto> result = feedService.selectFeedAll();
		return result;
	}

	@GetMapping("allFile")
	public List<FileDto> selectAllFile() throws Exception {
		List<FileDto> result = fileService.selectAllFileService();
		return result;
	}

	@GetMapping("allPet")
	public List<PetTagDto> selectAllPetTag() throws Exception {
		List<PetTagDto> result = feedService.selectAllPetTags();
		return result;
	}

	@GetMapping("allTag")
	public List<TagDto> selectAllTag() throws Exception {
		List<TagDto> result = feedService.selectAllTags();
		return result;
	}
	@GetMapping("allCount")
	public List<TagCountDto> allCount() throws Exception{
		List<TagCountDto> result = feedService.allCount();
		return result;
	}
	@GetMapping("allFriendTag")
	public List<FriendTagDto> selectAllFriendTag() throws Exception {
		List<FriendTagDto> result = feedService.selectAllFriendTag();
		return result;
	}

	// 특정 user_id가 등록한 feed만 가져오기
	@GetMapping("feed/{user_id}")
	public List<FeedDto> selectFeed(@PathVariable(name = "user_id") String user_id) throws Exception {
		List<FeedDto> result = feedService.selectFeed(Integer.parseInt(user_id));
		return result;
	}

	// 피드, 파일, 펫태그, 친구태그, 사물태그, 좋아요
	@GetMapping("one/{feed_id}")
	public Map<String, Object> selectFeedOne(@PathVariable(name = "feed_id") String feed_id) throws Exception {
		Map<String, Object> result = new HashMap<>();

		// 피드 정보
		FeedDto feed = new FeedDto();
		feed = feedService.selectFeedOne(Integer.parseInt(feed_id));
		result.put("feed", feed);

		// 파일 정보
		List<FileDto> files = new ArrayList<>();
		files = fileService.selectFileByFeed(Integer.parseInt(feed_id));
//		for (int i = 0; i < files.size(); i++) {
//			System.out.println("파일정보 : " + files.get(i).toString());
//		}
		result.put("files", files);

		// 펫 태그 정보
		List<PetTagDto> petTags = new ArrayList<>();
		petTags = feedService.selectPetTag(Integer.parseInt(feed_id));
//		for (int i = 0; i < petTags.size(); i++) {
//
//			System.out.println("펫태그정보 : " + petTags.get(i).toString());
//		}
		result.put("petTag", petTags);

		// 친구 태그 정보
		List<FriendTagDto> friendTags = new ArrayList<>();
		friendTags = feedService.selectFriendTag(Integer.parseInt(feed_id));
		result.put("friendTag", friendTags);

		// 사물 태그 정보
		List<TagDto> tags = new ArrayList<>();
		tags = feedService.selectTag(Integer.parseInt(feed_id));
//		for (int i = 0; i < tags.size(); i++) {
//
//			System.out.println("태그정보 : " + tags.get(i));
//		}
		result.put("Tag", tags);

		// 좋아요 정보
		List<LikeDto> likes = new ArrayList<>();
		likes = likeService.selectLike(Integer.parseInt(feed_id));
//		for (int i = 0; i < likes.size(); i++)
//			System.out.println("좋아요정보 : " + likes.get(i));
		
		result.put("likes", likes);

		return result;
	}

	// 다른 사람 프로필들어갈때 필요하지 않을까!
	@GetMapping("file/{user_id}")
	public List<FileDto> selectFile(@PathVariable(name = "user_id") String user_id) throws Exception {
		List<FileDto> result = fileService.selectFile(Integer.parseInt(user_id));
		return result;
	}

	// feed_id를 기준으로 삭제
	@DeleteMapping("deletion/{feed_id}")
	public void deleteFeed(@PathVariable(name = "feed_id") String feed_id) throws NumberFormatException, Exception {
		if(fileService.selectFileByFeed(Integer.parseInt(feed_id))!=null) {
			fileService.deleteFileService(Integer.parseInt(feed_id));
		}
		if(feedService.selectFeed(Integer.parseInt(feed_id))!=null) {
			feedService.deleteFeed(Integer.parseInt(feed_id));
		}
		if(feedService.selectTag(Integer.parseInt(feed_id))!=null) {
			feedService.deleteTags(Integer.parseInt(feed_id));
		}
		if(feedService.selectPetTag(Integer.parseInt(feed_id))!=null) {
			feedService.deletePetTags(Integer.parseInt(feed_id));
		}
		if(feedService.selectFriendTag(Integer.parseInt(feed_id))!=null) {
			feedService.deleteFriendTags(Integer.parseInt(feed_id));
		}
		if(likeService.selectLike(Integer.parseInt(feed_id))!=null) {
			likeService.deleteLikeOne(Integer.parseInt(feed_id));
		}
	}
	
	// http://localhost:8000/feed/deletionallfeeduserid/5
	@DeleteMapping("deletionallfeeduserid/{user_id}")
	public boolean deleteAllFeedUserId(@PathVariable(name = "user_id") String user_id) throws Exception {
		boolean result = feedService.deleteAllFeedUserId(Integer.parseInt(user_id));
		return result;
	}
	
	// http://localhost:8000/feed/deletionallfileuserid/5
	@DeleteMapping("deletionallfileuserid/{user_id}")
	public boolean deleteAllFileUserId(@PathVariable(name = "user_id") String user_id) throws Exception {
		boolean result = fileService.deleteAllFileUserId(Integer.parseInt(user_id));
		return result;
	}
	
	// http://localhost:8000/feed/deletionallfriend/5
	@DeleteMapping("deletionallfriend/{user_id}")
	public boolean deleteAllFriend(@PathVariable(name = "user_id") String user_id) throws Exception {
		boolean result = feedService.deleteAllFriend(Integer.parseInt(user_id));
		return result;
	}
	
	// http://localhost:8000/feed/deletionallpettags
	@DeleteMapping("deletionallpettags")
	public boolean deleteAllPettag() throws Exception{
		boolean result = feedService.deleteAllPettags();
		return result;
	}
	
	// http://localhost:8000/feed/deletionalltags
	@DeleteMapping("deletionalltags")
	public boolean deleteAlltags() throws Exception{
		boolean result = feedService.deleteAlltags();
		return result;
	}

	
}