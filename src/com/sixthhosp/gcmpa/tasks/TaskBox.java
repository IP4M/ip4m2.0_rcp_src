package com.sixthhosp.gcmpa.tasks;

import java.util.ArrayList;

import org.apache.commons.lang.ArrayUtils;

import com.sixthhosp.gcmpa.xmlbeans.tasks.TaskType;
import com.sixthhosp.gcmpa.xmlbeans.tasks.TasksType;

/**
 * 任务内容封装器，用于保存任务信息
 * 
 * @author zhengzequn
 *
 */
public class TaskBox {

	private ArrayList<ITask> tasks;
	private static final int maxTasks = 200;

	public TaskBox() {
		tasks = new ArrayList<ITask>();
	}

	public TaskBox(TasksType tasksType) {
		this();
		TaskType[] types = tasksType.getTaskArray();
		for (TaskType type : types) {
			ITask task = new XmlTask(type);
			tasks.add(task);
		}
	}

	/**
	 * 添加任务到taskBox
	 * 
	 * @param task
	 */
	public synchronized void add(ITask task) {
		if (tasks.size() < maxTasks) {
			tasks.add(task);
		} else {
			ITask e = tasks.remove(0);
			e.deleteRelatedFiles();
			tasks.add(task);
		}
	}

	/**
	 * 删除任务
	 * 
	 * @param task
	 */
	public synchronized void delete(ITask task) {
		task.deleteRelatedFiles();
		tasks.remove(task);
	}

	/**
	 * 删除所有已经结束的任务
	 */
	public synchronized void deleteAllFiTasks() {
		ArrayList<ITask> list = new ArrayList<ITask>();
		list.addAll(tasks);
		for (ITask task : list) {
			if (task.isAllFinished()) {
				task.deleteRelatedFiles();
				tasks.remove(task);
			}
		}
	}

	/**
	 * 返回任务对象数组，注意！数组被反转
	 * 
	 * @return
	 */
	public synchronized ITask[] getTasksArrary() {
		ITask[] array = tasks.toArray(new ITask[tasks.size()]);
		ArrayUtils.reverse(array);
		return array;
	}

	/**
	 * 检查任务管理器是否存在正在运行的任务
	 * 
	 * @return
	 */
	public synchronized boolean isRunningTasksExist() {
		boolean b = false;
		for (ITask task : tasks) {
			if (!task.isAllFinished()) {
				b = true;
			}
		}
		return b;
	}

	/**
	 * 终止所有未完成的任务
	 */
	public synchronized void stopAllNotFinishedTasks() {
		for (ITask task : tasks) {
			if (!task.isAllFinished()) {
				if (task.canStop()) {
					task.stop();
				}
			}
		}
	}

	public synchronized ArrayList<ITask> getTasks() {
		return tasks;
	}

	public synchronized TasksType saveASTasksType() {
		TasksType tasksType = TasksType.Factory.newInstance();
		for (ITask task : tasks) {
			if (task instanceof XmlTask) {
				XmlTask xmlTask = (XmlTask) task;
				if (xmlTask.isAllFinished()) {
					tasksType.addNewTask().set(xmlTask.saveAsTaskType());
				}
			}
		}
		return tasksType;
	}
}
