package org.codeman;

import org.codeman.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author hdgaadd
 * Created on 2022/02/08
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class User extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String userName;
}
