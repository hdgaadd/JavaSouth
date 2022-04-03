package com.codeman;

import com.codeman.mapper.UserInfoMapper;
import com.codeman.model.UserInfo;
import io.shardingsphere.api.HintManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShardJdbcApplication.class)
@Slf4j
public class ShardJdbcApplicationTests {

    @Autowired
    UserInfoMapper userInfoMapper;

    /**
     * 读写分离测试，写操作只在主数据库，读操作只在从数据库
     */
    @Test
    public void test_write_read() {
        userInfoMapper.deleteByPrimaryKey(100L);

        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(100L);
        userInfo.setAccount("Account");
        userInfo.setPassword("pass");
        userInfo.setUserName("name");
        int insert = userInfoMapper.insertSelective(userInfo);
        log.info("插入主库结果:" + insert);

        UserInfo search = userInfoMapper.selectByPrimaryKey(100L); // 默认读操作，是在从数据库进行
        log.info("查询从库结果:" + search);

        HintManager.getInstance().setMasterRouteOnly();
        search = userInfoMapper.selectByPrimaryKey(100L);
        log.info("查询主库结果:" + search);
    }

    /**
     * 单库分表测试，数据插入多个表
     */
    @Test
    public void test_insert() {
        for (int i = 20; i < 30; i++) {
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId((long) i);
            userInfo.setAccount("Account" + i);
            userInfo.setPassword("pass" + i);
            userInfo.setUserName("name" + i);

            userInfoMapper.insert(userInfo);
        }
    }

}
