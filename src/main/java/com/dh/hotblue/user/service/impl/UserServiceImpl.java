package com.dh.hotblue.user.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.dh.hotblue.user.entity.UserEntity;
import com.dh.hotblue.user.payload.UserDto.UserRequest;
import com.dh.hotblue.user.repository.UserRepository;
import com.dh.hotblue.user.service.UserService;
import com.dh.hotblue.util.CommonUtil;
import com.dh.hotblue.util.MyObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Service
public class UserServiceImpl implements UserService {

	@Autowired	JPAQueryFactory jpaQueryFactory;
	@Autowired	UserRepository userRepository;
	@Autowired	BCryptPasswordEncoder encoder;
	@Autowired 	CommonUtil commonUtil;
	
	@Override
	public UserEntity save(UserRequest userReq) {
		UserEntity userEntity = new UserEntity();
		ObjectMapper mapper = new MyObjectMapper();
		userEntity = mapper.convertValue(userReq, UserEntity.class);
		userEntity.setPassword(encoder.encode(userEntity.getProviderId()));
		userEntity = userRepository.save(userEntity);
		return userEntity;
	}

}
