package com.petmily.curation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.petmily.curation.dto.LikeDto;
import com.petmily.curation.dto.LikeListDto;
import com.petmily.curation.service.LikeService;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/like")
public class LikeController {

	@Autowired
	LikeService likeService;
 // RequestParam으로 바꾸기 
	//  http://localhost:8000/like/new/{user_id}/{feed_id}
	@PostMapping("/new")
	public int insertLike(@RequestBody LikeDto like) throws Exception {
		likeService.insertLike(like);
		return likeService.countLike(like.getFeed_id());
	}
	
	@DeleteMapping("/delete")
	public int deleteLike(@RequestBody LikeDto like) throws Exception {
		likeService.deleteLike(like);
		return likeService.countLike(like.getFeed_id());
	}

	
	@GetMapping(path = "select/{feed_id}")
	public int selectAllLike(@PathVariable("feed_id") int feed_id) throws Exception {
		return likeService.countLike(feed_id);
	}
	
	@GetMapping("list")
	public List<LikeListDto> likeList() throws Exception{
		List<LikeListDto> list = likeService.listLike();
		return list;
	}
	
	// http://localhost:8000/like/deletelikeuserid/5
	@DeleteMapping("/deletelikeuserid/{user_id}")
	public boolean deleteLikeUserId(@PathVariable(name = "user_id") int user_id) throws Exception {
		boolean result = likeService.deleteLikeByUserId(user_id);
		return result;
	}
}
