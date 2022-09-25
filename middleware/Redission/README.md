### Redisson

**process**

- server-two没有休眠则输出、server-one休眠16s才输出

  而server-one率先获得锁，故server-two要等16s，在server-one输出后才输出
