package com.warthunder.vehicle.service;

import com.warthunder.vehicle.dto.*;

public interface UserService {

    /** 用户注册 */
    LoginResultVO register(RegisterDTO registerDTO);

    /** 用户登录 */
    LoginResultVO login(LoginDTO loginDTO);

    /** 获取当前用户信息 */
    UserVO getCurrentUser(Integer userId);

    /** 更新当前用户信息 */
    UserVO updateCurrentUser(Integer userId, UserInfoUpdateDTO updateDTO);
    /**
     * 增加用户余额
     */
    UserVO addBalance(Integer userId, AddBalanceDTO dto);
}