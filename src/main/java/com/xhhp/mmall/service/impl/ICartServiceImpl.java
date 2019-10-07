package com.xhhp.mmall.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.xhhp.mmall.common.Const;
import com.xhhp.mmall.common.ServerResponse;
import com.xhhp.mmall.dao.CartMapper;
import com.xhhp.mmall.dao.ProductMapper;
import com.xhhp.mmall.pojo.Cart;
import com.xhhp.mmall.pojo.Product;
import com.xhhp.mmall.service.ICartService;
import com.xhhp.mmall.util.BigDecimalUtil;
import com.xhhp.mmall.vo.CartProductVo;
import com.xhhp.mmall.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * ICartServiceImpl class
 *
 * @author Flc
 * @date 2019/9/28
 */
@Service("iCartService")
@PropertySource(value = {"classpath:/application-${spring.profiles.active}.properties"})
public class ICartServiceImpl implements ICartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Value("${ftp.server.http.prefix}")
    private String ftpPrefix;

    @Override
    public ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count) {
        Cart cart = cartMapper.selectCartByUserIdProductId(userId, productId);
        if(cart == null) {
            //产品不在购物车里
            Cart carItem = new Cart();
            carItem.setQuantity(count);
            carItem.setUserId(userId);
            carItem.setChecked(Const.Cart.CHECKED);
            carItem.setProductId(productId);
            cartMapper.insert(carItem);
        } else {
            //产品已经在购物车里了
            count = cart.getQuantity() + count;
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        CartVo cartVo = this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(cartVo);
    }

    @Override
    public ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count) {
        if(productId == null || count == null) {
            return ServerResponse.createByERRORMessage("参数错误");
        }
        Cart cart = cartMapper.selectCartByUserIdProductId(userId, productId);
        if(cart != null) {
            cart.setQuantity(count);
        }
        cartMapper.updateByPrimaryKeySelective(cart);
        CartVo cartVo = this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(cartVo);
    }

    @Override
    public ServerResponse<CartVo> delete(Integer userId, String productIds) {
        //Guava使用逗号分割并转成集合
        List<String> productList = Splitter.on(",").splitToList(productIds);
        if(CollectionUtils.isEmpty(productList)) {
            return ServerResponse.createByERRORMessage("参数错误");
        }
        cartMapper.deleteByUserIdProducts(userId,productList);
        CartVo cartVo = this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(cartVo);
    }

    @Override
    public ServerResponse<CartVo> list(Integer userId) {
        CartVo cartVo = this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(cartVo);
    }

    @Override
    public ServerResponse<CartVo> selectOrUnSelect(Integer userId, Integer checked, Integer productId) {
        cartMapper.checkedOrUncheckedAllProduct(userId, checked, checked);
        return this.list(userId);
    }

    @Override
    public ServerResponse<Integer> getCartProductCount(Integer userId) {
        return ServerResponse.createBySuccess(cartMapper.selectCartProductCount(userId));
    }

    private CartVo getCartVoLimit(Integer userId) {
        CartVo cartVo = new CartVo();
        List<Cart> cartList = cartMapper.selectCartByUserId(userId);
        List<CartProductVo> cartProductVoList = Lists.newArrayList();

        BigDecimal cartTotalPrice = new BigDecimal("0");

        if(!CollectionUtils.isEmpty(cartList)) {
            for(Cart cart:cartList) {
                CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setId(cart.getId());
                cartProductVo.setProductId(cart.getProductId());
                cartProductVo.setUserId(cart.getUserId());

                Product product = productMapper.selectByPrimaryKey(cart.getProductId());
                if(product != null) {
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductSubtitle(product.getSubtitle());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductStock(product.getStock());

                    //判断库存
                    int buyLimitCount = 0;
                    if(product.getStock() >= cart.getQuantity()) {
                        buyLimitCount = cart.getQuantity();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                    } else {
                        buyLimitCount = product.getStock();
                        Cart cart1 = new Cart();
                        cart1.setId(cart.getId());
                        cart1.setQuantity(buyLimitCount);
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
                        cartMapper.updateByPrimaryKeySelective(cart);
                    }
                    cartProductVo.setQuantity(buyLimitCount);

                    //计算总价
                    cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(),cart.getQuantity()));
                    cartProductVo.setProductChecked(cart.getChecked());
                }

                if(cart.getChecked() == Const.Cart.CHECKED) {
                    //如果勾选，就计算总价
                    cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(), cartProductVo.getProductTotalPrice().doubleValue());

                }
                cartProductVoList.add(cartProductVo);
            }
        }
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setCartProductVo(cartProductVoList);
        cartVo.setAllchecked(getAllCheckedStatus(userId));
        cartVo.setImageHost(ftpPrefix);
        return cartVo;
    }

    private boolean getAllCheckedStatus(Integer userId) {
        if(userId == null) {
            return false;
        }
        return cartMapper.selectCartProductCheckedStatusByUserId(userId) == 0;
    }
}
