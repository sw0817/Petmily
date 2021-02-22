package com.petmily.curation.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.petmily.curation.dto.PetDto;
import com.petmily.curation.dto.PetTagDto;

@Mapper
public interface PetMapper {
	public boolean insertPet(PetDto pet) throws Exception; // c
	public List<PetDto> selectPet(int no) throws Exception; // r
	public PetDto selectPetByPetId(int pet_id)throws Exception;
	public boolean updatePet(PetDto pet) throws Exception; // u
	public boolean deletePet(int no) throws Exception; // d
	public List<PetDto> selectAllPet() throws Exception; // r
	public boolean updatePetTags(PetTagDto pettag) throws Exception;
	
	public boolean deleteDiaryPettag(int user_id) throws Exception; // d
	public boolean deletePetByUserId(int user_id) throws Exception; // d
}
