package com.petmily.curation.controller;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.petmily.curation.dto.BackgroundDto;
import com.petmily.curation.dto.DiaryDto;
import com.petmily.curation.dto.DiaryFileDto;
import com.petmily.curation.dto.DiaryPetTagDto;
import com.petmily.curation.dto.FileDto;
import com.petmily.curation.dto.MemberDto;
import com.petmily.curation.dto.PetTagDto;
import com.petmily.curation.service.DiaryFileService;
import com.petmily.curation.service.DiaryService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/diary")
public class DiaryController {
	final String SUCC = "SUCCESS";

	final String FAIL = "FAIL";

	@Autowired
	private DiaryService diaryService;

	@Autowired
	private DiaryFileService diaryFileService;

	@PostMapping("/newDiary")
	public void insertDiary(@RequestPart(name = "filesupload", required = false) List<MultipartFile> filesupload,
			@RequestParam(name = "fileTypes", required = false) List<String> file_types,
			@RequestParam(name = "user_id", required = true) String user_id,
			@RequestParam(name = "diary_title", required = true) String diary_title,
			@RequestParam(name = "diary_year", required = true) String diary_year,
			@RequestParam(name = "diary_month", required = true) String diary_month,
			@RequestParam(name = "diary_day", required = true) String diary_day,
			@RequestParam(name = "diary_content", required = true) String diary_content,
			@RequestParam(name = "diary_feeling", required = true) String diary_feeling,
			@RequestParam(name = "diary_weather", required = true) String diary_weather,
			@RequestParam(name = "diary_activity", required = true) String diary_activity,
			@RequestParam(name = "pet_id", required = false) List<String> pet_id

	) throws Exception {
//		System.out.println(filesupload + "  <-------전송된 파일");
//		System.out.println("파일 내용: " + diary_content);
//		System.out.println("user_id: " + user_id);
//		System.out.println("pet_id: " + pet_id);

		DiaryDto diary = new DiaryDto();
		diary.setUser_id(Integer.parseInt(user_id));
		diary.setDiary_title(diary_title);
		diary.setDiary_year(diary_year);
		diary.setDiary_month(diary_month);
		diary.setDiary_day(diary_day);
		diary.setDiary_content(diary_content);
		diary.setDiary_feeling(diary_feeling);
		diary.setDiary_weather(diary_weather);
		diary.setDiary_activity(diary_activity);
		diaryService.insertDiary(diary);
//		System.out.println(diary.toString() + " 새 다이어리 작성 ");
//
//		System.out.println("펫 아이디 " + pet_id);
		if (pet_id != null) {
			DiaryPetTagDto ptg = new DiaryPetTagDto();
			for (int i = 0; i < pet_id.size(); i++) {
				System.out.println(pet_id.get(i));
				ptg.setUser_id(diary.getUser_id());
				ptg.setDiary_id(diary.getDiary_id());
				ptg.setPet_id(Integer.parseInt(pet_id.get(i)));

				diaryService.registerPetTags(ptg);
			}
		}

		if (filesupload != null && filesupload.size() != 0) {
			DiaryFileDto diaryfile = new DiaryFileDto();
			for (int i = 0; i < filesupload.size(); i++) {
				String sourceFileName = filesupload.get(i).getOriginalFilename(); // 파일 원본 이름
				String sourceFileNameExtension = FilenameUtils.getExtension(sourceFileName).toLowerCase();
				File destinationFile;
				String destinationFileName;

		        
		        String fileUrl = "/home/ubuntu/images/";
//				String fileUrl = "C:/Petmily/frontend/src/assets/images/DiaryImg/";
			
		        destinationFileName = RandomStringUtils.randomAlphanumeric(32) + "." + sourceFileNameExtension; 
//		        System.out.println("변환되는 이름 : " + destinationFileName);
		        destinationFile = new File(fileUrl + destinationFileName); 
		        if(!destinationFile.exists()) {
		        	destinationFile.getParentFile().mkdirs(); 
		        }
				filesupload.get(i).transferTo(destinationFile);
				
				
				diaryfile.setDiary_id(diary.getDiary_id());
				diaryfile.setUser_id(diary.getUser_id());
				diaryfile.setFile_type(file_types.get(i));
				diaryfile.setFile_name(destinationFileName);
				diaryfile.setFile_oriname(sourceFileName);
				diaryfile.setFile_url(fileUrl);
				diaryFileService.insertFile(diaryfile);
			}
		}

	}

