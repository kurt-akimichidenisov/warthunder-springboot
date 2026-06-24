package com.warthunder.vehicle.service.impl;

import com.warthunder.vehicle.common.PageResult;
import com.warthunder.vehicle.dto.UserCrewVO;
import com.warthunder.vehicle.dto.UserVehicleVO;
import com.warthunder.vehicle.entity.*;
import com.warthunder.vehicle.repository.*;
import com.warthunder.vehicle.service.DashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private UserVehicleRepository userVehicleRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private CrewRepository crewRepository;

    @Autowired
    private VehicleCrewAssignmentRepository assignmentRepository;

    @Override
    public PageResult<UserVehicleVO> getUserVehicles(Integer userId, Integer page, Integer pageSize,
                                                     String sortBy, String sortOrder) {

        Sort sort = buildVehicleSort(sortBy, sortOrder);
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, sort);

        Page<UserVehicle> userVehiclePage = userVehicleRepository.findByUserId(userId, pageRequest);

        // 获取当前页所有用户载具的ID
        List<Integer> userVehicleIds = userVehiclePage.getContent().stream()
                .map(UserVehicle::getId)
                .collect(Collectors.toList());

        // 批量查分配关系
        List<VehicleCrewAssignment> allAssignments = userVehicleIds.isEmpty()
                ? List.of()
                : assignmentRepository.findByUserVehicleIdIn(userVehicleIds);

        // 按userVehicleId分组
        Map<Integer, List<VehicleCrewAssignment>> assignmentMap = allAssignments.stream()
                .collect(Collectors.groupingBy(VehicleCrewAssignment::getUserVehicleId));

        // 批量查乘员信息
        List<Integer> crewIds = allAssignments.stream()
                .map(VehicleCrewAssignment::getCrewId)
                .distinct()
                .collect(Collectors.toList());
        Map<Integer, Crew> crewMap = crewIds.isEmpty()
                ? Map.of()
                : crewRepository.findAllById(crewIds).stream()
                .collect(Collectors.toMap(Crew::getId, c -> c));

        // 批量查载具信息
        List<Integer> vehicleIds = userVehiclePage.getContent().stream()
                .map(UserVehicle::getVehicleId)
                .collect(Collectors.toList());
        Map<Integer, Vehicle> vehicleMap = vehicleIds.isEmpty()
                ? Map.of()
                : vehicleRepository.findAllById(vehicleIds).stream()
                .collect(Collectors.toMap(Vehicle::getId, v -> v));

        // 组装VO
        List<UserVehicleVO> rows = userVehiclePage.getContent().stream().map(uv -> {
            Vehicle vehicle = vehicleMap.get(uv.getVehicleId());

            List<VehicleCrewAssignment> assignments = assignmentMap.getOrDefault(uv.getId(), List.of());
            List<UserVehicleVO.AssignedCrewVO> assignedCrew = assignments.stream().map(a -> {
                Crew crew = crewMap.get(a.getCrewId());
                return UserVehicleVO.AssignedCrewVO.builder()
                        .id(crew != null ? crew.getId() : a.getCrewId())
                        .name(crew != null ? crew.getName() : "Unknown")
                        .build();
            }).collect(Collectors.toList());

            return UserVehicleVO.builder()
                    .id(uv.getId())
                    .unitId(vehicle != null ? vehicle.getUnitId() : null)
                    .namePrefix(vehicle != null ? vehicle.getNamePrefix() : null)
                    .vehicleName(vehicle != null ? vehicle.getVehicleName() : "Unknown")
                    .mainRole(vehicle != null ? vehicle.getMainRole() : null)
                    .unitCountry(vehicle != null ? vehicle.getUnitCountry() : null)
                    .vehicleRank(vehicle != null ? vehicle.getVehicleRank() : null)
                    .br(vehicle != null ? vehicle.getBr() : null)
                    .assignedCrew(assignedCrew)
                    .build();
        }).collect(Collectors.toList());

        return new PageResult<>(userVehiclePage.getTotalElements(), rows);
    }

    // ====== getUserCrew 简化版 ======
    @Override
    public PageResult<UserCrewVO> getUserCrew(Integer userId, Integer page, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<Crew> crewPage = crewRepository.findByUserId(userId, pageRequest);

        List<UserCrewVO> rows = crewPage.getContent().stream().map(crew -> {
            List<UserCrewVO.AssignedVehicleVO> assignedVehicles = new ArrayList<>();

            // 查该乘员是否分配了载具
            assignmentRepository.findByCrewId(crew.getId()).ifPresent(assignment -> {
                // 查user_vehicle
                userVehicleRepository.findById(assignment.getUserVehicleId()).ifPresent(uv -> {
                    // 查vehicle
                    vehicleRepository.findById(uv.getVehicleId()).ifPresent(vehicle -> {
                        assignedVehicles.add(UserCrewVO.AssignedVehicleVO.builder()
                                .id(uv.getId())
                                .vehicleName(vehicle.getVehicleName())
                                .build());
                    });
                });
            });

            return UserCrewVO.builder()
                    .id(crew.getId())
                    .name(crew.getName())
                    .crewRole(crew.getCrewRole())
                    .assignedVehicles(assignedVehicles)
                    .build();
        }).collect(Collectors.toList());

        return new PageResult<>(crewPage.getTotalElements(), rows);
    }
    private Sort buildVehicleSort(String sortBy, String sortOrder) {
        // 用户载具按purchaseDate排序（默认），或按id
        String column = "id";
        if ("purchaseDate".equals(sortBy)) {
            column = "purchaseDate";
        }
        Sort.Direction direction = "desc".equalsIgnoreCase(sortOrder)
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        return Sort.by(direction, column);
    }
}