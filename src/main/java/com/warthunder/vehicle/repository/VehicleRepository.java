package com.warthunder.vehicle.repository;

import com.warthunder.vehicle.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository
        extends JpaRepository<Vehicle, Integer>,
        JpaSpecificationExecutor<Vehicle> {

    /** 按角色统计所有被购买的载具数量 */
    @Query("SELECT uv.vehicle.mainRole, COUNT(uv) " +
            "FROM UserVehicle uv " +
            "GROUP BY uv.vehicle.mainRole " +
            "ORDER BY COUNT(uv) DESC")
    List<Object[]> countByMainRole();
}