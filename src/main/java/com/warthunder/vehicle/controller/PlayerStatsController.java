package com.warthunder.vehicle.controller;

import com.warthunder.vehicle.common.Result;
import com.warthunder.vehicle.dto.PlayerStatsVO;
import com.warthunder.vehicle.service.PlayerStatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/players")
@Tag(name = "玩家统计", description = "玩家排行榜公开接口")
public class PlayerStatsController {

    @Autowired
    private PlayerStatsService playerStatsService;

    @GetMapping("/stats")
    @Operation(summary = "获取玩家统计与排行榜")
    public Result getPlayerStats(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "20") Integer pageSize) {

        log.info("查询玩家统计: pageNum={}, pageSize={}", pageNum, pageSize);
        PlayerStatsVO stats = playerStatsService.getPlayerStats(pageNum, pageSize);
        return Result.success(stats);
    }
}