package com.sixthhosp.gcmpa.tasks;

public interface ITask {
	public static String FINISHED = "Finished";
	public static String FAILED = "Failed";
	public static String TERMINATED = "Terminated";
	public static String RUNNING = "Running";
	public static String WAITING = "Waiting";

	/**
	 * 外部调用程序正常运行退出码
	 */
	public static int EXITVALUE = 0;

	/**
	 * 确认任务是否结束，在start之后调用, true完成，false未完成
	 * 
	 * @return
	 */
	public boolean isAllFinished();

	/**
	 * 启动命令
	 */
	public void start();

	/**
	 * 确认是否处于能够停止的状态
	 * 
	 * @return
	 */
	public boolean canStop();

	/**
	 * 终止命令
	 */
	public void stop();

	/**
	 * 删除与任务相关的文件，包含标准输出、错误输出、参数XML文件
	 */
	public void deleteRelatedFiles();
}
