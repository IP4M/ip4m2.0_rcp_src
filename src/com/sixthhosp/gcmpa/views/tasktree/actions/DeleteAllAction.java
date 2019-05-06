package com.sixthhosp.gcmpa.views.tasktree.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.sixthhosp.gcmpa.data.DataFactory;

public class DeleteAllAction extends Action {

	private TableViewer tableViewer;

	public DeleteAllAction(TableViewer tableViewer) {
		this.tableViewer = tableViewer;
		this.setText("Delete All");
		this.setToolTipText("Delete all the tasks, Doesn't affect the running tasks");

		this.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_ELCL_REMOVEALL));
		this.setDisabledImageDescriptor(PlatformUI.getWorkbench()
				.getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_ELCL_REMOVEALL_DISABLED));
	}

	@Override
	public void run() {
		String message = "Do you really want to delete all the tasks? Doesn't affect the running tasks!";
		boolean b = MessageDialog.openQuestion(tableViewer.getTable()
				.getShell(), "Confirm", message);
		if (b) {
			DataFactory.getTaskBox().deleteAllFiTasks();
			DataFactory.saveTaskBox();
			tableViewer.refresh();
		}
	}

}
