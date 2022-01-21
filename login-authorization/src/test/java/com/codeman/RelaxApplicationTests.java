package com.codeman;

import com.codeman.mapper.AdminMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;


@RunWith(SpringRunner.class)
@SpringBootTest
public class RelaxApplicationTests {

	@Resource
	private AdminMapper adminMapper;
	@Test
	public void test() {
		System.out.println(adminMapper.findAll());
	}
}
