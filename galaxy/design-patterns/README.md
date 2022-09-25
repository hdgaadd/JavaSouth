# 📚structure

✅  策略模式

✅  观察者模式

✅  工厂模式

✅  单例模式

✅  迭代器与组合模式

✅  拦截过滤器模式

✅  责任链模式

✅  命令模式

✅  适配器模式





# 📖Details

## adapter

**process**

- 某些物件不能直接操作，但能通过创建一个适配器的方式，使其达到操作的目的

**knowledge**

- lombok的var可以修饰方法中的局部变量，使其不必设置具体类型，lombok会根据运行时**推断出类型**





## command

**process**

- **命令队列**存储命令，执行命令则遍历队列所有任务
- 可撤销某条命令

**knowledge**

- 当某基类要求某成员变量必须通过**构造方法赋值**时，则该基类类型必须选择**abstract**，而不是interface







## chain-of-responsibility-patterns

**process**

- 责任链模式就像踢皮球，遇到无法处理的困难，**抛给上级**去处理





## interceptor

**process**

- 在原本方法**执行前运行**拦截器方法
- 过滤管理器暴露调用接口，实现核心对象拦截链**不暴露**
- 拦截链包含**核心代码**，内含拦截的执行逻辑、所拦截对象
- 过滤管理器外面可包裹一层客户端**Client**，给用户进行调用，而过滤管理器给内部人员调用

**knowledge**

- why要**隐藏**核心逻辑

  避免核心逻辑被**轻易调用**而造成资源破坏



## factory



## iterator



## observer



## singleton



## strategy





