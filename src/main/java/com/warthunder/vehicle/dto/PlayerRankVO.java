package com.warthunder.vehicle.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlayerRankVO {
    private Integer id;
    private String username;
    private String avatar;
    private Integer gender;
    private Long vehicleCount;
    private Long crewCount;
}