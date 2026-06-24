package com.warthunder.vehicle.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignCrewDTO {

    @NotNull(message = "用户载具ID不能为空")
    private Integer userVehicleId;

    @NotNull(message = "乘员ID不能为空")
    private Integer crewId;
}