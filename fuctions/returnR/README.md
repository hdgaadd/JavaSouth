## returnR

**why**

- 对每次url调用返回的结果做统一处理，返回的R对象可以**显示在页面上**，起到给前端页面提示的作用

> 页面接收到的为：{"code":1,"data":{"id":1,"name":"Jone","age":18,"email":"test1@baomidou.com"},"message":"success"}

**how**

**返回R对象**return R.ok(userService.getId());

R.ok方法把数据库查询到的数据、成败信息整型表示、成败信息字符串封装成R对象，返回给前端

```
public static <T> R<T> ok(T data) { // R的两个位置泛型，确保了返回的R对象的泛型也是该泛型
    return restResult(CommonConstant.SUCCESS, data, "success");
}
```

**成败信息类**的类型

- 成败信息类为接口类型，因为接口的变量类型默认为public static final，代表该变量不可变且可访问

      public interface CommonConstant { //[ˈkɒnstənt]常量
      
          Integer SUCCESS = 1;
      
          Integer FAIL = 0;
      }

- 成败信息类为Enum类型

  ```
  public enum ResultCode {
      SUCCESS(1, "successful"),
      FAIL(0, "fail");
  
      private final long code;
      private final String message;
  
      ResultCode (long code, String message) {
          this.code = code;
          this.message = message;
      }
  
      public long getCode() {
          return this.code;
      }
  
      public String getMessage() {
          return this.message;
      }
  }
  ```

