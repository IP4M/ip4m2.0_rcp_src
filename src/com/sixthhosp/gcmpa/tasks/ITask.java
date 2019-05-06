package com.sixthhosp.gcmpa.tasks;

public interface ITask {
	public static String FINISHED = "Finished";
	public static String FAILED = "Failed";
	public static String TERMINATED = "Terminated";
	public static String RUNNING = "Running";
	public static String WAITING = "Waiting";

	/**
	 * �ⲿ���ó������������˳���
	 */
	public static int EXITVALUE = 0;

	/**
	 * ȷ�������Ƿ��������start֮�����, true��ɣ�falseδ���
	 * 
	 * @return
	 */
	public boolean isAllFinished();

	/**
	 * ��������
	 */
	public void start();

	/**
	 * ȷ���Ƿ����ܹ�ֹͣ��״̬
	 * 
	 * @return
	 */
	public boolean canStop();

	/**
	 * ��ֹ����
	 */
	public void stop();

	/**
	 * ɾ����������ص��ļ���������׼������������������XML�ļ�
	 */
	public void deleteRelatedFiles();
}
