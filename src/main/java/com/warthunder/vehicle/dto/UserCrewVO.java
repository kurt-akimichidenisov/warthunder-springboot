package com.warthunder.vehicle.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserCrewVO {
    private Integer id;
    private String name;
    private Integer crewRole;
    private List<AssignedVehicleVO> assignedVehicles;

    @Data
    @Builder
    public static class AssignedVehicleVO {
        private Integer id;
        private String vehicleName;
    }
}