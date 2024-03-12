package com.application.practice2Ver5.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.application.practice2Ver5.dto.MemberDTO;

public interface MemberService {

	public void createMember(MultipartFile uploadProfile, MemberDTO memberDTO) throws Exception, IOException;

	public String checkValidId(String memberId);

	public boolean isLogin(MemberDTO memberDTO);

	public MemberDTO getMemberDetail(String memberId);

	public void updateMember(MultipartFile uploadProfile, MemberDTO memberDTO) throws Exception, IOException;

	public void udpateInactiveMember(String memberId);
	
	public void updateTodayMemberCnt();
	//public void deleteMemberScheduler(); ->여기서부터

}
