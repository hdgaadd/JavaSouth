package org.codeman;

import org.codeman.api.ResultCode;
import org.codeman.exception.Asserts;

/**
 * @author hdgaadd
 * Created on 2021/12/08/19:49
 */
public class Client {
    public static void main(String[] args) {
        Asserts.fail("失败");
        Asserts.fail(ResultCode.SUCCESS);
        Asserts.fail(ResultCode.FAILED);
    }
}
