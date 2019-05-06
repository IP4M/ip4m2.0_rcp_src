package com.sixthhosp.gcmpa.views.tasktree.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TableViewer;

import com.sixthhosp.gcmpa.data.DataFactory;

public class StopAllAction extends Action {

	private TableViewer tableViewer;

	public StopAllAction(TableViewer tableViewer) {
		this.tableViewer = tableViewer;
		this.setText("Stop All");
		this.setToolTipText("Stop all the still running tasks");

		this.setImageDescriptor(ImageDescriptor.createFromFile(
				StopAllAction.class, "icons/terminate_all_co(1).gif"));
	}

	@Override
	public void run() {
		String message = "Do you really want to stop all the running tasks?";
		boolean b = MessageDialog.openQuestion(tableViewer.getTable()
				.getShell(), "Confirm", message);
		if (b) {
			DataFactory.getTaskBox().stopAllNotFinishedTasks();
			tableViewer.refresh();
		}
	}

}
