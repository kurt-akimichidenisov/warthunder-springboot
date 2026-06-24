package com.warthunder.vehicle.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CrewDetailVO {
    private Integer id;
    private String name;
    private Integer crewRole;
    /** 已分配的载具（单个对象，一个乘员只能分配到一个载具） */
    private AssignedVehicleInfo assignedVehicle;

    @Data
    @Builder
    public static class AssignedVehicleInfo {
        private Integer userVehicleId;
        private String vehicleName;
    }
}