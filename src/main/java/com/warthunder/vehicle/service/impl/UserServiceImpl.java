package com.warthunder.vehicle.service.impl;

import com.warthunder.vehicle.dto.*;
import com.warthunder.vehicle.entity.User;
import com.warthunder.vehicle.repository.UserRepository;
import com.warthunder.vehicle.service.UserService;
import com.warthunder.vehicle.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public LoginResultVO register(RegisterDTO dto) {
        log.info("注册用户: username={}, email={}", dto.getUsername(), dto.getEmail());

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("该邮箱已被注册");
        }

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("该用户名已被使用");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setGender(dto.getGender());
        user.setWarpoints(new BigDecimal("9999999"));
        user.setEagles(new BigDecimal("9999999"));

        userRepository.save(user);

        log.info("用户注册成功: userId={}", user.getId());

        String token = jwtUtil.generateToken(user.getId(), user.getEmail());
        UserVO userVO = toUserVO(user);
        return new LoginResultVO(token, userVO);
    }

    @Override
    public LoginResultVO login(LoginDTO dto) {
        log.info("用户登录: email={}", dto.getEmail());

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("邮箱或密码错误"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("邮箱或密码错误");
        }

        log.info("用户登录成功: userId={}", user.getId());

        String token = jwtUtil.generateToken(user.getId(), user.getEmail());
        UserVO userVO = toUserVO(user);
        return new LoginResultVO(token, userVO);
    }

    @Override
    public UserVO getCurrentUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        return toUserVO(user);
    }

    @Override
    public UserVO updateCurrentUser(Integer userId, UserInfoUpdateDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        if (dto.getUsername() != null && !dto.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsername(dto.getUsername())) {
                throw new RuntimeException("该用户名已被使用");
            }
            user.setUsername(dto.getUsername());
        }

        if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new RuntimeException("该邮箱已被注册");
            }
            user.setEmail(dto.getEmail());
        }

        if (dto.getGender() != null) {
            user.setGender(dto.getGender());
        }

        if (dto.getAvatar() != null) {
            user.setAvatar(dto.getAvatar());
        }

        userRepository.save(user);
        log.info("用户信息更新成功: userId={}", userId);

        return toUserVO(user);
    }

    private UserVO toUserVO(User user) {
        return UserVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .gender(user.getGender())
                .avatar(user.getAvatar())
                .warpoints(user.getWarpoints())
                .eagles(user.getEagles())
                .createdAt(user.getCreatedAt())
                .build();
    }
    @Override
    public UserVO addBalance(Integer userId, AddBalanceDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        BigDecimal wp = dto.getWarpoints() != null ? dto.getWarpoints() : BigDecimal.ZERO;
        BigDecimal ge = dto.getEagles() != null ? dto.getEagles() : BigDecimal.ZERO;

        if (wp.compareTo(BigDecimal.ZERO) < 0 || ge.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("充值金额不能为负数");
        }
        if (wp.compareTo(BigDecimal.ZERO) == 0 && ge.compareTo(BigDecimal.ZERO) == 0) {
            throw new RuntimeException("请至少选择一种货币充值");
        }

        user.setWarpoints(user.getWarpoints().add(wp));
        user.setEagles(user.getEagles().add(ge));
        userRepository.save(user);

        log.info("充值成功: userId={}, +{}WP, +{}GE, 余额: {}WP, {}GE",
                userId, wp, ge, user.getWarpoints(), user.getEagles());

        return toUserVO(user);
    }
}