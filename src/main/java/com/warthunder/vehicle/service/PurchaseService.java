package com.warthunder.vehicle.service;

import com.warthunder.vehicle.dto.PurchaseRequestDTO;

public interface PurchaseService {

    /**
     * 购买载具
     * @param userId 当前用户ID
     * @param dto 购买请求
     * @return 购买成功的user_vehicle记录
     */
    Object purchase(Integer userId, PurchaseRequestDTO dto);
}