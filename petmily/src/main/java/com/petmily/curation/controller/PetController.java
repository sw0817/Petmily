package com.petmily.curation.controller;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.petmily.curation.dto.PetDto;
import com.petmily.curation.dto.PetTagDto;
import com.petmily.curation.service.DiaryService;
import com.petmily.curation.service.FeedService;
import com.petmily.curation.service.PetService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@Controller
@RequestMapping("/pet")
public class PetController {
	final String SUCC = "SUCCESS";
	final String FAIL = "FAIL";

	@Autowired
	PetService petService;
	
	@Autowired
	FeedService feedService;
	
	@Autowired
	DiaryService diaryService;
	/*
	 * 결과 출력URL http://localhost:8000/pet/result
	 * 
	 * @RequestMapping("result") public String index5(HttpServletRequest req, Model
	 * model) throws Exception { int user_id =
	 * Integer.parseInt(req.getParameter("user_id")); // model.addAttribute("img",
	 * fileService.selectFileService(user_id)); System.out.println("사진 결과 확인	 + "
	 * + user_id); return "result"; }
	 */

	// 파일업로드URL http://localhost:8000/pet/uploadForm
	@GetMapping("uploadForm")
	public String uploadTest(Model model) {
		return "uploadForm";
	}

	// 조회 확인URL http://localhost:8000/pet/selectAll/2
	// 쿼리스트링: GetMapping("selectAll") -> @RequestParam("user_id") |
	// http://localhost:8000/pet/selectAll?user_id=2
	// restApi: @GetMapping("selectAll/{user_id}") -> @PathVariable("user_id")
	// 펫 정보 조회(R)
	@ResponseBody
	@GetMapping("selectAll/{user_id}")
	public List<PetDto> selectAllPet(@PathVariable("user_id") String user_id) throws NumberFormatException, Exception {
		List<PetDto> result = petService.selectPetService(Integer.parseInt(user_id));
		return result;
	}

	@ResponseBody
	@GetMapping("selectAll")
	public List<PetDto> selectAllPet() throws NumberFormatException, Exception {
		List<PetDto> result = petService.selectAllPetService();
		return result;
	}

	// 펫 정보 입력(C)
	// http://localhost:8000/pet/insertProc
	@ResponseBody
	@PostMapping("insertProc")
	public List<PetDto> InsertProc(Model model, @RequestParam(name = "user_id", required = true) String user_id,
			@RequestParam(name = "pet_name", required = false) String pet_name,
			@RequestParam(name = "pet_kind", required = false) String pet_kind,
			@RequestParam(name = "pet_gender", required = false) String pet_gender,
			@RequestParam(name = "pet_age", required = false) String pet_age,
			@RequestParam(name = "pet_content", required = false) String pet_content,
			@RequestPart(name = "fileupload", required = true) MultipartFile fileupload) throws Exception {


		int IntTypeUser_id = Integer.parseInt(user_id);
		String sourceFileName = fileupload.getOriginalFilename(); // 파일 원본 이름
		String sourceFileNameExtension = FilenameUtils.getExtension(sourceFileName).toLowerCase(); // 동일한 이름으로 업로드될때를

		File destinationFile;
		String destinationFileName;

		String fileUrl = "/home/ubuntu/images/";
//		String fileUrl = "C:/Petmily/frontend/src/assets/images/petImg/";

		destinationFileName = RandomStringUtils.randomAlphanumeric(32) + "." + sourceFileNameExtension;
		destinationFile = new File(fileUrl + destinationFileName);
		if (!destinationFile.exists()) {
			destinationFile.getParentFile().mkdirs();
		}
		fileupload.transferTo(destinationFile);

		// 펫 등록
		PetDto pet = new PetDto();
		pet.setPet_name(pet_name);
		pet.setUser_id(IntTypeUser_id); // front에서 로그인한 상태이기 때문에 전송요청시 user_id를 묶어서 보내주면 이곳에 할당
		pet.setPet_kind(pet_kind);
		pet.setPet_gender(pet_gender);
		pet.setPet_age(Integer.parseInt(pet_age));
		pet.setPet_content(pet_content);
		pet.setFile_name(destinationFileName);
		pet.setFile_oriname(sourceFileName);
		pet.setFile_url(fileUrl);

		petService.insertPetService(pet);

		List<PetDto> result = petService.selectPetService(IntTypeUser_id);
//		for (PetDto e : result) {
//			System.out.println(IntTypeUser_id + "의 펫 정보: " + e);
//		}

		// 이동해도 동작하지 않도록 해두었음
		return result;
	}

	// 펫 삭제와 업데이트는 pet_no로 하자; 프론트랑 상의 후 만들기

