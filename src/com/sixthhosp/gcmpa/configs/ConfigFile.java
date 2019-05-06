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
 * 存放配置文件信息
 * 
 * @author zhengzequn
 *
 */
public class ConfigFile {
	/**
	 * 软件根目录路径
	 */
	private static String softFolderPath;

	/**
	 * 工程根目录路径
	 */
	private static String projectFolderPath;

	/**
	 * 用户目录路径
	 */
	private static String userFolderPath;

	/**
	 * 存放用户数据跟目录路径
	 */
	private static String dataFolderPath;

	/**
	 * 存放任务分析结果目录路径
	 */
	private static String taskResultsFolderPath;

	/**
	 * 存放任务信息目录路径
	 */
	private static String taskInfoFolderPath;

	/**
	 * 临时目录
	 */
	private static String tmpFolderPath;

	/**
	 * 存放任务信息xml配置文件路径
	 */
	private static String taskInfoXmlPath;

	/**
	 * 文件锁路径
	 */
	private static String lockFilePath;

	/**
	 * loc选项配置信息文件存放目录
	 */
	private static String locFolderPath;

	/**
	 * tool_conf文件所在路径
	 */
	private static String toolConfXmlPath;

	/**
	 * 获取软件跟目录路径
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
	 * 获取工程根目录路径
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
	 * 获取用户目录路径
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
	 * 获取存放用户数据跟目录路径
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
	 * 获取存放任务分析结果目录路径
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
	 * 获取存放任务信息目录路径
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
	 * 获取存放任务信息xml配置文件路径
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
	 * 获取文件锁路径
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
	 * 获取loc选项配置信息文件存放目录
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
	 * 初始化软件相关目录和相关配置文件
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
