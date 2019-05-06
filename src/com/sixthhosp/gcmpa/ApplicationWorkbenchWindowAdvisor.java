package com.sixthhosp.gcmpa;

import java.awt.Dimension;

import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import com.sixthhosp.gcmpa.configs.ConfigFile;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	public ApplicationWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	public ActionBarAdvisor createActionBarAdvisor(
			IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setShowCoolBar(false);
		configurer.setShowStatusLine(true);
		Dimension dim = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		configurer.setInitialSize(new Point((int) dim.getWidth(), (int) dim
				.getHeight()));

		initialize();
	}

	@Override
	public void postWindowOpen() {
		// TODO Auto-generated method stub
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.getWindow().getShell().setMaximized(true);
	}

	@Override
	public boolean preWindowShellClose() {
		if (PreWindowCloseWorks.questionCloseRunningTask()) {
			PreWindowCloseWorks.saveData();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * ≥ı ºªØ
	 */
	private void initialize() {
		ConfigFile.initial();

	}
}
