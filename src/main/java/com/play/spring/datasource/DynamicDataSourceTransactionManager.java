package com.play.spring.datasource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;

/**
 * created on 2016-07-05 13:39
 *
 * @author zhouyu
 */
public class DynamicDataSourceTransactionManager extends DataSourceTransactionManager {

//	private Logger logger = LoggerFactory.getLogger(this.getClass());
	/**
	 * 主数据库的key
	 */
	private String masterDataSourceKey;
	/**
	 * 从数据库的key列表
	 */
	private List<String> slaveDataSourceKeys;
	/**
	 * 使用权重设置从数据库key列表
	 */
	private Map<String, Integer> slaveDataSourceWeightMap;

	private AtomicInteger counter = new AtomicInteger();

	public void setMasterDataSourceKey(String masterDataSourceKey) {
		this.masterDataSourceKey = masterDataSourceKey;
	}

	public void setSlaveDataSourceKeys(List<String> slaveDataSourceKeys) {
		this.slaveDataSourceKeys = slaveDataSourceKeys;
	}

	public void setSlaveDataSourceWeightMap(Map<String, Integer> slaveDataSourceWeightMap) {
		this.slaveDataSourceWeightMap = slaveDataSourceWeightMap;
	}

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		if (this.slaveDataSourceWeightMap == null && slaveDataSourceKeys == null) {
			throw new IllegalArgumentException("Property 'slaveDataSourceKeys' or 'slaveDataSourceWeightMap' is required");
		}
		if (slaveDataSourceKeys == null) {
			this.slaveDataSourceKeys = new ArrayList<>();
		}
		if (slaveDataSourceWeightMap != null && !slaveDataSourceWeightMap.isEmpty()) {
			for (Map.Entry<String, Integer> entry : this.slaveDataSourceWeightMap.entrySet()) {
				if (entry.getValue() != null && entry.getValue() > 0) {
				//	logger.info(entry.getKey() + " 的权重＝" + entry.getValue());
					for (int i = 0; i < entry.getValue(); i++) {
						slaveDataSourceKeys.add(entry.getKey());
					}
				}else{
					slaveDataSourceKeys.add(entry.getKey());
				}
			}
			Collections.shuffle(slaveDataSourceKeys);

			StringBuilder sb = new StringBuilder("从数据库列表如下：");
			for (int i = 0; i < slaveDataSourceKeys.size(); i++) {
				sb.append(slaveDataSourceKeys.get(i)).append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			//logger.info(sb.toString());
		}
		if (slaveDataSourceKeys.isEmpty()) {
		//	logger.info("从库为空，使用主库");
			slaveDataSourceKeys.add(masterDataSourceKey);
		}
	}

	/**
	 * 只读事务到从库 读写事务到主库
	 *
	 * @param transaction
	 * @param definition
	 */
	@Override
	protected void doBegin(Object transaction, TransactionDefinition definition) {
		boolean readOnly = definition.isReadOnly();
		// logger.info("readOnly: " + readOnly);
		if (readOnly) {
			int count = counter.incrementAndGet();
			if (count > 1000000) {
				counter.set(0);
			}
			int size = slaveDataSourceKeys.size();
			int index = count % size;
			// logger.info("slave: "+slaveDataSourceKeys.get(index));
			DataSourceHolder.setDataSource(slaveDataSourceKeys.get(index));

		} else {
			DataSourceHolder.setDataSource(masterDataSourceKey);
		}
		super.doBegin(transaction, definition);
	}

	/**
	 * 清理本地线程的数据源
	 *
	 * @param transaction
	 */
	@Override
	protected void doCleanupAfterCompletion(Object transaction) {
		DataSourceHolder.clearDataSource();
		super.doCleanupAfterCompletion(transaction);
	}
}