	// 특정 유저의 모든 다이어리 가져오기
	@GetMapping("/allDiary/{user_id}")
	public List<DiaryDto> selectDiaryAll(@PathVariable int user_id) throws Exception {
		List<DiaryDto> diary = diaryService.selectDiaryAll(user_id);
		return diary;
	}

	@GetMapping("/selectDiary/{user_id}/{diary_year}/{diary_month}/{diary_day}")
	public DiaryDto selectDiaryByDay(@PathVariable int user_id, @PathVariable int diary_year,
			@PathVariable int diary_month, @PathVariable int diary_day) throws Exception {
		DiaryDto diary = new DiaryDto();
		diary = diaryService.selectDiaryByDay(user_id, diary_year, diary_month, diary_day);
		return diary;
	}

	@GetMapping("/allDiaryFile/{user_id}")
	public List<DiaryFileDto> selectAllFile(@PathVariable int user_id) throws Exception {
		List<DiaryFileDto> result = diaryFileService.selectAllFile(user_id);
		return result;
	}

	@GetMapping("/selectFile/{diary_id}")
	public List<DiaryFileDto> selectFile(@PathVariable int diary_id) throws Exception {
		List<DiaryFileDto> result = diaryFileService.selectFile(diary_id);
		return result;
	}

	@GetMapping("/allPet/{user_id}")
	public List<DiaryPetTagDto> selectAllPetTag(@PathVariable int user_id) throws Exception {
		List<DiaryPetTagDto> result = diaryService.selectAllPetTags(user_id);
		return result;
	}

	@GetMapping("/selectPet/{diary_id}")
	public List<DiaryPetTagDto> selectPet(@PathVariable int diary_id) throws Exception {
		List<DiaryPetTagDto> result = diaryService.selectPet(diary_id);
		return result;
	}

