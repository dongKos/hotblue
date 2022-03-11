package com.dh.hotblue.user.service;

import com.dh.hotblue.user.entity.UserEntity;
import com.dh.hotblue.user.payload.UserDto.UserRequest;

public interface UserService {

	UserEntity save(UserRequest user);


}
