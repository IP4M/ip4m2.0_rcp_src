package com.sixthhosp.gcmpa.configs;

import java.io.File;
import java.util.Calendar;
import java.util.UUID;

/**
 * ���õ�һЩ�ļ���������
 * 
 * @author zhengzequn
 * 
 */
public class Utils {

	/**
	 * ��ȡ file to file �����·����Ϣ
	 * 
	 * @param base
	 * @param path
	 * @return
	 */
	public static String absToRel(String base, String path) {
		String relative = new File(base).toURI()
				.relativize(new File(path).toURI()).getPath();
		return relative;
	}

	/**
	 * ��ȡ��һ�������
	 * 
	 * @return
	 */
	public static String getRandomStringByUUID() {
		return UUID.randomUUID().toString();
	}

	/**
	 * ��ȡ��ǰ��ϵͳʱ��
	 * 
	 * @return
	 */
	public static String getCurrentTime() {
		Calendar c = Calendar.getInstance();

		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int date = c.get(Calendar.DATE);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);

		String time = year + "/" + month + "/" + date + "-" + hour + ":"
				+ minute + ":" + second;

		return time;
	}

	/**
	 * ��ȡ��ǰ��ϵͳʱ���ַ��������ڹ���ģ��������Ŀ¼
	 * 
	 * @return
	 */
	public static String getCurrentTime2() {
		Calendar c = Calendar.getInstance();

		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int date = c.get(Calendar.DATE);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);

		String time = year + "." + month + "." + date + "_" + hour + "."
				+ minute + "." + second;

		return time;
	}

	public static int[] getCurrentTime3() {
		int[] time = new int[6];
		Calendar c = Calendar.getInstance();

		time[0] = c.get(Calendar.YEAR);
		time[1] = c.get(Calendar.MONTH) + 1;
		time[2] = c.get(Calendar.DATE);
		time[3] = c.get(Calendar.HOUR_OF_DAY);
		time[4] = c.get(Calendar.MINUTE);
		time[5] = c.get(Calendar.SECOND);
		return time;

	}

	/**
	 * ����ģ��Ĺ���Ŀ¼
	 * 
	 * @return
	 */
	public static String generateRandomToolWorkingDir() {
		String path = ConfigFile.getTmpFolderPath() + "/"
				+ getRandomStringByUUID();
		return path;
	}
}
