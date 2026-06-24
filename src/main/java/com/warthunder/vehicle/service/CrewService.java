package com.warthunder.vehicle.service;

import com.warthunder.vehicle.dto.AssignCrewDTO;
import com.warthunder.vehicle.dto.CreateCrewDTO;
import com.warthunder.vehicle.dto.CrewDetailVO;

import java.util.List;

public interface CrewService {

    /**
     * 获取当前用户所有乘员（不分页，含已分配载具）
     */
    List<CrewDetailVO> getUserCrewList(Integer userId);

    /**
     * 创建乘员
     */
    CrewDetailVO createCrew(Integer userId, CreateCrewDTO dto);

    /**
     * 分配乘员到载具（如果已分配则先解除旧的）
     */
    void assignCrew(Integer userId, AssignCrewDTO dto);

    /**
     * 解除乘员分配
     */
    void unassignCrew(Integer userId, Integer crewId);
}