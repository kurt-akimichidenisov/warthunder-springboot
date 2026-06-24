package com.warthunder.vehicle.repository;

import com.warthunder.vehicle.entity.Crew;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CrewRepository extends JpaRepository<Crew, Integer> {

    Page<Crew> findByUserId(Integer userId, Pageable pageable);

    List<Crew> findByUserId(Integer userId);

    /** 按角色统计所有乘员数量 */
    @Query("SELECT c.crewRole, COUNT(c) FROM Crew c GROUP BY c.crewRole")
    List<Object[]> countByCrewRole();
}