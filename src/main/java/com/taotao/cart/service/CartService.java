package com.taotao.cart.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.abel533.entity.Example;
import com.taotao.cart.bean.Cart;
import com.taotao.cart.bean.Item;
import com.taotao.cart.mapper.CartMapper;

@Service
public class CartService {
	
	@Autowired
	private CartMapper cartMapper;
	
	/**
     * 添加商品到购物车
     * 逻辑：判断加入的商品在原有购物车中是否存在，如果存在数量相加，如果不存在，直接写入即可
     */
	public void addItemToCart(Item item, Long userId) {
		Cart record = new Cart();
		record.setUserId(userId);
		record.setItemId(item.getId());
		Cart cart = this.cartMapper.selectOne(record);
		if(null != cart){//如果在购物车中存在该商品
			cart.setNum(cart.getNum() + 1);// TODO
			cart.setUpdated(new Date());
			this.cartMapper.updateByPrimaryKey(cart);//这里的cart对象是从数据库查询出来的。
		}else{//不存在
			cart = new Cart();
			cart.setUserId(userId);
			cart.setItemId(item.getId());
			cart.setItemTitle(item.getTitle());
			cart.setItemPrice(item.getPrice());
			if(StringUtils.isNotEmpty(item.getImage())){
				cart.setItemImage(StringUtils.split(item.getImage(), ',')[0]);
			}
			cart.setNum(1);// TODO
			cart.setCreated(new Date());
			cart.setUpdated(cart.getCreated());
			this.cartMapper.insert(cart);
		}
	}
	
	/**
	 * 根据用户id查询购物车。（要求已登录）
	 */
	public List<Cart> queryCartlistByUserId(Long userId) {
		
		Example example = new Example(Cart.class);
        // 设置排序条件
        example.setOrderByClause("created DESC");
        // 设置查询条件
        example.createCriteria().andEqualTo("userId", userId);

        return this.cartMapper.selectByExample(example);
	}
	
	/**
	 * 更新购物车中商品的数量
	 * @param itemId
	 * @param num
	 * @param userId
	 */
	public void updateNum(Long itemId, Integer num, Long userId) {

        // 更新的数据
        Cart record = new Cart();
        record.setNum(num);
        record.setUpdated(new Date());

        // 更新的条件
        Example example = new Example(Cart.class);
        example.createCriteria().andEqualTo("itemId", itemId).andEqualTo("userId", userId);

        this.cartMapper.updateByExampleSelective(record, example);
    }

    public void delete(Long itemId, Long userId) {
        Cart record = new Cart();
        record.setItemId(itemId);
        record.setUserId(userId);
        this.cartMapper.delete(record);
    }
	
}
