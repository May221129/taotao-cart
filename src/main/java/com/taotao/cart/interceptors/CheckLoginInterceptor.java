package com.taotao.cart.interceptors;

import org.springframework.stereotype.Component;
import com.taotao.common.interceptor.CheckoutIsLoginInterceptor;

/**
 * 拦截器：将所有需要做登录校验的请求都拦截下来，在这里统一做登录校验。
 * 继承common中的拦截器，用父类中的preHandle()方法做登录校验。
 */
@Component
public class CheckLoginInterceptor extends CheckoutIsLoginInterceptor{

//	public static final String COOKIE_TOKEN = "TAOTAO_TOKEN";// cookie中token的key

//	@Value("${TAOTAO_SSO_URL}")
//	private String TAOTAO_SSO_URL;
//
//	@Autowired
//	private ApiService apiService;
//
//	private static final ObjectMapper MAPPER = new ObjectMapper();
//
//	/**
//	 * 验证是否登录:让sso端完成token的验证。
//	 */
//	@Override
//	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
//			throws Exception {
//		if (handler instanceof HandlerMethod) {
//			if(null != request.getCookies()){
//				for (Cookie cookie : request.getCookies()) {
//					// 看cookie中是否有token：
//					if (cookie.getName().equalsIgnoreCase(COOKIE_TOKEN)) {
//						String result = this.apiService.doGet(TAOTAO_SSO_URL + "/service/user/checkoutToken");
//						if (StringUtils.isNotEmpty(result)) {
//							User user = MAPPER.readValue(result, User.class);
//							request.setAttribute("user", user);
//						}
//						return true;//用户登录了，要放行。
//					}
//				}
//			}
//			return true;//用户没有登录，也要放行：
//		}
//		return false;
//	}
	
}
