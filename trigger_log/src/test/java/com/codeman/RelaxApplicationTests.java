package com.codeman;


import com.codeman.entity.User;
import com.codeman.service.TestImpl;
import com.codeman.service.impl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RelaxApplicationTests {

	@Resource
	private TestImpl testImpl;

	@Test
	public void testSelect() {
		System.out.println(("----- selectAll method test ------"));
		User byId = testImpl.getById(1);
		System.out.println(byId);
	}

}
