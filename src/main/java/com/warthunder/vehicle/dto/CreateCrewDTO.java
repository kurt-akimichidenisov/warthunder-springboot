package com.warthunder.vehicle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCrewDTO {

    @NotBlank(message = "乘员名称不能为空")
    private String name;

    /** 0=车长, 1=炮手, 2=驾驶员, 3=装填手, 4=通信员 */
    @NotNull(message = "乘员角色不能为空")
    private Integer crewRole;
}