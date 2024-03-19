package org.codeman.utils;


import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codeman.bean.ApplicationXmlResult;
import org.codeman.bean.BeanDefinition;
import org.codeman.bean.PropertyDefinition;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


/**
 * @author hdgaadd
 * created on 2022/03/10
 *
 * description: copy
 */
public class XmlUtil {

    @SuppressWarnings("rawtypes")
    public static ApplicationXmlResult readAppXml(String fileName) {

        try {

            // 创建一个读取器
            SAXReader saxReader = new SAXReader();
            Document document = null;

            // 获取要读取的配置文件的路径
            URL xmlPath = XmlUtil.class.getClassLoader().getResource(fileName);

            // 读取文件内容
            document = saxReader.read(xmlPath);

            // 获取xml中的根元素
            Element rootElement = document.getRootElement();

            //不是beans根元素的，文件不对
            if (!"beans".equals(rootElement.getName())) {
                System.err.println("applicationContext.xml文件格式不对，缺少beans");
                return null;
            }

            ApplicationXmlResult result = new ApplicationXmlResult();
            List<BeanDefinition> beanDefines = new ArrayList<>();
            result.setBeanDefinitions(beanDefines);

            for (Iterator iterator = rootElement.elementIterator(); iterator.hasNext();) {
                Element element = (Element) iterator.next();
                String eleName = element.getName();
                if ("component-scan".equals(eleName)) {

                    //扫描目录
                    String scanPackage = element.attributeValue("base-package");
                    result.setComponentScan(scanPackage);

                } else if ("bean".equals(eleName)) {

                    //扫描并解析bean定义
                    beanDefines.add(parseBeanDefine(element));
                } else {
                    System.out.println("不支持此xml标签解析:" + eleName);
                }

            }

            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 解析bean标签
     *
     * @param element
     * @return
     */
    @SuppressWarnings("rawtypes")
    private static BeanDefinition parseBeanDefine(Element element) {
        String id = element.attributeValue("id");// 获取bean的id属性值
        String clazz = element.attributeValue("class");// 获取bean的class属性值
        BeanDefinition beanDefinition = new BeanDefinition(id, clazz);
        // 获取bean的Property属性
        for (Iterator subElementIterator = element.elementIterator(); subElementIterator.hasNext();) {
            Element subElement = (Element) subElementIterator.next();
            String propertyName = subElement.attributeValue("name");
            String propertyRef = subElement.attributeValue("ref");
            String propertyValue = subElement.attributeValue("value");
            PropertyDefinition propertyDefinition = new PropertyDefinition(propertyName, propertyValue,
                    propertyRef);
            beanDefinition.addPropertyDefinition(propertyDefinition);
        }
        return beanDefinition;
    }

}
