package com.taotao.cart.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.taotao.common.interceptor.SupCheckoutIsLoginInterceptor;

@Component
public class CartCheckoutIsLoginInterceptor extends SupCheckoutIsLoginInterceptor {
	
	/**
	 * 拦截器：将所有访问购物车的请求都拦截下来。通过调用SupCheckoutIsLoginInterceptor父类中的preHandle()方法做登录校验。
	 * 父类中的校验结果是用户已登录，那么UserThreadLocal中就会被放入当前用户的用户信息。
	 * 父类中的校验结果是用户未登录，那么UserThreadLocal中就没有用户信息。
	 * 无论用户登录与否，都要放行，可访问接下来的controller层。
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 执行conmon中公共拦截器的preHandle()方法，验证用户是否登录：
		super.preHandle(request, response, handler);
		// 不管登录没，都要放行：
		return true;
	}
}
