package com.warthunder.vehicle.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PlayerStatsVO {
    private List<VehicleStatsItem> vehicleStats;
    private List<CrewStatsItem> crewStats;
    private List<PlayerRankVO> rows;
    private Long total;
}