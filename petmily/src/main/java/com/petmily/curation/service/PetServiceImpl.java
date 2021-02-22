package com.petmily.curation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petmily.curation.dto.PetDto;
import com.petmily.curation.dto.PetTagDto;
import com.petmily.curation.mapper.PetMapper;

@Service
public class PetServiceImpl implements PetService{

	@Autowired
	PetMapper petMapper;
	
	@Override
	public boolean insertPetService(PetDto pet) throws Exception {
		return petMapper.insertPet(pet);
	}

	@Override
	public List<PetDto> selectPetService(int no) throws Exception {
		return petMapper.selectPet(no);
	}

	@Override
	public boolean updatePetService(PetDto pet) throws Exception {
		return petMapper.updatePet(pet);
	}

	@Override
	public boolean deletePetService(int no) throws Exception {
		return petMapper.deletePet(no);
	}
	@Override
	public List<PetDto> selectAllPetService() throws Exception {
		return petMapper.selectAllPet();
	}

	@Override
	public PetDto selectPetByPetId(int pet_id) throws Exception {
		return petMapper.selectPetByPetId(pet_id);
	}

	@Override
	public boolean updatePetTags(PetTagDto pettag) throws Exception {
		return petMapper.updatePetTags(pettag);
	}

	@Override
	public boolean deleteDiaryPettag(int user_id) throws Exception {
		return petMapper.deleteDiaryPettag(user_id);
	}

	@Override
	public boolean deletePetByUserId(int user_id) throws Exception {
		return petMapper.deletePetByUserId(user_id);
	}
	
}
