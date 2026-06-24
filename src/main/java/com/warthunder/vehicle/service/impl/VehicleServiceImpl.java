package com.warthunder.vehicle.service.impl;

import com.warthunder.vehicle.common.PageResult;
import com.warthunder.vehicle.dto.VehicleQueryDTO;
import com.warthunder.vehicle.entity.Vehicle;
import com.warthunder.vehicle.repository.VehicleRepository;
import com.warthunder.vehicle.service.VehicleService;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Override
    public PageResult<Vehicle> page(VehicleQueryDTO query) {
        log.info("分页查询载具: {}", query);

        // 1. 构建排序
        Sort sort = buildSort(query.getSortBy(), query.getSortOrder());

        // 2. 构建分页参数（Spring Data JPA页码从0开始）
        int pageIndex = query.getPage() - 1;
        PageRequest pageRequest = PageRequest.of(pageIndex, query.getPageSize(), sort);

        // 3. 构建动态查询条件
        Specification<Vehicle> spec = buildSpecification(query);

        // 4. 执行分页查询
        Page<Vehicle> pageResult = vehicleRepository.findAll(spec, pageRequest);

        // 5. 封装返回
        return new PageResult<>(pageResult.getTotalElements(), pageResult.getContent());
    }

    /**
     * 构建排序
     */
    private Sort buildSort(String sortBy, String sortOrder) {
        // 映射前端字段到数据库列名
        String column = switch (sortBy) {
            case "name" -> "vehicleName";
            case "role" -> "mainRole";
            case "country" -> "unitCountry";
            case "rank" -> "vehicleRank";
            case "br" -> "br";
            case "cost" -> "purchaseAmount";
            default -> "id";
        };

        Sort.Direction direction = "desc".equalsIgnoreCase(sortOrder)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        return Sort.by(direction, column);
    }

    /**
     * 构建动态查询条件（替代之前的MyBatis XML动态SQL）
     */
    private Specification<Vehicle> buildSpecification(VehicleQueryDTO query) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 名称模糊搜索
            if (query.getName() != null && !query.getName().isBlank()) {
                predicates.add(
                        criteriaBuilder.like(root.get("vehicleName"),
                                "%" + query.getName() + "%")
                );
            }

            // 国家筛选（支持逗号分隔多选）
            if (query.getCountries() != null && !query.getCountries().isBlank()) {
                List<String> countryList = Arrays.asList(query.getCountries().split(","));
                predicates.add(root.get("unitCountry").in(countryList));
            }

            // 角色筛选（支持逗号分隔多选）
            if (query.getRoles() != null && !query.getRoles().isBlank()) {
                List<String> roleList = Arrays.asList(query.getRoles().split(","));
                predicates.add(root.get("mainRole").in(roleList));
            }

            // 等级筛选（支持逗号分隔多选）
            if (query.getRanks() != null && !query.getRanks().isBlank()) {
                List<String> rankList = Arrays.asList(query.getRanks().split(","));
                predicates.add(root.get("vehicleRank").in(rankList));
            }

            // BR范围筛选
            if (query.getBrMin() != null && query.getBrMax() != null) {
                predicates.add(
                        criteriaBuilder.between(root.get("br"),
                                query.getBrMin(), query.getBrMax())
                );
            } else if (query.getBrMin() != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(root.get("br"), query.getBrMin())
                );
            } else if (query.getBrMax() != null) {
                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(root.get("br"), query.getBrMax())
                );
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}