	// 수정
	@PutMapping("/updateDiary")
	public ResponseEntity<String> diaryUpdate(@RequestParam(name = "diary_id", required = true) int diary_id,
			@RequestParam(name = "diary_title", required = true) String diary_title,
			@RequestParam(name = "diary_year", required = true) String diary_year,
			@RequestParam(name = "diary_month", required = true) String diary_month,
			@RequestParam(name = "diary_day", required = true) String diary_day,
			@RequestParam(name = "diary_content", required = true) String diary_content,
			@RequestParam(name = "diary_feeling", required = true) String diary_feeling,
			@RequestParam(name = "diary_weather", required = true) String diary_weather,
			@RequestParam(name = "diary_activity", required = true) String diary_activity,
			@RequestParam(name = "pet_id", required = false) List<String> pet_id,
			@RequestParam(name = "user_id", required = true) String user_id) throws Exception {
		try {
			DiaryDto diary = new DiaryDto();
			diary.setDiary_id(diary_id);
			diary.setDiary_title(diary_title);
			diary.setDiary_year(diary_year);
			diary.setDiary_month(diary_month);
			diary.setDiary_day(diary_day);
			diary.setDiary_content(diary_content);
			diary.setDiary_feeling(diary_feeling);
			diary.setDiary_weather(diary_weather);
			diary.setDiary_activity(diary_activity);

			diaryService.updateDiary(diary);

			diaryService.deletePetTags(diary_id); // 펫 태그 다 지우고
			// 들어온거 새로 등록 처리
			if (pet_id != null) {

				DiaryPetTagDto ptg = new DiaryPetTagDto();
				for (int i = 0; i < pet_id.size(); i++) {

					ptg.setUser_id(Integer.parseInt(user_id));
					ptg.setDiary_id(diary.getDiary_id());
					ptg.setPet_id(Integer.parseInt(pet_id.get(i)));

					diaryService.registerPetTags(ptg);
				}
			}

			return new ResponseEntity<String>(SUCC, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
		}
	}

	@DeleteMapping("/deleteDiary/{diary_id}")
	public ResponseEntity<String> diaryDelete(@PathVariable int diary_id) throws Exception {
		diaryService.deleteDiary(diary_id);
		if (diary_id != 0)
			return new ResponseEntity<String>(SUCC, HttpStatus.OK);
		return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
	}

	@PostMapping("/newbg")
	public String insertDiaryBg(@RequestPart(name = "filesupload", required = true) List<MultipartFile> filesupload,
			@RequestParam(name = "user_id", required = true) int user_id) throws Exception {
		
			BackgroundDto bg = new BackgroundDto();
			if (filesupload != null && filesupload.size() != 0) {
				for (int i = 0; i < filesupload.size(); i++) {
					String sourceFileName = filesupload.get(i).getOriginalFilename(); // 파일 원본 이름
					String sourceFileNameExtension = FilenameUtils.getExtension(sourceFileName).toLowerCase();
					File destinationFile;
					String destinationFileName;

					String fileUrl = "/home/ubuntu/images/";	
//					String fileUrl = "C:/Petmily/frontend/src/assets/images/DiaryBgImg/";
					destinationFileName = RandomStringUtils.randomAlphanumeric(32) + "." + sourceFileNameExtension; 
			        destinationFile = new File(fileUrl + destinationFileName); 
			        if(!destinationFile.exists()) {
			        	destinationFile.getParentFile().mkdirs(); 
			        }
			        filesupload.get(i).transferTo(destinationFile); 

					bg.setUser_id(user_id);
					bg.setFile_name(destinationFileName);
					bg.setFile_oriname(sourceFileName);
					bg.setFile_url(fileUrl);
					
					if (diaryFileService.selectDiaryBg(user_id) == null)
						diaryFileService.insertDiaryBg(bg);
					else {
						diaryFileService.deleteDiaryBg(user_id);
						diaryFileService.insertDiaryBg(bg);}
				}

			}
			return bg.getFile_name();
	}

	@GetMapping("/getbg/{user_id}")
	public BackgroundDto selectDiaryBg(@PathVariable int user_id) throws Exception {
		BackgroundDto bg = diaryFileService.selectDiaryBg(user_id);
		return bg;
	}

	@PutMapping("/updatebg")
	public String updateDiaryBg(@RequestPart(name = "filesupload", required = true) List<MultipartFile> filesupload,
			@RequestParam(name = "user_id", required = true) int user_id) throws Exception {

		BackgroundDto bg = new BackgroundDto();
		if (filesupload != null && filesupload.size() != 0) {
			diaryFileService.deleteDiaryBg(user_id);
			for (int i = 0; i < filesupload.size(); i++) {
				String sourceFileName = filesupload.get(i).getOriginalFilename(); // 파일 원본 이름
				String sourceFileNameExtension = FilenameUtils.getExtension(sourceFileName).toLowerCase();
				File destinationFile;
				String destinationFileName;

				String fileUrl = "/home/ubuntu/images/";	
//				String fileUrl = "C:/Petmily/frontend/src/assets/images/DiaryBgImg/";
				destinationFileName = RandomStringUtils.randomAlphanumeric(32) + "." + sourceFileNameExtension; 
		        destinationFile = new File(fileUrl + destinationFileName); 
		        if(!destinationFile.exists()) {
		        	destinationFile.getParentFile().mkdirs(); 
		        }
				filesupload.get(i).transferTo(destinationFile);

				bg.setUser_id(user_id);
				bg.setFile_name(destinationFileName);
				bg.setFile_oriname(sourceFileName);
				bg.setFile_url(fileUrl);
				diaryFileService.insertDiaryBg(bg);
				System.out.println(bg.toString());
			}
		}
		return bg.getFile_name();

	}

	// http://localhost:8000/diary/deletebg/5
	@DeleteMapping("/deletebg/{user_id}")
	public ResponseEntity<String> deleteDiaryBg(@PathVariable(name = "user_id") int user_id) throws Exception {

		if (diaryFileService.deleteDiaryBg(user_id)) {
			return new ResponseEntity<String>(SUCC, HttpStatus.OK);
		}
		return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
	}
	
	// http://localhost:8000/diary/deletediarybyuserid/5
	@DeleteMapping("/deletediarybyuserid/{user_id}")
	public boolean diaryDeleteByUserId(@PathVariable(name = "user_id") int user_id) throws Exception {
		boolean result = diaryService.deleteDiaryByUserId(user_id);
		return result;
	}
	
	// http://localhost:8000/diary/deletediaryfilebyuserid/5
	@DeleteMapping("/deletediaryfilebyuserid/{user_id}")
	public boolean diaryFileDeleteByUserId(@PathVariable(name = "user_id") int user_id) throws Exception {
		boolean result = diaryFileService.deleteFileUserId(user_id);
		return result;
	}
}
