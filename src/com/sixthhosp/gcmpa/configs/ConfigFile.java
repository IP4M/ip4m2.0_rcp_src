package com.sixthhosp.gcmpa.configs;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.datalocation.Location;

import com.sixthhosp.gcmpa.xmlbeans.tasks.TasksDocument;

/**
 * ��������ļ���Ϣ
 * 
 * @author zhengzequn
 *
 */
public class ConfigFile {
	/**
	 * �����Ŀ¼·��
	 */
	private static String softFolderPath;

	/**
	 * ���̸�Ŀ¼·��
	 */
	private static String projectFolderPath;

	/**
	 * �û�Ŀ¼·��
	 */
	private static String userFolderPath;

	/**
	 * ����û����ݸ�Ŀ¼·��
	 */
	private static String dataFolderPath;

	/**
	 * �������������Ŀ¼·��
	 */
	private static String taskResultsFolderPath;

	/**
	 * ���������ϢĿ¼·��
	 */
	private static String taskInfoFolderPath;

	/**
	 * ��ʱĿ¼
	 */
	private static String tmpFolderPath;

	/**
	 * ���������Ϣxml�����ļ�·��
	 */
	private static String taskInfoXmlPath;

	/**
	 * �ļ���·��
	 */
	private static String lockFilePath;

	/**
	 * locѡ��������Ϣ�ļ����Ŀ¼
	 */
	private static String locFolderPath;

	/**
	 * tool_conf�ļ�����·��
	 */
	private static String toolConfXmlPath;

	/**
	 * ��ȡ�����Ŀ¼·��
	 * 
	 * @return
	 */
	public static String getSoftFolderPath() {
		if (softFolderPath == null) {
			Location location = Platform.getInstallLocation();
			URL url = location.getURL();
			String path = url.getPath();
			softFolderPath = path.substring(1, path.length() - 1);
		}
		return new File(softFolderPath).getAbsolutePath();
	}

	/**
	 * ��ȡ���̸�Ŀ¼·��
	 * 
	 * @return
	 */
	public static String getProjectFolderPath() {
		if (projectFolderPath == null) {
			try {
				projectFolderPath = FileLocator.toFileURL(
						Platform.getBundle("com.sixthhosp.gcmpa").getEntry(""))
						.getPath();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return projectFolderPath;
	}

	/**
	 * ��ȡ�û�Ŀ¼·��
	 * 
	 * @return
	 */
	public static String getUserFolderPath() {
		if (userFolderPath == null) {
			userFolderPath = System.getProperty("user.home");
		}
		return userFolderPath;
	}

	/**
	 * ��ȡ����û����ݸ�Ŀ¼·��
	 * 
	 * @return
	 */
	public static String getDataFolderPath() {
		if (dataFolderPath == null) {
			dataFolderPath = getUserFolderPath() + "/.ip4m";
		}
		return dataFolderPath;
	}

	/**
	 * ��ȡ�������������Ŀ¼·��
	 * 
	 * @return
	 */
	public static String getTaskResultsFolderPath() {
		if (taskResultsFolderPath == null) {
			taskResultsFolderPath = getUserFolderPath() + "/IP4M_Outputs";
		}
		return taskResultsFolderPath;
	}

	/**
	 * ��ȡ���������ϢĿ¼·��
	 * 
	 * @return
	 */
	public static String getTaskInfoFolderPath() {
		if (taskInfoFolderPath == null) {
			taskInfoFolderPath = getDataFolderPath() + "/task_infos";
		}
		return taskInfoFolderPath;
	}

	public static String getTmpFolderPath() {
		if (tmpFolderPath == null) {
			tmpFolderPath = getDataFolderPath() + "/tmp";
		}
		return tmpFolderPath;
	}

	/**
	 * ��ȡ���������Ϣxml�����ļ�·��
	 * 
	 * @return
	 */
	public static String getTaskInfoXmlPath() {
		if (taskInfoXmlPath == null) {
			taskInfoXmlPath = getTaskInfoFolderPath() + "/task_infos.xml";
		}
		return taskInfoXmlPath;
	}

	/**
	 * ��ȡ�ļ���·��
	 * 
	 * @return
	 * 
	 */
	public static String getLockFilePath() {
		if (lockFilePath == null) {
			lockFilePath = getDataFolderPath() + "/ip4m.lck";
		}
		return lockFilePath;
	}

	/**
	 * ��ȡlocѡ��������Ϣ�ļ����Ŀ¼
	 * 
	 * @return
	 */
	public static String getLocFolderPath() {
		if (locFolderPath == null) {
			locFolderPath = getSoftFolderPath() + "/loc";
		}
		return locFolderPath;
	}

	public static String getToolConfXmlPath() {
		if (toolConfXmlPath == null) {
			toolConfXmlPath = getSoftFolderPath() + "/configs/tool_conf.xml";
		}
		return toolConfXmlPath;
	}

	/**
	 * ��ʼ��������Ŀ¼����������ļ�
	 */
	public static void initial() {
		String[] dirList = { getDataFolderPath(), getTaskResultsFolderPath(),
				getTaskInfoFolderPath(), getTmpFolderPath() };
		for (int i = 0; i < dirList.length; i++) {
			File dir = new File(dirList[i]);
			if (dir.isAbsolute() && !dir.isDirectory() && !dir.exists()) {
				try {
					FileUtils.forceMkdir(dir);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		File tasksInfoFile = new File(getTaskInfoXmlPath());
		if (!tasksInfoFile.exists()) {
			try {
				TasksDocument tasksDocument = TasksDocument.Factory
						.newInstance();
				tasksDocument.addNewTasks();

				tasksDocument.save(tasksInfoFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
