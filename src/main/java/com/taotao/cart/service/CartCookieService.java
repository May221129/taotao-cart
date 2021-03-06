package com.taotao.cart.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cart.bean.Cart;
import com.taotao.cart.bean.Item;
import com.taotao.common.util.CookieUtils;

@Service
public class CartCookieService {

	public static final String COOKIE_CART = "TAOTAO_CART";// cookie中cart

	private static final Integer COOKIE_TIME = 60 * 60 * 24 * 30 * 12;//cart在cookie中保存一年的时间。

	private static final ObjectMapper MAPPER = new ObjectMapper();

	/**
	 * 添加商品到购物车 
	 * 逻辑：判断加入的商品在原有购物车中是否存在，如果存在数量相加，如果不存在，直接写入即可
	 */
	public void addItemToCart(Item item, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		List<Cart> cookieCart = this.queryCartlist(request);

		Cart cart = null;
		for (Cart c : cookieCart) {
			if (c.getItemId().longValue() == item.getId().longValue()) {
				// 该商品已经存在购物车中
				cart = c;
				break;
			}
		}

		if (null == cart) {// 不存在
			cart = new Cart();
			cart.setCreated(new Date());
			cart.setUpdated(cart.getCreated());
			cart.setItemId(item.getId());
			cart.setItemTitle(item.getTitle());
			cart.setItemPrice(item.getPrice());
			cart.setItemImage(StringUtils.split(item.getImage(), ',')[0]);
			cart.setNum(1); 
			// 商品加入购物车列表中
			cookieCart.add(cart);
		} else {// 存在
			cart.setNum(cart.getNum() + 1); // TODO 默认为1
			cart.setUpdated(new Date());
		}

		// 将购物车列表数据写入到cookie中【什么时候需要编码：在cookie中存储的数据中包含中文时。这里的java编码是UTF-8，所以直接开启即可。】
		CookieUtils.setCookie(request, response, COOKIE_CART, MAPPER.writeValueAsString(cookieCart), COOKIE_TIME, true);
	}

	public List<Cart> queryCartlist(HttpServletRequest request) throws Exception {
		//因为在上面的addItemToCart()方法中，将cart放入cookie时做了编码，所以取出cart时需要做解码，最后一个参数为true：
		String jsonData = CookieUtils.getCookieValue(request, COOKIE_CART, true);
		List<Cart> cookieCart = null;
		if (StringUtils.isEmpty(jsonData)) {
			cookieCart = new ArrayList<Cart>();
		} else {
			// 反序列化
			cookieCart = MAPPER.readValue(jsonData,
					MAPPER.getTypeFactory().constructCollectionType(List.class, Cart.class));
		}
		// TODO 排序

		// 返回List<Cart>對象：
		return cookieCart;
	}
	
	public void updateNum(Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        List<Cart> carts = this.queryCartlist(request);

        Cart cart = null;
        for (Cart c : carts) {
            if (c.getItemId().longValue() == itemId.longValue()) {
                // 该商品已经存在购物车中
                cart = c;
                break;
            }
        }

        if (cart != null) {
            cart.setNum(num);
            cart.setUpdated(new Date());
        } else {
            // 参数非法
            return;
        }

        // 将购物车列表数据写入到cookie中
        CookieUtils.setCookie(request, response, COOKIE_CART, MAPPER.writeValueAsString(carts), COOKIE_TIME,
                true);
    }

    public void delete(Long itemId, HttpServletRequest request, HttpServletResponse response) throws Exception {
       
    	List<Cart> carts = this.queryCartlist(request);

        Cart cart = null;
        for (Cart c : carts) {
            if (c.getItemId().longValue() == itemId.longValue()) {
                cart = c;
                carts.remove(c);
                break;
            }
        }
        if (cart == null) {
            return;
        }
        // 将购物车列表数据写入到cookie中
        CookieUtils.setCookie(request, response, COOKIE_CART, MAPPER.writeValueAsString(carts), COOKIE_TIME,
                true);
    }
	
}
