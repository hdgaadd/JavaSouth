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
	@Resource
	private RedissonClient redissonClient;

	@Test
	public void testLock() {
		RLock lock = redissonClient.getLock("redisson");
		try {
			lock.lock();
			try {
				System.out.println("i am server-two");
			} catch (Exception e) {

			}
		} finally {
			lock.unlock();
		}

	}
}
