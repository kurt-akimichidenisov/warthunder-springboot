package com.warthunder.vehicle.service.impl;

import com.warthunder.vehicle.dto.*;
import com.warthunder.vehicle.entity.User;
import com.warthunder.vehicle.repository.CrewRepository;
import com.warthunder.vehicle.repository.UserRepository;
import com.warthunder.vehicle.repository.UserVehicleRepository;
import com.warthunder.vehicle.repository.VehicleRepository;
import com.warthunder.vehicle.service.PlayerStatsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PlayerStatsServiceImpl implements PlayerStatsService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private CrewRepository crewRepository;

    @Autowired
    private UserVehicleRepository userVehicleRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Map<Integer, String> CREW_ROLE_MAP = Map.of(
            0, "Commander",
            1, "Gunner",
            2, "Driver",
            3, "Loader",
            4, "Radio Operator"
    );

    @Override
    public PlayerStatsVO getPlayerStats(Integer page, Integer pageSize) {
        log.info("查询玩家统计数据: page={}, pageSize={}", page, pageSize);

        // ========== 1. 载具统计 ==========
        List<Object[]> vehicleStatsRaw = vehicleRepository.countByMainRole();
        List<VehicleStatsItem> vehicleStats = vehicleStatsRaw.stream()
                .map(row -> new VehicleStatsItem(
                        row[0] != null ? row[0].toString() : "Unknown",
                        (Long) row[1]
                ))
                .collect(Collectors.toList());

        // ========== 2. 乘员统计 ==========
        List<Object[]> crewStatsRaw = crewRepository.countByCrewRole();
        List<CrewStatsItem> crewStats = crewStatsRaw.stream()
                .map(row -> {
                    Integer roleCode = (Integer) row[0];
                    String roleName = CREW_ROLE_MAP.getOrDefault(roleCode, "Unknown");
                    return new CrewStatsItem(roleName, (Long) row[1]);
                })
                .collect(Collectors.toList());

        // ========== 3. 玩家排行榜 ==========
        List<Object[]> userCounts = userVehicleRepository.countVehiclesByUser();

        // 按载具数量降序，取全部，再做分页
        List<Object[]> sortedCounts = userCounts.stream()
                .sorted((a, b) -> Long.compare((Long) b[1], (Long) a[1]))
                .collect(Collectors.toList());

        long total = sortedCounts.size();

        // 手动分页
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, sortedCounts.size());
        List<Object[]> pagedCounts = start < sortedCounts.size()
                ? sortedCounts.subList(start, end)
                : List.of();

        // 批量查用户信息
        List<Integer> userIds = pagedCounts.stream()
                .map(row -> (Integer) row[0])
                .collect(Collectors.toList());
        Map<Integer, User> userMap = userIds.isEmpty()
                ? Map.of()
                : userRepository.findAllById(userIds).stream()
                .collect(Collectors.toMap(User::getId, u -> u));

        // 批量查乘员数量
        Map<Integer, Long> crewCountMap = new HashMap<>();
        if (!userIds.isEmpty()) {
            for (Integer uid : userIds) {
                long count = crewRepository.findByUserId(uid).size();
                crewCountMap.put(uid, count);
            }
        }

        List<PlayerRankVO> rows = pagedCounts.stream().map(row -> {
            Integer userId = (Integer) row[0];
            Long vehicleCount = (Long) row[1];
            User user = userMap.get(userId);

            return PlayerRankVO.builder()
                    .id(userId)
                    .username(user != null ? user.getUsername() : "Unknown")
                    .avatar(user != null ? user.getAvatar() : null)
                    .gender(user != null ? user.getGender() : 0)
                    .vehicleCount(vehicleCount)
                    .crewCount(crewCountMap.getOrDefault(userId, 0L))
                    .build();
        }).collect(Collectors.toList());

        return PlayerStatsVO.builder()
                .vehicleStats(vehicleStats)
                .crewStats(crewStats)
                .rows(rows)
                .total(total)
                .build();
    }
}