package com.warthunder.vehicle.controller;

import com.warthunder.vehicle.common.Result;
import com.warthunder.vehicle.common.PageResult;
import com.warthunder.vehicle.service.DashboardService;
import com.warthunder.vehicle.util.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "个人面板", description = "用户载具、车组查询接口")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/vehicles")
    @Operation(summary = "获取用户载具列表")
    public Result getUserVehicles(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {

        Integer userId = UserContext.getUserId();
        if (userId == null) {
            return Result.error("未登录");
        }

        log.info("查询用户载具: userId={}, page={}, pageSize={}, sortBy={}, sortOrder={}",
                userId, pageNum, pageSize, sortBy, sortOrder);

        PageResult<?> pageResult = dashboardService.getUserVehicles(
                userId, pageNum, pageSize, sortBy, sortOrder);
        return Result.success(pageResult);
    }

    @GetMapping("/crew")
    @Operation(summary = "获取用户车组分页列表")
    public Result getUserCrew(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "20") Integer pageSize) {

        Integer userId = UserContext.getUserId();
        if (userId == null) {
            return Result.error("未登录");
        }

        log.info("查询用户乘员: userId={}, page={}, pageSize={}", userId, pageNum, pageSize);

        PageResult<?> pageResult = dashboardService.getUserCrew(userId, pageNum, pageSize);
        return Result.success(pageResult);
    }
}