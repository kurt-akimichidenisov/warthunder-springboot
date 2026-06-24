package com.warthunder.vehicle.controller;

import com.warthunder.vehicle.common.Result;
import com.warthunder.vehicle.dto.AssignCrewDTO;
import com.warthunder.vehicle.dto.CreateCrewDTO;
import com.warthunder.vehicle.service.CrewService;
import com.warthunder.vehicle.util.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user/crew")
@Tag(name = "车组管理", description = "车组创建、分配、解除接口")
public class CrewController {

    @Autowired
    private CrewService crewService;

    @GetMapping("/list")
    @Operation(summary = "获取所有车组")
    public Result getUserCrewList() {
        Integer userId = UserContext.getUserId();
        if (userId == null) {
            return Result.error("未登录");
        }
        log.info("查询所有乘员: userId={}", userId);
        List<?> list = crewService.getUserCrewList(userId);
        return Result.success(list);
    }

    @PostMapping
    @Operation(summary = "创建车组")
    public Result createCrew(@Valid @RequestBody CreateCrewDTO dto) {
        Integer userId = UserContext.getUserId();
        if (userId == null) {
            return Result.error("未登录");
        }
        log.info("创建乘员: userId={}, name={}, crewRole={}", userId, dto.getName(), dto.getCrewRole());
        Object result = crewService.createCrew(userId, dto);
        return Result.success(result);
    }

    @PostMapping("/assign")
    @Operation(summary = "分配车组到载具")
    public Result assignCrew(@Valid @RequestBody AssignCrewDTO dto) {
        Integer userId = UserContext.getUserId();
        if (userId == null) {
            return Result.error("未登录");
        }
        log.info("分配乘员: userId={}, crewId={}, userVehicleId={}",
                userId, dto.getCrewId(), dto.getUserVehicleId());
        crewService.assignCrew(userId, dto);
        return Result.success("分配成功");
    }

    @PostMapping("/unassign")
    @Operation(summary = "解除车组分配")
    public Result unassignCrew(@RequestBody AssignCrewDTO dto) {
        Integer userId = UserContext.getUserId();
        if (userId == null) {
            return Result.error("未登录");
        }
        log.info("解除分配: userId={}, crewId={}", userId, dto.getCrewId());
        crewService.unassignCrew(userId, dto.getCrewId());
        return Result.success("解除成功");
    }
}