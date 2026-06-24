package com.warthunder.vehicle.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 登录/注册成功后返回的数据
 */
@Data
@AllArgsConstructor
public class LoginResultVO {
    private String token;
    private UserVO user;
}