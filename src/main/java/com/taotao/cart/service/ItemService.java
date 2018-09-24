package com.taotao.cart.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cart.bean.Item;
import com.taotao.common.service.ApiService;


@Service
public class ItemService {
	
	@Autowired
	private ApiService apiService;
	
	@Value("${TAOTAO_MANAGE_URL}")
	private String TAOTAO_MANAGE_URL;
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	/**
	 * 根据商品id查询商品详情。
	 * 提问：为什么是通过httpClient来访问后台系统获取到商品详情，而不是cart系统直连数据库查询tb_item表？
	 * 因为职责划分问题，cart系统只负责购物车相关。
	 * @param itemId
	 * @return
	 */
	public Item queryItemById(Long itemId) {
		try {
			String url = TAOTAO_MANAGE_URL + "/rest/api/item/" + itemId;
			String jsonData = apiService.doGet(url);
			if(StringUtils.isNotEmpty(jsonData)){
				return MAPPER.readValue(jsonData, Item.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}

}
