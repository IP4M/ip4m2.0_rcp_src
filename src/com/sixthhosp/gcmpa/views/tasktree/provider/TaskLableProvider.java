package com.sixthhosp.gcmpa.views.tasktree.provider;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.sixthhosp.gcmpa.tasks.ITask;
import com.sixthhosp.gcmpa.tasks.XmlTask;
import com.sixthhosp.gcmpa.views.tasktree.TaskTableViewer;

public class TaskLableProvider extends LabelProvider implements
		ITableLabelProvider {

	private static String moduleTaskImagePath = "icons/persp_tab.gif";

	private Image moduleTaskImage;

	private TaskTableViewer taskTableViewer;

	private int i;

	private Thread thread;

	public TaskLableProvider() {
		super();
		moduleTaskImage = ImageDescriptor.createFromFile(
				TaskLableProvider.class, moduleTaskImagePath).createImage();
	}

	public TaskLableProvider(TaskTableViewer taskTreeViewer) {
		this();
		this.i = 1;
		this.taskTableViewer = taskTreeViewer;
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		if (columnIndex == 0) {
			return moduleTaskImage;
		}
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		if (columnIndex == 0) {
			if (element instanceof XmlTask) {
				XmlTask xmlTask = (XmlTask) element;
				return "Task: " + xmlTask.getTaskname();
			}
		} else if (columnIndex == 1) {
			if (element instanceof XmlTask) {
				XmlTask xmlTask = (XmlTask) element;
				String string = xmlTask.getStarttime();
				if (string == null || string.equals("")) {
					return "";
				} else {
					return string;
				}
			}
		} else if (columnIndex == 2) {
			if (element instanceof XmlTask) {
				XmlTask xmlTask = (XmlTask) element;
				String string = xmlTask.getEndtime();
				if (string == null || string.equals("")) {
					return "";
				} else {
					return string;
				}
			}
		} else if (columnIndex == 3) {
			if (element instanceof XmlTask) {
				XmlTask xmlTask = (XmlTask) element;
				String state = xmlTask.getTaskstate();
				if (state.equals(ITask.RUNNING)) {
					if (i == 1) {
						return "Running";
					} else if (i == 2) {
						return "Running.";
					} else if (i == 3) {
						return "Running..";
					} else if (i == 4) {
						return "Running...";
					}
				} else {
					return state;
				}
			}
		}
		return null;
	}

	/**
	 * 启动一个时钟线程，实时刷新任务管理器
	 */
	public void startDynamicRunningStat() {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					while (!Thread.interrupted()) {
						Thread.sleep(1000);
						if (i == 1) {
							setI(2);
							if (!taskTableViewer.getTable().isDisposed()) {
								taskTableViewer.getTable().getDisplay()
										.asyncExec(new Runnable() {
											public void run() {
												if (!taskTableViewer.getTable()
														.isDisposed()) {
													taskTableViewer.refresh();
												}
											}
										});
							}
						} else if (i == 2) {
							setI(3);
							if (!taskTableViewer.getTable().isDisposed()) {
								taskTableViewer.getTable().getDisplay()
										.asyncExec(new Runnable() {
											public void run() {
												if (!taskTableViewer.getTable()
														.isDisposed()) {
													taskTableViewer.refresh();
												}
											}
										});
							}
						} else if (i == 3) {
							setI(4);
							if (!taskTableViewer.getTable().isDisposed()) {
								taskTableViewer.getTable().getDisplay()
										.asyncExec(new Runnable() {
											public void run() {
												if (!taskTableViewer.getTable()
														.isDisposed()) {
													taskTableViewer.refresh();
												}
											}
										});
							}
						} else if (i == 4) {
							setI(1);
							if (!taskTableViewer.getTable().isDisposed()) {
								taskTableViewer.getTable().getDisplay()
										.asyncExec(new Runnable() {
											public void run() {
												if (!taskTableViewer.getTable()
														.isDisposed()) {
													taskTableViewer.refresh();
												}
											}
										});
							}
						} else {
							break;
						}
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
				}
			}
		};
		thread = new Thread(runnable);
		thread.start();
	}

	/**
	 * 杀死时钟线程
	 */
	public void killThread() {
		thread.interrupt();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		// 在任务管理器被关闭时，关闭时钟线程
		killThread();

		super.dispose();
		moduleTaskImage.dispose();
	}

	public static String getModuleTaskImagePath() {
		return moduleTaskImagePath;
	}

	public static void setModuleTaskImagePath(String moduleTaskImagePath) {
		TaskLableProvider.moduleTaskImagePath = moduleTaskImagePath;
	}

	public TaskTableViewer getTaskTreeViewer() {
		return taskTableViewer;
	}

	public void setTaskTreeViewer(TaskTableViewer taskTreeViewer) {
		this.taskTableViewer = taskTreeViewer;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public Thread getThread() {
		return thread;
	}

}
