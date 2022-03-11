package com.dh.hotblue.user.entity;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@SequenceGenerator(name = "user_seq", sequenceName = "user_seq", initialValue = 1, allocationSize = 1)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="users")
public class UserEntity {
	
	@Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
	private Long userId;
	
	@ApiModelProperty(value="닉네임")
	@Column(unique=true)
	private String nickname;
	
	@ApiModelProperty(value="비밀번호")
	private String password;
	
	@ApiModelProperty(value="SNS 로그인 ProviderId", 
			notes="회원가입 시, 암호화 해서 password컬럼에 저장")
	@Column(unique=true)
	private String providerId;
	
	@ApiModelProperty(value="권한")
	private String roles = "ROLE_USER";
	
	private String refreshToken;
	
	public List<String> getRoleList() {
		if(this.roles.length() > 0) {
			return Arrays.asList(this.roles.split(","));
		}
		return new ArrayList<>();
	}
}