package org.codeman.factory;

import java.util.ArrayList;
import java.util.List;

 // 工厂方法模式
public class FactoryMethod {
    abstract static class Farm {
        List<Animal> productList = new ArrayList<>(); // 只能生产同一类型的产品动物，而不能生产多种类型的产品，例如水果
    }

    static class GuangZhouFarm extends Farm {
        public String toString() {
            return "广州农场养殖或种植：" + productList;
        }
    }

    static class BeiJingFarm extends Farm {
        public String toString() {
            return "北京农场养殖或种植：" + productList;
        }
    }

    abstract static class Animal { }

    static class horse extends Animal {
        public String toString() {
            return "马";
        }
    }

    static class pig extends Animal {
        public String toString() {
            return "猪";
        }
    }

    public static void main(String[] args) { // 添加产品只需要添加一个产品类，而不用在专门生产产品的类中添加程序段
        Farm farm1 = new BeiJingFarm(); // 北京农场
        farm1.productList.add(new horse());
        farm1.productList.add(new pig());
        System.out.println(farm1);

        Farm farm2 = new GuangZhouFarm(); // 广州农场
        farm2.productList.add(new horse());
        System.out.println(farm2);
    }
}
