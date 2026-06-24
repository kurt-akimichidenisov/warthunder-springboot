package com.warthunder.vehicle.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddBalanceDTO {
    private BigDecimal warpoints;
    private BigDecimal eagles;
}