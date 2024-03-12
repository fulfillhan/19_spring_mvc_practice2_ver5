package com.application.practice2Ver5.dao;

import org.apache.ibatis.annotations.Mapper;

import com.application.practice2Ver5.dto.MemberDTO;

@Mapper
public interface MemberDAO {

	public void createMember(MemberDTO memberDTO);

	public String getChekcId(String memberId);

	public MemberDTO isLogin(String memberId);

	public MemberDTO getMemberDetail(String memberId);

	public void updateMember(MemberDTO memberDTO);

	public void udpateInactiveMember(String memberId);

	public int updateTodayMemberCnt(String today);
	

}
