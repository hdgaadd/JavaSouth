package com.codeman;


import com.codeman.entity.User;
import com.codeman.rocketMQ.Producer;
import com.codeman.service.TestImpl;
import com.codeman.service.impl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RelaxApplicationTests {

	@Resource
	private Producer producer;

	@Test
	public void test() throws InterruptedException {
		producer.addMoney();

		new CountDownLatch(1).await();
		//上面代码实现消息生产者阻塞，如果不执行生产者阻塞的话，程序会立刻退出，导致消费者接受不到消息
		//但此时消息还会保存在rocketMQ的消息队列中，下一次运行程序消费者会接受到上一次的消息和本次的消息
	}

}
