package org.codeman.chain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author hdgaadd
 * created on 2022/04/18
 */
@Data
@AllArgsConstructor
public class User {

    /**
     * 是否是新用户
     */
    public Boolean isNewUser;
    /**
     * 是否完成分享任务
     */
    private Boolean isCompleteShareTask;
    /**
     * 是否超过最大领取限制
     */
    private Boolean isExceededLimit;
}
