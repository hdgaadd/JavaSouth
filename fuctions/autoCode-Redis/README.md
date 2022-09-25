### autoCode-Redis

**how**

- for循环联合Random生成验证码，保存在Redis里

**bugs**

- ```
  java.lang.NullPointerException: null空指针异常
  ```

  sloved：private RedisService redisSerivice没有添加@Resource

  定位应该是对象，却测试变量，变量不可能空指针异常