import api.ResultCode;
import exception.Asserts;

/**
 * @author hdgaadd
 * Created on 2021/12/08/19:49
 */
public class Test {
    public static void main(String[] args) {
        Asserts.fail("失败");
        Asserts.fail(ResultCode.SUCCESS);
        Asserts.fail(ResultCode.FAILED);
    }
}
