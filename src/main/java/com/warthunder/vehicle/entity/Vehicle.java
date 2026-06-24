package com.warthunder.vehicle.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "vehicles")
public class Vehicle implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 业务标识，如 cn_m8_greyhound */
    @Column(name = "unit_id", length = 50)
    private String unitId;

    /** 名称前的特殊标记，如 ␗、▄、◔ */
    @Column(name = "name_prefix", length = 10)
    private String namePrefix;

    /** 纯净名称 */
    @Column(name = "vehicle_name", nullable = false, length = 100)
    private String vehicleName;

    /** 原始完整名称 */
    @Column(name = "original_name", length = 150)
    private String originalName;

    /** 主角色：Light tank, Medium tank, Heavy tank, SPAA, Tank destroyer */
    @Column(name = "main_role", length = 30)
    private String mainRole;

    /** 国家代码 */
    @Column(name = "unit_country", length = 30)
    private String unitCountry;

    /** 原始等级：I, II, III, IV, V, VI, VII, VIII */
    @Column(name = "vehicle_rank", length = 5)
    private String vehicleRank;

    /** 等级数字：1-8 */
    @Column(name = "rank_number")
    private Byte rankNumber;

    /** 战斗等级 */
    @Column(precision = 3, scale = 1)
    private BigDecimal br;

    /** 购买数量 */
    @Column(name = "purchase_amount")
    private Integer purchaseAmount;

    /** 货币类型：warpoints, eagles, Free */
    @Column(name = "purchase_currency", length = 20)
    private String purchaseCurrency;
}