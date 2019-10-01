package com.xhhp.mmall.service;

import com.xhhp.mmall.common.ServerResponse;
import com.xhhp.mmall.pojo.Shipping;

public interface ShippingService {

    ServerResponse add(Integer userId, Shipping shipping);

    ServerResponse delete(Integer userId, Integer shippingId);

    ServerResponse update(Integer userId, Shipping shipping);

    ServerResponse select(Integer userId, Integer shippingId);

    ServerResponse list(Integer userId, Integer pageSize, Integer pageNum);
}
