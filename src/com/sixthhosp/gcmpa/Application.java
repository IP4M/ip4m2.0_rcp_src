package com.sixthhosp.gcmpa;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import com.sixthhosp.gcmpa.filelock.SoftFileLock;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.
	 * IApplicationContext)
	 */
	public Object start(IApplicationContext context) {

		// 防止一个用户启动多个程序
		if (!SoftFileLock.isLocked()) {
			// instance already running.
			MessageDialog
					.openInformation(new Shell(), "Information",
							"Another instance of this application is already running. Exiting!");
			System.exit(0);
		}

		// 确保win64系统
		if (!Utils.isWin64bit()) {
			MessageDialog.openInformation(new Shell(), "Information",
					"Software only works on Windows 64bit System. Exiting!");
			System.exit(0);
		}

		// 确保程序具有管理员权限
		if (!Utils.isAdmin()) {
			MessageDialog.openInformation(new Shell(), "Information",
					"Administrator privileges are required. Exiting!");
			System.exit(0);
		}

		// 确保用户名不包含非法字符
		if (!Utils.isUserNameOK()) {
			MessageDialog
					.openInformation(
							new Shell(),
							"Information",
							"Windwos OS user name contains illegal characters(not ascii printable). Exiting!");
			System.exit(0);
		}

		// 确保软件根目录不包含非法字符
		if (!Utils.isSoftPathOK()) {
			MessageDialog
					.openInformation(
							new Shell(),
							"Information",
							"Absolute path of software installation directory "
									+ "contains illegal characters(not ascii printable). Exiting!");
			System.exit(0);
		}

		Display display = PlatformUI.createDisplay();
		try {
			int returnCode = PlatformUI.createAndRunWorkbench(display,
					new ApplicationWorkbenchAdvisor());
			if (returnCode == PlatformUI.RETURN_RESTART) {
				return IApplication.EXIT_RESTART;
			}
			return IApplication.EXIT_OK;
		} finally {
			display.dispose();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	public void stop() {
		if (!PlatformUI.isWorkbenchRunning())
			return;
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final Display display = workbench.getDisplay();
		display.syncExec(new Runnable() {
			public void run() {
				if (!display.isDisposed())
					workbench.close();
			}
		});
	}
}
