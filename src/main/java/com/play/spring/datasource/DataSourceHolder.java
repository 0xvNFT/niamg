package com.play.spring.datasource;

/**
 * created on 2016-07-05 12:01
 *
 * @author zhouyu
 */
public class DataSourceHolder {

    /**
     * dataSource master or slave
     */
    private static final ThreadLocal<String> dataSource = new ThreadLocal<String>();


    public static void setDataSource(String dataSourceKey) {
        dataSource.set(dataSourceKey);
    }

    public static String getDataSource() {
        return dataSource.get();
    }

    /**
     * 清除thread local中的数据源
     */
    public static void clearDataSource() {
        dataSource.remove();
    }

}
