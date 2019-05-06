package com.sixthhosp.gcmpa.tasks;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import com.sixthhosp.gcmpa.configs.ConfigFile;
import com.sixthhosp.gcmpa.configs.Utils;
import com.sixthhosp.gcmpa.editors.ParamEditor;
import com.sixthhosp.gcmpa.views.TasksView;
import com.sixthhosp.gcmpa.xmlbeans.tasks.TaskType;
import com.sixthhosp.gcmpa.xmlbeans.tool.ToolDocument;

public class XmlTask implements ITask {

	public static int jobsNum = 0;

	private String id;
	private String xmlinfopath;
	private String taskname;
	private String taskstate;
	private String starttime;
	private String endtime;
	private String commandline;
	private String stderrpath;
	private String stdoutpath;
	private String htmloutpath;
	private String outputsfolderpath;
	private String taskfolderpath;

	//
	private ParamEditor paramEditor;
	private TableViewer tableViewer;
	//
	private String workingDirPath;
	private File workingDirFile;
	private boolean allFinished;
	private ExecuteWatchdog watchdog;

	public XmlTask() {
		IViewPart viewPart = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage()
				.findView(TasksView.ID);
		Assert.isNotNull(viewPart);
		TasksView tasksView = ((TasksView) viewPart);
		tableViewer = tasksView.getTaskTableViewer();
	}

	public XmlTask(TaskType taskType) {
		this();
		this.id = taskType.getId();
		this.xmlinfopath = taskType.getXmlinfopath();
		this.taskname = taskType.getTaskname();
		this.taskstate = taskType.getTaskstate();
		this.starttime = taskType.getStarttime();
		this.endtime = taskType.getEndtime();
		this.commandline = taskType.getCommandline();
		this.stderrpath = taskType.getStderrpath();
		this.stdoutpath = taskType.getStdoutpath();
		this.htmloutpath = taskType.getHtmloutpath();
		this.outputsfolderpath = taskType.getOutputsfolderpath();
		this.taskfolderpath = taskType.getTaskfolderpath();

		this.allFinished = true;
	}

	public XmlTask(ParamEditor paramEditor) {
		this();
		this.paramEditor = paramEditor;
		id = Utils.getRandomStringByUUID();
		taskfolderpath = ConfigFile.getTaskInfoFolderPath() + "/" + id;
		xmlinfopath = taskfolderpath + "/module.xml";
		taskname = paramEditor.getTool().getName();
		commandline = paramEditor.getExecuteCommand();
		stderrpath = taskfolderpath + "/stdout.log";
		stdoutpath = taskfolderpath + "/stderr.log";
		htmloutpath = paramEditor.getHtmlToolOutput().getAbsOutputFilePath();
		outputsfolderpath = paramEditor.getOutputDirectoryFile()
				.getAbsolutePath();

		taskstate = WAITING;
		this.allFinished = false;
		workingDirFile = paramEditor.getWorkingDirectoryFile();
		workingDirPath = workingDirFile.getAbsolutePath();
	}

	@Override
	public boolean isAllFinished() {
		return allFinished;
	}

