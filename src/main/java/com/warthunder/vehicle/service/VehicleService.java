package com.warthunder.vehicle.service;

import com.warthunder.vehicle.common.PageResult;
import com.warthunder.vehicle.dto.VehicleQueryDTO;

public interface VehicleService {
    /**
     * 分页查询载具
     */
    PageResult<?> page(VehicleQueryDTO query);
}