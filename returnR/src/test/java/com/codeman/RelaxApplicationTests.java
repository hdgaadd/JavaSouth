package com.codeman;

import com.codeman.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class RelaxApplicationTests {

	@Test
	public void test() {

		User user = new User();
		user.setName("dsfld");
		System.out.println(user.getName());
	}

}
