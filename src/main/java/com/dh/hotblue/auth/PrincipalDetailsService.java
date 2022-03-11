package com.dh.hotblue.auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dh.hotblue.user.entity.UserEntity;
import com.dh.hotblue.user.repository.UserRepository;


// /login 요청시 동작
@Service
public class PrincipalDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
		Optional<UserEntity> userEntity = userRepository.findByNickname(nickname);
		return new PrincipalDetails(userEntity.get());
	}

}
