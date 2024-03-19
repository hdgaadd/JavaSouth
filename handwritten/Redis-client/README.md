### Redis-client

**process**

- 把方法与入参，**转换**为Redis可识别的命令
- 通过**Socket**向Redis发送后执行命令
- 通过Socket接收Redis消息

**knowledge**

- **INCR操作**使键的值+1

- **…**为可接收多个该类型对象

  ```java
  public static String commandTransform(Command command, byte[]... bytes) {
  	...
  }
  ```

