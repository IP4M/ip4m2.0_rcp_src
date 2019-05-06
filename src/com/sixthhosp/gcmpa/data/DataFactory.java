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
 * ���ݹ��������ڴ�ų�ʼ�������������Ϣ
 * 
 * @author zhengzequn
 * 
 */
public class DataFactory {

	private static ToolBox toolBox;
	private static TaskBox taskBox;

	/**
	 * ��ȡģ�鹤��������������
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
	 * ��ȡ���������������ģ��
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
