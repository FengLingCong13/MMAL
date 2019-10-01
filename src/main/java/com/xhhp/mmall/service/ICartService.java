package com.xhhp.mmall.service;

import com.xhhp.mmall.common.ServerResponse;
import com.xhhp.mmall.vo.CartVo;

public interface ICartService {

    ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count);

    ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count);

    ServerResponse<CartVo> delete(Integer userId, String productIds);

    ServerResponse<CartVo> list(Integer userId);

    ServerResponse<CartVo> selectOrUnSelect(Integer userId, Integer checkedId, Integer productId);

    ServerResponse<Integer> getCartProductCount(Integer userId) ;

}
