package com.dh.hotblue.code.property;

public interface CodeProperty {
	
	//BR01 - 버디 유형
	String buddyNickname = "BR0101";
	String buddyContact = "BR0102";
	String buddyYoutuber = "BR0103";
	String buddyBookmark = "BR0104";
	
	//BR02 - 버디 요청 목록 유형
	String buddyRequestRecieve = "BR0201";
	String buddyRequestRequest = "BR0202";
	
	//BR03 - 버디 상태
	String myAccept = "BR0301";	//상대방의 요청을 수락할까요?
	String buddyAccept = "BR0302";	//버디의 수락을 기다리고 있어요
	String buddy = "BR0303";	//이미 맺은 버디입니다
	String notBuddy = "BR0304";	//버디가아닙니다
	
	//BT01 - 버디 매수/매도 조회 유형
	String buddyTradingsAll = "BT0301";
	String buddyTradingsBuy = "BT0302";
	String buddyTradingsSell = "BT0303";
	
	//TC01 - 증권사 유형
	String tradingNH = "TC0101";
	String tradingKB = "TC0102";
	String tradingHanHwa = "TC0103";
	String tradingHankookBankis = "TC0104";
	String tradingSinhan = "TC0105";
	String tradingYoojin = "TC0106";
	String tradingKeeUm = "TC0107";
	String tradingYouAnta = "TC0108";
	String tradingIBK = "TC0109";
	String tradingDaesinCrayon = "TC0110";
	String tradingMiraeAsset = "TC0111";
	String tradingSamsung = "TC0112";
	
	//TC02 - 코스피 / 코스닥
	String kospi = "TC0201";	//코스피 
	String kosdak = "TC0202";	//코스닥 
	
	//TC03 - 주식 종목 구분
	String stockNormal = "TC0301";	//일반
	String stockETF = "TC0302";		//ETF
	String stockETN = "TC0303";		//ETN
	
	//전화번호 인증 만료시간 - 5분
	int PHONE_CERTI_EXPIRE_TIME = 60 * 5 * 1000;
	
	//서버 공통 코드
	String successMessage = "요청 성공";
	int HttpStatusOk = 200; 
	
	//Exception
	String USER_NOT_FOUND = "존재하지 않는 사용자입니다.";
	String REQUEST_NOT_FOUND = "요청을 찾을 수 없습니다.";
	String NOTICE_NOT_FOUND = "공지사항을 찾을 수 없습니다.";
	String ALREADY_EXIST = "이미 존재하는 값입니다.";
	String REVERSE_BUDDY_REQUEST_EXIST = "상대방에게 받은 요청이 있습니다.";
	String REFRESH_TOKEN_NOT_FOUND = "RefreshToken이 존재하지 않습니다.";
	String REFRESH_TOKEN_UNVALID = "RefreshToken이 유효하지 않습니다.";
	
	//페이지 노출 개수
	int PAGING_SIZE = 50;
	
	
}
