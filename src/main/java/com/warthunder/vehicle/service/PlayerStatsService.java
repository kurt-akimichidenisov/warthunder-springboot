package com.warthunder.vehicle.service;

import com.warthunder.vehicle.dto.PlayerStatsVO;

public interface PlayerStatsService {

    PlayerStatsVO getPlayerStats(Integer page, Integer pageSize);
}