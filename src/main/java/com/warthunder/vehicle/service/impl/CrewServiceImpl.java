package com.warthunder.vehicle.service.impl;

import com.warthunder.vehicle.dto.AssignCrewDTO;
import com.warthunder.vehicle.dto.CreateCrewDTO;
import com.warthunder.vehicle.dto.CrewDetailVO;
import com.warthunder.vehicle.entity.*;
import com.warthunder.vehicle.repository.*;
import com.warthunder.vehicle.service.CrewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CrewServiceImpl implements CrewService {

    @Autowired
    private CrewRepository crewRepository;

    @Autowired
    private UserVehicleRepository userVehicleRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private VehicleCrewAssignmentRepository assignmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<CrewDetailVO> getUserCrewList(Integer userId) {
        log.info("查询用户所有乘员: userId={}", userId);

        // 1. 查该用户所有乘员
        List<Crew> crewList = crewRepository.findByUserId(userId);

        if (crewList.isEmpty()) {
            return List.of();
        }

        // 2. 批量查分配关系
        List<Integer> crewIds = crewList.stream().map(Crew::getId).toList();
        List<VehicleCrewAssignment> assignments = assignmentRepository.findByCrewIdIn(crewIds);

        // crewId → assignment
        Map<Integer, VehicleCrewAssignment> assignmentMap = assignments.stream()
                .collect(Collectors.toMap(
                        VehicleCrewAssignment::getCrewId,
                        a -> a,
                        (a, b) -> a
                ));

        // 3. 批量查user_vehicle和vehicle
        List<Integer> userVehicleIds = assignments.stream()
                .map(VehicleCrewAssignment::getUserVehicleId)
                .distinct()
                .toList();
        Map<Integer, UserVehicle> uvMap = userVehicleIds.isEmpty()
                ? Map.of()
                : userVehicleRepository.findAllById(userVehicleIds).stream()
                .collect(Collectors.toMap(UserVehicle::getId, uv -> uv));

        List<Integer> vehicleIds = uvMap.values().stream()
                .map(UserVehicle::getVehicleId)
                .distinct()
                .toList();
        Map<Integer, Vehicle> vehicleMap = vehicleIds.isEmpty()
                ? Map.of()
                : vehicleRepository.findAllById(vehicleIds).stream()
                .collect(Collectors.toMap(Vehicle::getId, v -> v));

        // 4. 组装VO
        return crewList.stream().map(crew -> {
            CrewDetailVO.AssignedVehicleInfo assignedInfo = null;
            VehicleCrewAssignment assignment = assignmentMap.get(crew.getId());
            if (assignment != null) {
                UserVehicle uv = uvMap.get(assignment.getUserVehicleId());
                if (uv != null) {
                    Vehicle vehicle = vehicleMap.get(uv.getVehicleId());
                    assignedInfo = CrewDetailVO.AssignedVehicleInfo.builder()
                            .userVehicleId(uv.getId())
                            .vehicleName(vehicle != null ? vehicle.getVehicleName() : "Unknown")
                            .build();
                }
            }

            return CrewDetailVO.builder()
                    .id(crew.getId())
                    .name(crew.getName())
                    .crewRole(crew.getCrewRole())
                    .assignedVehicle(assignedInfo)
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public CrewDetailVO createCrew(Integer userId, CreateCrewDTO dto) {
        log.info("创建乘员: userId={}, name={}, crewRole={}", userId, dto.getName(), dto.getCrewRole());

        // 验证用户存在
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        Crew crew = new Crew();
        crew.setUserId(userId);
        crew.setName(dto.getName());
        crew.setCrewRole(dto.getCrewRole());

        crewRepository.save(crew);
        log.info("乘员创建成功: crewId={}", crew.getId());

        return CrewDetailVO.builder()
                .id(crew.getId())
                .name(crew.getName())
                .crewRole(crew.getCrewRole())
                .assignedVehicle(null)
                .build();
    }

    @Override
    @Transactional
    public void assignCrew(Integer userId, AssignCrewDTO dto) {
        log.info("分配乘员: userId={}, crewId={}, userVehicleId={}",
                userId, dto.getCrewId(), dto.getUserVehicleId());

        // 1. 验证乘员属于当前用户
        Crew crew = crewRepository.findById(dto.getCrewId())
                .orElseThrow(() -> new RuntimeException("乘员不存在"));
        if (!crew.getUserId().equals(userId)) {
            throw new RuntimeException("该乘员不属于你");
        }

        // 2. 验证载具属于当前用户
        UserVehicle userVehicle = userVehicleRepository.findById(dto.getUserVehicleId())
                .orElseThrow(() -> new RuntimeException("载具不存在"));
        if (!userVehicle.getUserId().equals(userId)) {
            throw new RuntimeException("该载具不属于你");
        }

        // 3. 如果乘员已有分配，先删除旧的
        Optional<VehicleCrewAssignment> existing = assignmentRepository.findByCrewId(dto.getCrewId());
        if (existing.isPresent()) {
            log.info("乘员已有分配，先解除旧分配: assignmentId={}", existing.get().getId());
            assignmentRepository.delete(existing.get());
        }

        // 4. 创建新分配
        VehicleCrewAssignment assignment = new VehicleCrewAssignment();
        assignment.setUserVehicleId(dto.getUserVehicleId());
        assignment.setCrewId(dto.getCrewId());
        assignmentRepository.save(assignment);

        log.info("乘员分配成功");
    }

    @Override
    public void unassignCrew(Integer userId, Integer crewId) {
        log.info("解除乘员分配: userId={}, crewId={}", userId, crewId);

        // 验证乘员属于当前用户
        Crew crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new RuntimeException("乘员不存在"));
        if (!crew.getUserId().equals(userId)) {
            throw new RuntimeException("该乘员不属于你");
        }

        // 删除分配记录
        Optional<VehicleCrewAssignment> assignment = assignmentRepository.findByCrewId(crewId);
        if (assignment.isPresent()) {
            assignmentRepository.delete(assignment.get());
            log.info("解除分配成功");
        } else {
            log.info("该乘员未分配，无需解除");
        }
    }
}