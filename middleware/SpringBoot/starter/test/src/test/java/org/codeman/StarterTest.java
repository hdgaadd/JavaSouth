package org.codeman;

import org.codeman.service.INoStarterSplitService;
import org.codeman.service.ISplitService;
import org.codeman.service.NoStarterSplitServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author hdgaadd
 * created on 2022/12/17
 *
 * description:
 *  - starter实现把bean交个IOC容器管理，而非starter的依赖无法实现
 *  - starter可配置当存在某些class、某些特定的bean，才交给IOC管理。该特定的bean可以是dubbo的某特定bean，代表该项目引入了dubbo依赖
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StarterTest {

    private static final String parameter = "name hdgaadd";

    @Resource
    private ISplitService service;

//    // 依赖失败
//    @Resource
//    private INoStarterSplitService noStarterService;

    @Test
    public void starterTest() {
        System.out.println(service.split(parameter));
    }

    @Test
    public void noStarterTest() {
        INoStarterSplitService service = new NoStarterSplitServiceImpl();
        System.out.println(service.split(parameter));
    }
}
