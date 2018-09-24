package com.taotao.cart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.taotao.common.service.ApiService;


@RequestMapping("order")
@Controller
public class OrderController {
	
	@Autowired
	private ApiService apiService;
	
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public void createOrder() throws Exception {
		String url = "http://order.taotao.com/api/order/create.html";
		apiService.doPostHaveJsonParams(url, null);
	}
}
