package com.warthunder.vehicle.service;

import com.warthunder.vehicle.common.PageResult;
import com.warthunder.vehicle.dto.UserCrewVO;
import com.warthunder.vehicle.dto.UserVehicleVO;

public interface DashboardService {

    /** 获取当前用户的载具列表（分页） */
    PageResult<UserVehicleVO> getUserVehicles(Integer userId, Integer page, Integer pageSize,
                                              String sortBy, String sortOrder);

    /** 获取当前用户的乘员列表（分页） */
    PageResult<UserCrewVO> getUserCrew(Integer userId, Integer page, Integer pageSize);
}