package com.warthunder.vehicle.dto;

import lombok.Data;

@Data
public class VehicleQueryDTO {
    /** 页码，默认1 */

    private Integer page = 1;

    /** 每页大小，默认20 */
    private Integer pageSize = 20;

    /** 名称模糊搜索 */
    private String name;

    /** 国家，逗号分隔，如 "china,usa,ussr" */
    private String countries;

    /** 角色，逗号分隔，如 "Light tank,Medium tank" */
    private String roles;

    /** 等级，逗号分隔，如 "I,II,III" */
    private String ranks;

    /** BR最小值 */
    private Double brMin;

    /** BR最大值 */
    private Double brMax;

    /** 排序字段：name/role/country/rank/br/cost/id */
    private String sortBy = "id";

    /** 排序方向：asc/desc */
    private String sortOrder = "asc";
}