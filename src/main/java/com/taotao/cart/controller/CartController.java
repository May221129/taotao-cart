package com.taotao.cart.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.cart.bean.Cart;
import com.taotao.cart.bean.Item;
import com.taotao.cart.service.CartCookieService;
import com.taotao.cart.service.CartService;
import com.taotao.cart.service.ItemService;
import com.taotao.common.annotation.CheckoutToken;
import com.taotao.common.threadLocal.UserThreadLocal;
import com.taotao.sso.query.bean.User;

/**
 * 购物车。
 */
@RequestMapping("cart")
@Controller
public class CartController {

	@Autowired
	private ItemService itemService;

	@Autowired
	private CartService cartService;

	@Autowired
	private CartCookieService cartCookieService;

	/**
	 * 将商品加入购物车，添加成功后跳转到购物车列表页。
	 * url:http://cart.taotao.com/cart/add/${itemId}.html
	 * 写cookie涉及到response对象，读cookie涉及到request对象。
	 * @return String：视图名：如果是以"redirect:"开头就做重定向。
	 */
	@CheckoutToken
	@RequestMapping(value = "/add/{itemId}", method = RequestMethod.GET)
	public String addItemToCart(@PathVariable("itemId") Long itemId, HttpServletRequest request,
			HttpServletResponse response) {

		Item item = this.itemService.queryItemById(itemId);
		if (null != item) {// 存在该商品：
			User user = UserThreadLocal.get();
			if (null != user) {// 有登录：将商品放入数据库
				this.cartService.addItemToCart(item, user.getId());
			} else {// 没有登录：将商品放入cookie中
				try {
					this.cartCookieService.addItemToCart(item, request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// 重定向到购物车列表页面：
			return "redirect:/cart/list.html";
		} else {// 不存在该商品：
			return null;
		}
	}

	/**
	 * 根据用户id查看购物车详情 
	 * url:http://cart.taotao.com/cart/list.html
	 */
	@CheckoutToken
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView toCartByUserId(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("cart");
		List<Cart> cartList = null;
		
		User user = UserThreadLocal.get();
		if (null != user) {// 已登录：
			cartList = this.cartService.queryCartlistByUserId(user.getId());
		} else {// 未登录：
			try {
				cartList = this.cartCookieService.queryCartlist(request);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		mv.addObject("cartList", cartList);

		return mv;
	}
	
	/**
	 * 根据用户id更新购物车中某商品的数量。
	 */
	@CheckoutToken
	@RequestMapping(value = "update/num/{itemId}/{num}", method = RequestMethod.POST)
	public ResponseEntity<Void> updateNum(@PathVariable("itemId") Long itemId, @PathVariable("num") Integer num,
			HttpServletRequest request, HttpServletResponse response) {
		User user = UserThreadLocal.get();
		if (null == user) {
			// 未登录状态
			try {
				this.cartCookieService.updateNum(itemId, num, request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// 登录状态
			this.cartService.updateNum(itemId, num, user.getId());
		}
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	/**
	 * 根据商品id删除购物车中的该商品。
	 */
	@CheckoutToken
	@RequestMapping(value = "delete/{itemId}", method = RequestMethod.GET)
	public String delete(@PathVariable("itemId") Long itemId, HttpServletRequest request,
			HttpServletResponse response) {
		User user = UserThreadLocal.get();
		if (null == user) {
			// 未登录状态
			try {
				this.cartCookieService.delete(itemId, request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// 登录状态
			this.cartService.delete(itemId, user.getId());
		}
		// 重定向到购物车列表页面
		return "redirect:/cart/list.html";
	}

}
