package com.warthunder.vehicle.repository;

import com.warthunder.vehicle.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /** 根据邮箱查询用户（登录用） */
    Optional<User> findByEmail(String email);

    /** 根据用户名查询（注册时查重） */
    Optional<User> findByUsername(String username);

    /** 检查邮箱是否已存在 */
    boolean existsByEmail(String email);

    /** 检查用户名是否已存在 */
    boolean existsByUsername(String username);
}