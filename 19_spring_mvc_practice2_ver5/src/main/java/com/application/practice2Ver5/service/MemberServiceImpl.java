package com.application.practice2Ver5.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.application.practice2Ver5.dao.MemberDAO;
import com.application.practice2Ver5.dto.MemberDTO;

@Service
public class MemberServiceImpl implements MemberService {
	
	@Value("${file.repo.path}")
	private String fileRepositoryPath;
	
	@Autowired
	private MemberDAO memberDAO;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public static Logger logger = LoggerFactory.getLogger(MemberDAO.class);

	@Override
	public void createMember(MultipartFile uploadProfile, MemberDTO memberDTO) throws Exception, IOException {

		// 파일 업로드하기
		if (!uploadProfile.isEmpty()) {
			String originalFilename = uploadProfile.getOriginalFilename();
			memberDTO.setProfileOriginalName(originalFilename);

			UUID uuid = UUID.randomUUID();
			String extenstion = originalFilename.substring(originalFilename.lastIndexOf("."));
			String profileUUID = extenstion + uuid;
			memberDTO.setProfileUUID(profileUUID);

			uploadProfile.transferTo(new File(fileRepositoryPath + profileUUID));

		}

		if (memberDTO.getSmsstsYn() == null)
			memberDTO.setSmsstsYn("n");
		if (memberDTO.getEmailstsYn() == null)
			memberDTO.setEmailstsYn("n");

		// 패스워드 암호화
		String encodingPasswd = passwordEncoder.encode(memberDTO.getPasswd());
		memberDTO.setPasswd(encodingPasswd);

		memberDAO.createMember(memberDTO);

	}

	@Override
	public String checkValidId(String memberId) {
		String dupleCheckId = "n";// 중복이아니다
		if(memberDAO.getChekcId(memberId) != null) {// 중복이다
			dupleCheckId = "y";
		}
		return dupleCheckId;
	}

	@Override
	public boolean isLogin(MemberDTO memberDTO) {
		boolean isCehckLogin = false;
		MemberDTO loginData = memberDAO.isLogin(memberDTO.getMemberId());
		
		if(loginData != null) {
			//패스워드 암호화 비교하기 & activeYn 은 'Y'가나와야 한다
			if(passwordEncoder.matches(memberDTO.getPasswd(), loginData.getPasswd()) &&
			   loginData.getActiveYn().equals("y")) {
				isCehckLogin = true;
			}
		}
		return isCehckLogin;
	}

	@Override
	public MemberDTO getMemberDetail(String memberId) {
		
		return memberDAO.getMemberDetail(memberId);
	}

	@Override
	public void updateMember(MultipartFile uploadProfile, MemberDTO memberDTO) throws Exception, IOException {
		
		if(!uploadProfile.isEmpty()) {
			File deleteFile = new File(fileRepositoryPath+uploadProfile);
			deleteFile.delete();
			
			String originalFilename = uploadProfile.getOriginalFilename();
			memberDTO.setProfileOriginalName(originalFilename);
			
			UUID uuid = UUID.randomUUID();
			String extenstion = originalFilename.substring(originalFilename.lastIndexOf("."));
			String profileUUID = uuid + extenstion;
			memberDTO.setProfileUUID(profileUUID);
			
			uploadProfile.transferTo(new File(fileRepositoryPath+profileUUID));
		}
		
		if(memberDTO.getSmsstsYn() == null) memberDTO.setSmsstsYn("n");
		if(memberDTO.getEmailstsYn() == null) memberDTO.setEmailstsYn("n");
		
		memberDAO.updateMember(memberDTO);
		
	}

	@Override
	public void udpateInactiveMember(String memberId) {
		
		memberDAO.udpateInactiveMember(memberId);
		
	}

	@Override
	@Scheduled(cron = "59 59 23 * * *")
	public void updateTodayMemberCnt() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today = sdf.format(new Date());
		 int todayMemberCnt = memberDAO.updateTodayMemberCnt(today);
	}

	

}
