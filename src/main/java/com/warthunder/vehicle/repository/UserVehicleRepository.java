package com.warthunder.vehicle.repository;

import com.warthunder.vehicle.entity.UserVehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserVehicleRepository extends JpaRepository<UserVehicle, Integer> {

    Page<UserVehicle> findByUserId(Integer userId, Pageable pageable);

    List<UserVehicle> findByUserId(Integer userId);

    boolean existsByUserIdAndVehicleId(Integer userId, Integer vehicleId);

    Optional<UserVehicle> findByUserIdAndVehicleId(Integer userId, Integer vehicleId);

    /** 按用户统计载具数量，降序 */
    @Query("SELECT uv.userId, COUNT(uv) " +
            "FROM UserVehicle uv " +
            "GROUP BY uv.userId " +
            "ORDER BY COUNT(uv) DESC")
    List<Object[]> countVehiclesByUser();
}