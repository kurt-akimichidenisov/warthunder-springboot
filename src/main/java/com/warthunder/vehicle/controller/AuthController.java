package com.warthunder.vehicle.controller;

import com.warthunder.vehicle.common.Result;
import com.warthunder.vehicle.dto.*;
import com.warthunder.vehicle.service.UserService;
import com.warthunder.vehicle.util.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "用户认证", description = "注册、登录、用户信息接口")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public Result register(@Valid @RequestBody RegisterDTO registerDTO) {
        log.info("注册请求: username={}, email={}", registerDTO.getUsername(), registerDTO.getEmail());
        LoginResultVO result = userService.register(registerDTO);
        return Result.success(result);
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public Result login(@Valid @RequestBody LoginDTO loginDTO) {
        log.info("登录请求: email={}", loginDTO.getEmail());
        LoginResultVO result = userService.login(loginDTO);
        return Result.success(result);
    }

    @GetMapping("/info")
    @Operation(summary = "获取当前用户信息")
    public Result getUserInfo() {
        Integer userId = UserContext.getUserId();
        if (userId == null) {
            return Result.error("未登录");
        }
        log.info("获取用户信息: userId={}", userId);
        UserVO user = userService.getCurrentUser(userId);
        return Result.success(user);
    }

    @PutMapping("/info")
    @Operation(summary = "更新用户信息")
    public Result updateUserInfo(@Valid @RequestBody UserInfoUpdateDTO updateDTO) {
        Integer userId = UserContext.getUserId();
        if (userId == null) {
            return Result.error("未登录");
        }
        log.info("更新用户信息: userId={}, dto={}", userId, updateDTO);
        UserVO user = userService.updateCurrentUser(userId, updateDTO);
        return Result.success(user);
    }

    @PostMapping("/logout")
    @Operation(summary = "退出登录")
    public Result logout() {
        log.info("用户退出登录");
        return Result.success("退出成功");
    }

    @PostMapping("/balance/add")
    @Operation(summary = "充值余额")
    public Result addBalance(@Valid @RequestBody AddBalanceDTO dto) {
        Integer userId = UserContext.getUserId();
        if (userId == null) {
            return Result.error("未登录");
        }
        log.info("充值: userId={}, warpoints={}, eagles={}", userId, dto.getWarpoints(), dto.getEagles());
        UserVO user = userService.addBalance(userId, dto);
        return Result.success(user);
    }
}