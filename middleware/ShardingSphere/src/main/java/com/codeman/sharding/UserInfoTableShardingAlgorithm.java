package com.codeman.sharding;

import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.api.algorithm.sharding.standard.PreciseShardingAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

@Slf4j
public class UserInfoTableShardingAlgorithm implements PreciseShardingAlgorithm<Long> { // 配置分片策略，把数据分配到多个表中

	@Override
	public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
		for (String tableName : availableTargetNames) {
			if (tableName.endsWith(shardingValue.getValue() % 2 + "")) {
				log.info("TABLE_NAME: {}", tableName);
				return tableName;
			}
		}
		throw new IllegalArgumentException();
	}

}
