package com.warthunder.vehicle.dto;

import lombok.Data;

@Data
public class PurchaseRequestDTO {
    /** 载具ID（vehicles表的id） */
    private Integer vehicleId;
    /** 支付货币：warpoints 或 eagles，免费载具可不传 */
    private String currency;
}