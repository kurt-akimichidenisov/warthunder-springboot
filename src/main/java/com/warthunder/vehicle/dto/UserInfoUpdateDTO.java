package com.warthunder.vehicle.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserInfoUpdateDTO {

    @Size(min = 3, max = 50, message = "用户名长度3-50位")
    private String username;

    @Email(message = "邮箱格式不正确")
    private String email;

    private Integer gender;

    private String avatar;
}