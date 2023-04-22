package org.codeman.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.codeman.domain.MybatisPlus;
import org.codeman.mapper.MybatisPlusMapper;
import org.codeman.service.IMybatisPlusService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.util.List;

/**
 * @ClassName: MybatisPlusServiceImpl
 **/
@Slf4j
@Repository
public class MybatisPlusServiceImpl extends ServiceImpl<MybatisPlusMapper, MybatisPlus> implements IMybatisPlusService {

    public void queryTest() {
        lambdaQueryTest();
        queryWrapperTest();
    }

    private void lambdaQueryTest() {
        // SELECT 特定字段
        List<MybatisPlus> list = lambdaQuery()
                .eq(MybatisPlus::getId, 1)
                .select(MybatisPlus::getName)
                .list();
        log.info("SELECT 特定字段：" + list.toString());

        // SELECT *
        List<MybatisPlus> list1 = lambdaQuery()
                .eq(MybatisPlus::getId, 1)
                .select()
                .list();
        log.info("SELECT *：" + list1.toString());

        // SELECT name FROM mybatis_plus WHERE (id = ? AND name LIKE ?)
        List<MybatisPlus> list2 = lambdaQuery()
                .eq(MybatisPlus::getId, 2)
                .like(MybatisPlus::getName, "hdgaadd")
                .select(MybatisPlus::getName)
                .list();
        log.info("SELECT name FROM mybatis_plus WHERE (id = ? AND name LIKE ?)：" + list2.toString());

        // lambdaUpdate的set
        boolean updateResult = this.lambdaUpdate()
                .eq(MybatisPlus::getId, 1)
                .set(MybatisPlus::getName, "hdgaadd")
                .update();
        log.info("lambdaUpdate的set：" + updateResult);
    }

    private void queryWrapperTest() {
        // 链式函数查询lambdaQuery不能使用 SUM(字段名)，改用QueryWrapper
        QueryWrapper<MybatisPlus> sumWrapper = new QueryWrapper<>();
        sumWrapper.eq("name", "hdgaadd").select("IFNULL(sum(id),0) AS idCount");
        int idCount = Integer.parseInt(getMap(sumWrapper).get("idCount").toString());
        log.info("链式函数查询lambdaQuery不能使用 SUM(字段名)，改用QueryWrapper：" + idCount);

        // queryWrapper的set
        UpdateWrapper<MybatisPlus> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", 1)
               .set("name", "hdgaadd");
        log.info("queryWrapper的set：" + this.update(wrapper));
    }

}
