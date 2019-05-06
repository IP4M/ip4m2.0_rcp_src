package com.sixthhosp.gcmpa.views.tasktree.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.sixthhosp.gcmpa.tasks.XmlTask;

public class StopAction extends Action {

	private TableViewer tableViewer;

	public StopAction(TableViewer tableViewer) {
		this.tableViewer = tableViewer;
		this.setText("Stop");
		this.setToolTipText("Stop the selected running task");

		this.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_ELCL_STOP));
		this.setDisabledImageDescriptor(PlatformUI.getWorkbench()
				.getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_ELCL_STOP_DISABLED));
	}

	@Override
	public void run() {
		IStructuredSelection selection = (IStructuredSelection) tableViewer
				.getSelection();
		if (selection.size() != 1) {
			return;
		}

		Object object = selection.getFirstElement();
		if (object instanceof XmlTask) {
			XmlTask xmlTask = (XmlTask) object;
			String message = "Do you really want to stop the runnning task \""
					+ xmlTask.getTaskname() + "\" ?";
			boolean canDelete = MessageDialog.openQuestion(tableViewer
					.getTable().getShell(), "Confirm", message);
			if (canDelete && xmlTask.canStop()) {
				xmlTask.stop();
				tableViewer.refresh();
			}
		}

	}

}
