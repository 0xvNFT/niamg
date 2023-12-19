package com.play.spring.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * created on 2016-07-05 11:51
 *
 * @author zhouyu
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
//	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	protected Object determineCurrentLookupKey() {
//		logger.info("DynamicDataSource : " + DataSourceHolder.getDataSource());
		return DataSourceHolder.getDataSource();
	}

}
