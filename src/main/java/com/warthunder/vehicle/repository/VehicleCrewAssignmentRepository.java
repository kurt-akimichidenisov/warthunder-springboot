package com.warthunder.vehicle.repository;

import com.warthunder.vehicle.entity.VehicleCrewAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleCrewAssignmentRepository extends JpaRepository<VehicleCrewAssignment, Integer> {

    /** 根据用户载具ID查分配的所有乘员 */
    List<VehicleCrewAssignment> findByUserVehicleId(Integer userVehicleId);

    /** 根据多个用户载具ID查分配 */
    List<VehicleCrewAssignment> findByUserVehicleIdIn(List<Integer> userVehicleIds);

    /** 根据乘员ID查分配记录 */
    Optional<VehicleCrewAssignment> findByCrewId(Integer crewId);

    /** ✅ 新增：根据多个乘员ID查分配 */
    List<VehicleCrewAssignment> findByCrewIdIn(List<Integer> crewIds);

    /** ✅ 新增：删除某个乘员的分配记录 */
    void deleteByCrewId(Integer crewId);
}