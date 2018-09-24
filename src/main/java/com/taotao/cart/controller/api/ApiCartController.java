package com.taotao.cart.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.taotao.cart.bean.Cart;
import com.taotao.cart.service.CartService;

@RequestMapping("api/cart")
@Controller
public class ApiCartController {

    @Autowired
    private CartService cartService;

    /**
     * 对外提供接口服务，根据用户id查询购物车列表.
     * 这里不需要判断用户是否已经登录，已经传递了userId过来，你也无法判断这个UserId的真伪，只能去查。
     */
    @RequestMapping(value = "{userId}", method = RequestMethod.GET)
    public ResponseEntity<List<Cart>> queryCartListByUserId(@PathVariable("userId") Long userId) {
        try {
            List<Cart> carts = this.cartService.queryCartlistByUserId(userId);
            if (null == carts || carts.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(carts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

}
