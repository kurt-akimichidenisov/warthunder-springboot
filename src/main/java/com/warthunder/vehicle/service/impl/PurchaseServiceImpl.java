package com.warthunder.vehicle.service.impl;

import com.warthunder.vehicle.dto.PurchaseRequestDTO;
import com.warthunder.vehicle.entity.User;
import com.warthunder.vehicle.entity.UserVehicle;
import com.warthunder.vehicle.entity.Vehicle;
import com.warthunder.vehicle.repository.UserRepository;
import com.warthunder.vehicle.repository.UserVehicleRepository;
import com.warthunder.vehicle.repository.VehicleRepository;
import com.warthunder.vehicle.service.PurchaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
public class PurchaseServiceImpl implements PurchaseService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserVehicleRepository userVehicleRepository;

    @Override
    @Transactional
    public Object purchase(Integer userId, PurchaseRequestDTO dto) {
        log.info("购买载具: userId={}, vehicleId={}, currency={}",
                userId, dto.getVehicleId(), dto.getCurrency());

        // 1. 查用户
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 2. 查载具
        Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId())
                .orElseThrow(() -> new RuntimeException("载具不存在"));

        // 3. 查重：不能重复购买
        if (userVehicleRepository.existsByUserIdAndVehicleId(userId, dto.getVehicleId())) {
            throw new RuntimeException("你已经拥有该载具，不能重复购买");
        }

        // 4. 判断是否免费
        boolean isFree = vehicle.getPurchaseAmount() == null
                || vehicle.getPurchaseCurrency() == null
                || "Free".equalsIgnoreCase(vehicle.getPurchaseCurrency());

        if (!isFree) {
            // 5. 扣款
            String currency = dto.getCurrency();
            BigDecimal price = BigDecimal.valueOf(vehicle.getPurchaseAmount());

            if ("warpoints".equals(currency)) {
                if (user.getWarpoints().compareTo(price) < 0) {
                    throw new RuntimeException("War Points 余额不足");
                }
                user.setWarpoints(user.getWarpoints().subtract(price));
                log.info("扣除 War Points: {}, 剩余: {}", price, user.getWarpoints());

            } else if ("eagles".equals(currency)) {
                if (user.getEagles().compareTo(price) < 0) {
                    throw new RuntimeException("Golden Eagles 余额不足");
                }
                user.setEagles(user.getEagles().subtract(price));
                log.info("扣除 Golden Eagles: {}, 剩余: {}", price, user.getEagles());

            } else {
                throw new RuntimeException("无效的货币类型: " + currency);
            }

            userRepository.save(user);
        } else {
            log.info("免费载具，直接领取");
        }

        // 6. 写入 user_vehicles
        UserVehicle userVehicle = new UserVehicle();
        userVehicle.setUserId(userId);
        userVehicle.setVehicleId(dto.getVehicleId());
        userVehicleRepository.save(userVehicle);

        log.info("购买成功: userVehicleId={}", userVehicle.getId());

        // 7. 返回结果
        return userVehicle;
    }
}