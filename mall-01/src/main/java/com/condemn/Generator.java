package com.condemn;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;


public class Generator {
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/mall?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai", "root", "root")
                .globalConfig(builder -> {
                    builder.outputDir("D://"); // 指定输出目录
                })
                .strategyConfig(builder -> {
                    builder.addInclude("pms_brand"); // 设置需要生成的表名
                })
                .execute();
    }
}