	// 내 프로필에서 펫 조회해서 가져올때 fileurl이랑 content만 묶어서 리턴하도록. select의 리턴 결과는 (fileurl,
	// content)

	@PutMapping("/petupdate/{pet_id}")
	public ResponseEntity<String> updatePet(@RequestParam(name = "pet_id", required = true) String pet_id,
			@RequestParam(name = "pet_name", required = false) String pet_name,
			@RequestParam(name = "pet_kind", required = false) String pet_kind,
			@RequestParam(name = "pet_gender", required = false) String pet_gender,
			@RequestParam(name = "pet_age", required = false) String pet_age,
			@RequestParam(name = "pet_content", required = false) String pet_content,
			@RequestPart(name = "fileupload", required = false) MultipartFile fileupload) throws Exception {
		
		PetDto prepet = petService.selectPetByPetId(Integer.parseInt(pet_id));
		int IntTypePet_id = Integer.parseInt(pet_id);
			PetDto pet = new PetDto();
			
			// 펫 이름 변경시 피드에 보이는 펫 이름도 변경 필요
			if(prepet.getPet_name()!=pet_name) {
				PetTagDto pettag = new PetTagDto();
				pettag.setPet_id(Integer.parseInt(pet_id));
				pettag.setPet_name(pet_name);
				petService.updatePetTags(pettag);
			}
			pet.setPet_name(pet_name);
			pet.setPet_id(IntTypePet_id);
			pet.setPet_kind(pet_kind);
			pet.setPet_gender(pet_gender);
			pet.setPet_age(Integer.parseInt(pet_age));
			pet.setPet_content(pet_content);
			
			pet.setFile_name(prepet.getFile_name());
			pet.setFile_oriname(prepet.getFile_oriname());
			pet.setFile_url(prepet.getFile_url());
			
		if(fileupload!=null) {
			String sourceFileName = fileupload.getOriginalFilename(); // 파일 원본 이름
			
			String sourceFileNameExtension = FilenameUtils.getExtension(sourceFileName).toLowerCase(); 
			// 동일한 이름으로 업로드될때를 대비해서 파일이름 중복되지 않게변환
			
			File destinationFile;
			String destinationFileName;
			
			String fileUrl = "/home/ubuntu/images/";
//			String fileUrl = "C:/Petmily/frontend/src/assets/images/PetImg/";
			
			destinationFileName = RandomStringUtils.randomAlphanumeric(32) + "." + sourceFileNameExtension;
			destinationFile = new File(fileUrl + destinationFileName);
			if (!destinationFile.exists()) {
				destinationFile.getParentFile().mkdirs();
			}
			fileupload.transferTo(destinationFile);
			
			pet.setFile_name(destinationFileName);
			pet.setFile_oriname(sourceFileName);
			pet.setFile_url(fileUrl);
		}
		
		boolean isUpdated = petService.updatePetService(pet);
		if (isUpdated) {
			return new ResponseEntity<String>(SUCC, HttpStatus.OK);
		}
		return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
	}

	@DeleteMapping("/petdelete/{pet_id}")
	public ResponseEntity<String> userDelete(@PathVariable int pet_id) throws Exception {
		boolean pet = petService.deletePetService(pet_id);
		// 피드 펫태그 정보 삭제
		boolean feedTag = feedService.deletePetTagsByPet(pet_id);
		// 다이어리 펫태그 정보 삭제 
		boolean diaryTag = diaryService.deletePetTagsByPet(pet_id);
		if (pet==true && feedTag==true && diaryTag==true) {
			return new ResponseEntity<String>(SUCC, HttpStatus.OK);
		}
		return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
	}
	
	// http://localhost:8000/pet/deletediarypettag/5
	@ApiOperation(value = "다이어리펫태그 삭제", notes = "탈퇴회원과 관련된 모든 다이어리펫태그 삭제", response = List.class)
	@DeleteMapping("deletediarypettag/{user_id}")
	@ResponseBody
	public boolean deleteDiaryPettagUserId(@PathVariable(name = "user_id") String user_id) throws Exception {
		boolean result=petService.deleteDiaryPettag(Integer.parseInt(user_id));
		return result;
	}
	
	
	// http://localhost:8000/pet/deletepetbyuserid/5
	@ApiOperation(value = "펫정보 삭제", notes = "탈퇴회원과 관련된 모든 펫정보 삭제", response = List.class)
	@DeleteMapping("deletepetbyuserid/{user_id}")
	@ResponseBody
	public boolean deletePetByUserId(@PathVariable(name = "user_id") String user_id) throws Exception {
		boolean result=petService.deletePetByUserId(Integer.parseInt(user_id));
		return result;
	}
}