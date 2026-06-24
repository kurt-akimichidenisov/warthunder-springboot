package com.warthunder.vehicle.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class UserVehicleVO {
    private Integer id;
    private String unitId;
    private String namePrefix;
    private String vehicleName;
    private String mainRole;
    private String unitCountry;
    private String vehicleRank;
    private BigDecimal br;
    private List<AssignedCrewVO> assignedCrew;

    @Data
    @Builder
    public static class AssignedCrewVO {
        private Integer id;
        private String name;
    }
}