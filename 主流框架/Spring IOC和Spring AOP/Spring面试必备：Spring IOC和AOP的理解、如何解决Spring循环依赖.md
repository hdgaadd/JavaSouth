## 1. Spring IOC和AOP

### 1.1 Spring IOC

Spring IOC其实是一种通过**描述**来创建和获取对象的技术，相比于最原始的通过new来创建对象，所有的对象都交由Spring IOC进行管理，我们管这些对象称为Spring Bean。

Spring Bean可以看成是班级里的学生，那IOC容器就是容纳学生的班级。每个Bean的分类、不同的生命周期，包括Prototype、Singleton、Request、Session、Global session都可以在IOC容器里进行管理。这其实是一种控制反转的思想，我们程序员把控制对象的权限都交由了靠谱的Spring IOC容器。

通过XML方式我们可以向Spring IOC描述我需要一个A对象。当Spring启动时这个Bean也就自动注入到IOC容器等待我们的使用。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.springframework.org/schema/beans https://www.springframework.org/schema/beans/spring-beans.xsd">

  <bean id="bean1" class="org.springframework.beans.factory.ConcurrentBeanFactoryBenchmark$ConcurrentBean"
			scope="prototype">
    <property name="date" value="2004/08/08"/>
  </bean>

</beans>
```

现在商业公司通过以上XML的方式已经是很少见了，Spring Boot提供了另一种通过注解来**描述**Bean的方式。Spring Boot底层基于注解的IOC容器是`AnnotationConfigApplicationContext`，这个留到我后续的文章再来讲解。

```java
// 通过注解的方式来创建Bean
@Configuration
public class TokenConfig {
    /**
     * 设置token的类型
     **/
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter()); // 设置token类型为JWT
    }
}
```

### 1.2 Spring AOP

在整个软件编程的历史长河中，最先面世的一种编程范式叫做**面向过程编程**。但随着软件系统越来越复杂，面向过程编程越来越难以管理软件的复杂性。于是**面向对象编程OOP**诞生，它致力于解决困扰前者的软件复杂性问题。

但面向对象编程并不是银弹，它存在一些弊端，例如我们需要为整个项目的所有对象都引入一个公共行为：打印对象每个方法的执行耗时。如果采用OOP需要把公共行为的代码都**重复**贴到每个对象的类里。。。

现在救世主来了，AOP面向切面编程弥补了OOP的缺陷。

通过Spring  AOP，我们可以为每个对象约定一套流程，为对象方法执行前执行后织入一些行为。如果是日志的话，在对象方法执行后触发就可以了。

Spring  AOP提供了多个注解，我们来看看。

1. @Before。前置通知，在方法执行之前执行。
2. @After。后置通知，在方法执行之后执行。
3. @AfterReturning。返回通知，方法不抛出异常，**正常退出**方法时执行。
4. @AfterThrowing。异常通知，方法抛出异常后执行 。
5. @Around。环绕通知，围绕着方法执行，可以自定义在方向执行前、执行后执行一段逻辑，**自由度更高**。
6. @Pointcut。切点。定义了要拦截的对象。

```java
// 防重复提交的切面
@Aspect
@Component
public class SubmitAspect {
    
    @Pointcut("@annotation(banRepeatSubmit)")
    public void pointCut(BanRepeatSubmit banRepeatSubmit) {}

    @Around("pointCut(banRepeatSubmit)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, BanRepeatSubmit banRepeatSubmit) throws Throwable {
        // 防止对象方法重复执行
    }
}
```

### 1.3 AOP的原理

在使用上文的六种注解时，这些注解是封装在一个由`@AspectJ`注解修饰的类，我们管这个类叫做**切面**。

Spring AOP扫描到@Pointcut定义的切点时，就会自动为该Bean创建一个代理。而Spring Boot目前底层的代理模式有两种：JDK动态代理、CGLIB动态代理。

如果被代理的对象实现了接口，则Spring会默认使用JDK动态代理；如果被代理对象没有实现接口，则Spring会改为使用CGLIB动态代理。原因是JDK动态代理要求被代理对象必须实现至少一个接口。

JDK动态代理通过生成代理对象的**字节码文件**，使要拦截的方法跳转到invoke()方法，而在invoke()里就是在**切面**里定义的各种拦截逻辑。

而CGLIB是通过生成代理类的**子类实现**，同时**修改字节码**文件让子类方法覆盖代理类的方法，从而实现对拦截方法的代理。

另外Spring AOP还集成了AspectJ，相比与以上的动态织入方式，AspectJ采用**编译织入**的方式来代理对象。

### 1.4 JDK和CGCLIB动态代理

JDK和CGCLIB动态代理哪个更快？这两者的生命周期可以分为创建对象阶段、实际运行阶段，我们要根据具体情况具体分析。

1. 在**实际运行**阶段，CGLIB性能比JDK运行性能**更高**。
2. 在**创建对象**阶段，基于两者的原理，CGLIB花费在**创建对象**的时间要比JDK多。JDK**只需**创建代理类的字节码，**而**CGLIB既要修改源代码的字节码文件，又要生成代理类的子类的字节码文件。

综上所述，对于需要创建大量对象的场景，JD动态代理K比CGLIB动态代理效率更高，反之CGLIB效率更高。

## 2. Spring循环依赖

### 2.1. 解决Spring循环依赖

谈到循环依赖，大多数人都是望而生畏。一旦发生了循环依赖，说明了这部分软件设计的**职责划分**出现了问题，而且要修复起来不是一件容易的事。如果是屎山代码，那可就头大了。。。

如下例子，对象A的成员变量引用了对象B，而对象B的成员变量引用了对象A。也就是说，对象A的初始化完成要先完成对象B的初始化，但对象B的初始化完成又要先完成对象A的初始化，形成了一个永远无法实现的环。

```java
    class A {
        B b = new B();
    }
    class B {
        A a = new A();
    }
```

Spring设计出了三个级别的缓存。一级缓存存放实例化、初始化的bean、二级缓存存放已实例化但没有初始化的bean、三级缓存存放创建**bean的工厂对象**。

例如现在有对象A依赖对象B，对象B依赖对象A。

Spring首先从**三级缓存**获得实例化的A、B；接着让A进入**二级缓存**，同时将**未初始化**的B注入A，这就得到了实例化、初始化的**A**，此时A就会进入**一级缓存**；最后一步，只需要再把初始化的A注入到B，此时循环依赖解决。