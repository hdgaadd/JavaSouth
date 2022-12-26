package org.codeman.factory;

import java.util.ArrayList;
import java.util.List;

// 抽象工厂模式
abstract class Farm {
    List<Product> productList=new ArrayList<>(); // 可以生产多种类型的产品：动物、水果
}

class GuangZhouFarm extends Farm {
    public String toString() { return "广州农场养殖或种植："+productList; }
}

class BeiJingFarm extends Farm {
    public String toString() { return "北京农场养殖或种植："+productList; }
}

abstract class Product {
}

abstract class Animal extends Product {
}

abstract class Plant extends Product {
}

class horse extends Animal {
    public String toString() { return "马"; }
}

class pig extends Animal {
    public String toString() { return "猪"; }
}

class apple extends Plant {
    public String toString() { return "苹果"; }
}

class watermelon extends Plant {
    public String toString() { return "西瓜"; }
}

public class AbstractFactory {
    public static void main(String[] args) {
        Farm farm1 = new BeiJingFarm(); // 北京农场
        farm1.productList.add(new horse());
        farm1.productList.add(new apple());
        farm1.productList.add(new watermelon());
        System.out.println(farm1);

        Farm farm2 = new GuangZhouFarm(); // 广州农场
        farm2.productList.add(new pig());
        farm2.productList.add(new apple());
        System.out.println(farm2);
    }
}