	@Override
	public void start() {
		try {
			allFinished = false;
			// 创建模块工作目录 创建输出结果目录 创建存放任务信息目录
			FileUtils.forceMkdir(workingDirFile);
			FileUtils.forceMkdir(new File(outputsfolderpath));
			FileUtils.forceMkdir(new File(taskfolderpath));

			// 执行任务
			File shellFile = new File(workingDirPath + File.separator
					+ "run.bat");
			FileUtils.writeStringToFile(shellFile, commandline);
			CommandLine commandLine = CommandLine.parse("\""
					+ shellFile.getAbsolutePath() + "\"");
			DefaultExecutor executor = new DefaultExecutor();
			executor.setWorkingDirectory(workingDirFile);
			watchdog = new ExecuteWatchdog(ExecuteWatchdog.INFINITE_TIMEOUT);
			executor.setWatchdog(watchdog);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
			PumpStreamHandler streamHandler = new PumpStreamHandler(
					outputStream, errorStream);
			executor.setStreamHandler(streamHandler);
			DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
			// 设置任务开始运行的时间
			int[] time = paramEditor.getTime();
			String t = time[0] + "/" + time[1] + "/" + time[2] + "-" + time[3]
					+ ":" + time[4] + ":" + time[5];
			starttime = t;
			executor.execute(commandLine, resultHandler);

			// 设置任务处于运行状态
			taskstate = RUNNING;
			// 刷新任务管理器
			refreshTaskBrowser();
			// 等待任务执行完成
			resultHandler.waitFor();

			if (watchdog.killedProcess()) {
				taskstate = TERMINATED;
			} else if (resultHandler.getExitValue() != EXITVALUE) {
				taskstate = FAILED;
			} else {
				taskstate = FINISHED;
			}

			// 保存标准输出和标准错误
			FileOutputStream stdoutFileOutputStream = new FileOutputStream(
					new File(stdoutpath));
			FileOutputStream stderrFileOutputStream = new FileOutputStream(
					new File(stderrpath));
			outputStream.writeTo(stdoutFileOutputStream);
			errorStream.writeTo(stderrFileOutputStream);
			// 保存模块xmlxinx
			saveToolXml();
			// 设置任务结束的时间
			endtime = Utils.getCurrentTime();
			// 刷新任务管理器
			refreshTaskBrowser();

			// 清理工作目录
			FileUtils.deleteQuietly(workingDirFile);

			allFinished = true;
		} catch (ExecuteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean canStop() {
		return taskstate.equals(RUNNING);
	}

	@Override
	public void stop() {
		if (watchdog.isWatching()) {
			watchdog.destroyProcess();
		}
	}

	/**
	 * 刷新任务管理器
	 */
	private void refreshTaskBrowser() {
		if (tableViewer != null && !tableViewer.getTable().isDisposed()) {
			tableViewer.getTable().getDisplay().asyncExec(new Runnable() {
				public void run() {
					if (tableViewer != null
							&& !tableViewer.getTable().isDisposed()) {
						tableViewer.refresh();
					}
				}
			});
		}
	}

	@Override
	public void deleteRelatedFiles() {
		if (StringUtils.isNotBlank(xmlinfopath)) {
			File file = new File(xmlinfopath);
			if (file.exists()) {
				FileUtils.deleteQuietly(file);
			}
		}
		if (StringUtils.isNotBlank(stdoutpath)) {
			File file = new File(stdoutpath);
			if (file.exists()) {
				FileUtils.deleteQuietly(file);
			}
		}
		if (StringUtils.isNotBlank(stderrpath)) {
			File file = new File(stderrpath);
			if (file.exists()) {
				FileUtils.deleteQuietly(file);
			}
		}
		if (StringUtils.isNotBlank(taskfolderpath)) {
			File file = new File(taskfolderpath);
			if (file.exists()) {
				FileUtils.deleteQuietly(file);
			}
		}
	}

	public TaskType saveAsTaskType() {
		TaskType taskType = TaskType.Factory.newInstance();
		taskType.setId(id);
		taskType.setXmlinfopath(xmlinfopath);
		taskType.setTaskname(taskname);
		taskType.setTaskstate(taskstate);

		if (starttime == null) {
			taskType.setStarttime("");
		} else {
			taskType.setStarttime(starttime);
		}
		if (endtime == null) {
			taskType.setEndtime(endtime);
		} else {
			taskType.setEndtime(endtime);
		}
		taskType.setCommandline(commandline);
		taskType.setStdoutpath(stdoutpath);
		taskType.setStderrpath(stderrpath);
		taskType.setHtmloutpath(htmloutpath);
		taskType.setOutputsfolderpath(outputsfolderpath);
		taskType.setTaskfolderpath(taskfolderpath);

		return taskType;

	}

	/**
	 * 保存模块xml参数信息
	 */
	public void saveToolXml() {
		try {
			ToolDocument toolDocument = ToolDocument.Factory.newInstance();
			toolDocument.setTool(paramEditor.saveAsToolType());
			toolDocument.save(new File(xmlinfopath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the xmlinfopath
	 */
	public String getXmlinfopath() {
		return xmlinfopath;
	}

	/**
	 * @param xmlinfopath
	 *            the xmlinfopath to set
	 */
	public void setXmlinfopath(String xmlinfopath) {
		this.xmlinfopath = xmlinfopath;
	}

	/**
	 * @return the taskname
	 */
	public String getTaskname() {
		return taskname;
	}

	/**
	 * @param taskname
	 *            the taskname to set
	 */
	public void setTaskname(String taskname) {
		this.taskname = taskname;
	}

	/**
	 * @return the taskstate
	 */
	public String getTaskstate() {
		return taskstate;
	}

	/**
	 * @param taskstate
	 *            the taskstate to set
	 */
	public void setTaskstate(String taskstate) {
		this.taskstate = taskstate;
	}

	/**
	 * @return the starttime
	 */
	public String getStarttime() {
		return starttime;
	}

	/**
	 * @param starttime
	 *            the starttime to set
	 */
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	/**
	 * @return the endtime
	 */
	public String getEndtime() {
		return endtime;
	}

	/**
	 * @param endtime
	 *            the endtime to set
	 */
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	/**
	 * @return the commandline
	 */
	public String getCommandline() {
		return commandline;
	}

	/**
	 * @param commandline
	 *            the commandline to set
	 */
	public void setCommandline(String commandline) {
		this.commandline = commandline;
	}

	/**
	 * @return the stderrpath
	 */
	public String getStderrpath() {
		return stderrpath;
	}

	/**
	 * @param stderrpath
	 *            the stderrpath to set
	 */
	public void setStderrpath(String stderrpath) {
		this.stderrpath = stderrpath;
	}

	/**
	 * @return the stdoutpath
	 */
	public String getStdoutpath() {
		return stdoutpath;
	}

	/**
	 * @param stdoutpath
	 *            the stdoutpath to set
	 */
	public void setStdoutpath(String stdoutpath) {
		this.stdoutpath = stdoutpath;
	}

	/**
	 * @return the htmloutpath
	 */
	public String getHtmloutpath() {
		return htmloutpath;
	}

	/**
	 * @param htmloutpath
	 *            the htmloutpath to set
	 */
	public void setHtmloutpath(String htmloutpath) {
		this.htmloutpath = htmloutpath;
	}

	/**
	 * @return the outputsfolderpath
	 */
	public String getOutputsfolderpath() {
		return outputsfolderpath;
	}

	/**
	 * @param outputsfolderpath
	 *            the outputsfolderpath to set
	 */
	public void setOutputsfolderpath(String outputsfolderpath) {
		this.outputsfolderpath = outputsfolderpath;
	}

	/**
	 * @return the taskfolderpath
	 */
	public String getTaskfolderpath() {
		return taskfolderpath;
	}

	/**
	 * @param taskfolderpath
	 *            the taskfolderpath to set
	 */
	public void setTaskfolderpath(String taskfolderpath) {
		this.taskfolderpath = taskfolderpath;
	}

	/**
	 * @return the workingDirPath
	 */
	public String getWorkingDirPath() {
		return workingDirPath;
	}

	/**
	 * @param workingDirPath
	 *            the workingDirPath to set
	 */
	public void setWorkingDirPath(String workingDirPath) {
		this.workingDirPath = workingDirPath;
	}

	/**
	 * @return the treeViewer
	 */
	public TableViewer getTableViewer() {
		return tableViewer;
	}

	public synchronized static void addJobsNum() {
		jobsNum++;
	}

	public synchronized static void minusJobsNum() {
		jobsNum--;
	}
}
