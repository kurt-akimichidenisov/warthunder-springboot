package com.warthunder.vehicle.controller;

import com.warthunder.vehicle.common.Result;
import com.warthunder.vehicle.dto.PurchaseRequestDTO;
import com.warthunder.vehicle.service.PurchaseService;
import com.warthunder.vehicle.util.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user/vehicles")
@Tag(name = "载具购买", description = "用户购买载具接口")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @PostMapping("/purchase")
    @Operation(summary = "购买载具")
    public Result purchase(@RequestBody PurchaseRequestDTO dto) {
        Integer userId = UserContext.getUserId();
        if (userId == null) {
            return Result.error("未登录");
        }

        log.info("购买请求: userId={}, vehicleId={}, currency={}",
                userId, dto.getVehicleId(), dto.getCurrency());

        Object result = purchaseService.purchase(userId, dto);
        return Result.success(result);
    }
}