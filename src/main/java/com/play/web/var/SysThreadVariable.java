package com.play.web.var;

public class SysThreadVariable {
	private static ThreadLocal<SysThreadObject> dataThread = new ThreadLocal<>();
	
	/**
	 * 初始化赋值
	 * 
	 * @param data
	 */
	public static void set(SysThreadObject data) {
		dataThread.set(data);
	}

	public static SysThreadObject get() {
		return dataThread.get();
	}
	

	/**
	 * 清除
	 */
	public static void clear() {
		dataThread.set(null);
		dataThread.remove();
	}
	
}
