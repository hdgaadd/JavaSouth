package org.codeman;

import org.codeman.component.CacheTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;


@RunWith(SpringRunner.class)
@SpringBootTest
public class CacheApplicationTests {

	@Resource
    private CacheTest cacheTest;

	@Test
	public void test() {
		System.out.println(cacheTest.test(1));
		System.out.println(cacheTest.test(1));
	}

}
