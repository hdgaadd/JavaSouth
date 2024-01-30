package org.codeman.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author hdgaadd
 * created on 2021/12/09
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SeckillOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String code;

    private Long commodityId;

    private Long userId;

    private Long seckillActivityId;

    private BigDecimal amount;

    private Integer orderStatus;

    private LocalDateTime createTime;

    private LocalDateTime payTime;

}
