package com.codeman;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


@RunWith(SpringRunner.class)
@SpringBootTest
public class RelaxApplicationTests {
	// introduce:
	// server-two没有休眠则输出，而server-one休眠16s才输出
	// 而server-one率先获得锁，故server-two要等16s，在server-one输出后才输出
	@Resource
	private RedissonClient redissonClient;

	@Test
	public void testLock() {
		RLock lock = redissonClient.getLock("redisson");
		try {
			lock.lock();
			try {
				TimeUnit.SECONDS.sleep(16);
				System.out.println("i am server-one");
			} catch (Exception e) {

			}
		} finally {
			lock.unlock();
		}

	}
}
