package org.codeman;

import org.codeman.component.AsyncTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;


@RunWith(SpringRunner.class)
@SpringBootTest
public class AsyncApplicationTests {

    @Autowired
    private AsyncTest asyncTest;

	@Test
	public void test() throws InterruptedException {
		// 异步代表非按序执行
	    asyncTest.asyncTest();

		for (int i = 0; i < 2; i++) {
			if (i == 0) {
				System.out.println("--------------------------the main thread executed--------------------------");
				TimeUnit.SECONDS.sleep(2); // 确保方法在主线程执行完之前执行完，否则出现方法未执行完而主线程执行完了，则该方法被关闭
			} else if (i == 1) {
				System.out.println("--------------------------the main thread is finished--------------------------");
			}
		};
	}

}
