package com.sixthhosp.gcmpa.data;

import java.io.File;
import java.io.IOException;

import org.apache.xmlbeans.XmlException;

import com.sixthhosp.gcmpa.configs.ConfigFile;
import com.sixthhosp.gcmpa.tasks.TaskBox;
import com.sixthhosp.gcmpa.tools.ToolBox;
import com.sixthhosp.gcmpa.xmlbeans.tasks.TasksDocument;
import com.sixthhosp.gcmpa.xmlbeans.tasks.TasksType;
import com.sixthhosp.gcmpa.xmlbeans.toolconf.ToolboxDocument;

/**
 * 数据工厂，用于存放初始化软件的数据信息
 * 
 * @author zhengzequn
 * 
 */
public class DataFactory {

	private static ToolBox toolBox;
	private static TaskBox taskBox;

	/**
	 * 获取模块工具树的输入数据
	 * 
	 * @return
	 */
	public static ToolBox getToolBox() {
		if (toolBox == null) {
			try {
				ToolboxDocument toolboxDocument = ToolboxDocument.Factory
						.parse(new File(ConfigFile.getToolConfXmlPath()));
				toolBox = new ToolBox(toolboxDocument);
			} catch (XmlException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return toolBox;
	}

	/**
	 * 获取任务管理器的数据模型
	 * 
	 * @return the taskBox
	 */
	public static TaskBox getTaskBox() {
		if (taskBox == null) {
			try {
				TasksDocument tasksDocument = TasksDocument.Factory
						.parse(new File(ConfigFile.getTaskInfoXmlPath()));
				taskBox = new TaskBox(tasksDocument.getTasks());
			} catch (XmlException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return taskBox;
	}

	public synchronized static void saveTaskBox() {
		if (taskBox != null) {
			try {
				TasksDocument tasksDocument = TasksDocument.Factory
						.newInstance();
				TasksType tasksType = taskBox.saveASTasksType();
				tasksDocument.setTasks(tasksType);

				tasksDocument.save(new File(ConfigFile.getTaskInfoXmlPath()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
