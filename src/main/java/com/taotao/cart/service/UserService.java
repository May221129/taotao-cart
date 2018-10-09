package com.taotao.cart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.taotao.sso.query.api.service.QueryUserByTokenService;
import com.taotao.sso.query.bean.User;

@Service
public class UserService {
	
	@Autowired
	private QueryUserByTokenService queryUserByTokenService;
	
	public User queryUserByToken(String token){
		return this.queryUserByTokenService.queryUserByToken(token);
	}
	
}
