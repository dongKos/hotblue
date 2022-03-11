package com.dh.hotblue.user.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.dh.hotblue.user.entity.UserEntity;



public interface UserRepository extends CrudRepository<UserEntity, Long>{

	Optional<UserEntity> findByNickname(String nickname);

}
