package com.warthunder.vehicle.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 返回给前端的用户信息（脱敏，不含密码）
 */
@Data
@Builder
public class UserVO {
    private Integer id;
    private String username;
    private String email;
    private Integer gender;
    private String avatar;
    private BigDecimal warpoints;
    private BigDecimal eagles;
    private LocalDateTime createdAt;
}