package com.sixthhosp.gcmpa;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.sixthhosp.gcmpa.data.DataFactory;
import com.sixthhosp.gcmpa.tasks.XmlTask;

/**
 * 窗口关闭前的一些操作
 * 
 * @author zhengzequn
 * 
 */
public class PreWindowCloseWorks {

	/**
	 * 程序关闭前进行数据保存操作
	 */
	public static void saveData() {
		ProgressMonitorDialog pmd = new ProgressMonitorDialog(PlatformUI
				.getWorkbench().getActiveWorkbenchWindow().getShell());
		IRunnableWithProgress rwp = new IRunnableWithProgress() {

			@Override
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException, InterruptedException {
				monitor.beginTask("Saving...", 10);
				// TODO 相应的业务逻辑

				monitor.subTask("Save Tasks Viewer...");
				Thread.sleep(100);
				DataFactory.saveTaskBox();
				monitor.worked(10);

				monitor.done();
			}
		};
		try {
			pmd.run(true, false, rwp);
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 确认是否关闭正在运行的程序，true关闭程序，false为取消关闭程序
	 * 
	 * @return
	 */
	public static boolean questionCloseRunningTask() {
		if (DataFactory.getTaskBox().isRunningTasksExist()
				&& XmlTask.jobsNum != 0) {
			String title = "Confirm Exit";
			String message = "Some tasks are still under running, do you really want to stop them and exit?";
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getShell();
			boolean b = MessageDialog.openQuestion(shell, title, message);
			if (b) {
				DataFactory.getTaskBox().stopAllNotFinishedTasks();
				while (XmlTask.jobsNum != 0) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	/**
	 * 确认是否关闭正在运行的程序，true关闭程序，false为取消关闭程序
	 * 
	 * @return
	 */
	public static boolean questionCloseRunningTaskForRestart() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell();

		if (DataFactory.getTaskBox().isRunningTasksExist()
				&& XmlTask.jobsNum != 0) {
			String title = "Confirm Restart";
			String message = "Some tasks are still under running, do you really want to stop them and restart?";
			boolean b = MessageDialog.openQuestion(shell, title, message);
			if (b) {
				DataFactory.getTaskBox().stopAllNotFinishedTasks();
				while (XmlTask.jobsNum != 0) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}
}
