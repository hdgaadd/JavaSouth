package com.codeman.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author hdgaadd
 * @since 2021-12-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SeckillActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private Long commodityId;

    private BigDecimal oldPrice;

    private BigDecimal seckillPrice;

    private Integer activityStatus;

    private Integer totalStock;

    private Integer availableStock;

    private Integer lockStock;

    private LocalDateTime startTime;

    private LocalDateTime endTime;


}
