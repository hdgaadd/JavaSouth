### Spring

**content**

- Spring IOC and Spring MVC

**process**

- 读取配置文件，扫描指定文件夹，创建bean的财产BeanDefinition
- 对创建的BeanDefinition进行保存
- 将所保存的BeanDefinition进行实例化 -> 注册
- 对所注册的BeanDefinition进行属性注入

**design**

- BeanFactory底层规范
- Application第二次规范
- AbstractApplicationContext规范实现者之一
- ClassPathXmlApplicationContext继承规范实现者，添加其他功能

**knowledge**

- 每一个规范的实现者，其遵循规范所创建出的功能**不同**
