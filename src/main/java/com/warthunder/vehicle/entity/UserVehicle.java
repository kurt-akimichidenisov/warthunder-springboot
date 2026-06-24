
package com.warthunder.vehicle.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_vehicles")
public class UserVehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "vehicle_id", nullable = false)
    private Integer vehicleId;

    /** ✅ 新增：关联Vehicle实体 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id",insertable = false, updatable = false)
    private Vehicle vehicle;

    @Column(name = "purchase_date")
    private LocalDateTime purchaseDate;

    @PrePersist
    protected void onCreate() {
        purchaseDate = LocalDateTime.now();
    }
}