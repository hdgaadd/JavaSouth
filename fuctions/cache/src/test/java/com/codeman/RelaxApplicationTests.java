package com.codeman;

import com.codeman.component.CacheTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class RelaxApplicationTests {

	@Autowired
    private CacheTest cacheTest;

	@Test
	public void test() {
		System.out.println(cacheTest.test(1));
		System.out.println(cacheTest.test(1));
	}

}
