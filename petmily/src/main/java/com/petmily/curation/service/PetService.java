package com.petmily.curation.service;

import java.util.List;

import com.petmily.curation.dto.PetDto;
import com.petmily.curation.dto.PetTagDto;

public interface PetService {
	public boolean insertPetService(PetDto pet) throws Exception; // c
	public List<PetDto> selectPetService(int no) throws Exception; // r
	public PetDto selectPetByPetId(int pet_id)throws Exception;
	public boolean updatePetService(PetDto pet) throws Exception; // u
	public boolean deletePetService(int no) throws Exception; // d
	public List<PetDto> selectAllPetService() throws Exception; // r
	public boolean updatePetTags(PetTagDto pettag) throws Exception;
	
	public boolean deleteDiaryPettag(int user_id) throws Exception; // d
	public boolean deletePetByUserId(int user_id) throws Exception; // d
}
