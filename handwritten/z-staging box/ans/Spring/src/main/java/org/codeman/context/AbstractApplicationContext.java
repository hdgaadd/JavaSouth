package org.codeman.context;

import org.codeman.bean.ApplicationXmlResult;
import org.codeman.bean.BeanDefinition;
import org.codeman.bean.DefaultListableBeanFactory;
import org.codeman.utils.XmlUtil;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hdgaadd
 * created on 2022/03/03
 *
 * description: 第二层规范实现者之一
 */
public abstract class AbstractApplicationContext implements ApplicationContext {

    protected String configFile = "application.xml";

    /**
     * 根据url获取方法对象
     */
    private Map<String, Method> methodMap = new ConcurrentHashMap<>();

    /**
     * 根据url获取Controller的beanId
     */
    private Map<String, String> controllerMap = new ConcurrentHashMap<>();

    private DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

    @Override
    public void refresh() {
        try {
            // step one: 读取配置文件
            ApplicationXmlResult result = XmlUtil.readAppXml(this.configFile);

            if (!Objects.isNull(result)) {
                // step one: 扫描指定文件夹，创建bean的财产BeanDefinition
                List<BeanDefinition> beanDefinitions = result.getBeanDefinitions();
                // doScanner()给beanDefinitions赋值
                this.doScanner(result.getComponentScan(), beanDefinitions);

                // step two: 对创建的BeanDefinition进行保存
                this.beanFactory.setBeanDefinitions(beanDefinitions);

                // step three: 将所保存的BeanDefinition进行实例化 -> 注册
                this.beanFactory.instanceBeans();

                // step four: 对所注册的BeanDefinition进行属性注入
                this.beanFactory.injectObjects();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public Method getHandlerMethod(String url) {
        return methodMap.get(url);
    }

    /**
     * singletonMap保存<beanId, bean实例>，controllerMap保存<url, beanId>
     *
     * @param url
     * @return
     */
    @Override
    public Object getController(String url) {
        if (!Objects.isNull(this.beanFactory.getSingletons())) {
            return beanFactory.getSingletons().get(controllerMap.get(url));
        }
        return null;
    }

    @Override
    public Object getBean(String beanName) {
        return this.beanFactory.getBean(beanName);
    }

    /**
     * 扫描指定包下的文件，将相应bean添加到该beanDefinitions里
     *
     * @param packageName
     * @param beanDefinitions
     */
    private void doScanner(String packageName, List<BeanDefinition> beanDefinitions) {

    }

    /**
     * 扫描指定类名，将相应bean添加到该集合里
     *
     * @param className
     * @param beanDefinitions
     */
    private void initScanAnnotation(String className, List<BeanDefinition> beanDefinitions) {

    }
}
