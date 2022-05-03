package org.codeman;
import org.codeman.job.JobInterface;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class RelaxApplicationTests {

	@Autowired
	private List<JobInterface> jobInterfaces; // 一个接口多实例

	@Test
	public void test() {
		for (JobInterface job: jobInterfaces) {
			job.task();
		}
	}

}
