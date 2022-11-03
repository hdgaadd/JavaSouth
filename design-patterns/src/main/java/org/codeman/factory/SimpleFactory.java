package org.codeman.factory;

import java.util.ArrayList;
import java.util.List;

//简单工厂模式
public class SimpleFactory {
    public static void main(String[] args) {
        Farm farm = new Farm();
        farm.newFarm1();
        farm.newFarm2();
    }
    //把创造工厂与产品与主程序解耦，添加产品只需在专门生产产品的Farm类，添加生产产品的程序段，而不用在主程序
    static class Farm{
        public void newFarm1(){
            List<String> productList=new ArrayList<>();
            productList.add("马");productList.add("苹果");productList.add("西瓜");
            System.out.print("北京农场养殖或种植：");
            System.out.println(productList);
        }
        public void newFarm2(){
            List<String> productList=new ArrayList<>();
            productList.add("猪");productList.add("苹果");
            System.out.print("广州农场养殖或种植：");
            System.out.println(productList);
        }
    }
}
