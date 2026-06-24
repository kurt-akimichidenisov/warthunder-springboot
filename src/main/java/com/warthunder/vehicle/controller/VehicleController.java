package com.warthunder.vehicle.controller;

import com.warthunder.vehicle.common.Result;
import com.warthunder.vehicle.common.PageResult;
import com.warthunder.vehicle.dto.VehicleQueryDTO;
import com.warthunder.vehicle.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/vehicles")
@Tag(name = "载具管理", description = "载具分页查询接口")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @GetMapping("/page")
    @Operation(summary = "分页查询载具")
    public Result page(VehicleQueryDTO query) {
        log.info("载具分页查询: page={}, pageSize={}, name={}, countries={}, roles={}, ranks={}, brMin={}, brMax={}, sortBy={}, sortOrder={}",
                query.getPage(), query.getPageSize(), query.getName(),
                query.getCountries(), query.getRoles(), query.getRanks(),
                query.getBrMin(), query.getBrMax(),
                query.getSortBy(), query.getSortOrder());

        PageResult<?> pageResult = vehicleService.page(query);
        return Result.success(pageResult);
    }
}