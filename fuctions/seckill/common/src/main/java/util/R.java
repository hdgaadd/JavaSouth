package util;

import constant.CommonConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@NoArgsConstructor
@AllArgsConstructor // 添加有参构造方法，参数为所有参数
@Data
public class R<T> implements Serializable {

    private int code;

    private T data;

    private String message;


    public static <T> R<T> ok(T data) {
        return restResult(CommonConstant.SUCCESS, data, "success");
    }

    public static <T> R<T> fail(T data) {
        return restResult(CommonConstant.FAIL, data, "fail");
    }

    private static <T> R<T> restResult(Integer code, T data, String msg) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setData(data);
        r.setMessage(msg);
        return r;
    }

    public static void main(String[] args) {
        R<Integer> r = new R<Integer>();
        r.setCode(6);
        r.setMessage("66");
        r.setData(new Integer(666));
        System.out.println(r);
    }
}
