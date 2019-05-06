package com.sixthhosp.gcmpa.views.tasktree.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.sixthhosp.gcmpa.data.DataFactory;
import com.sixthhosp.gcmpa.tasks.XmlTask;

public class DeleteAction extends Action {

	private TableViewer tableViewer;

	public DeleteAction(TableViewer tableViewer) {
		this.tableViewer = tableViewer;
		this.setText("Delete");
		this.setToolTipText("Delete the selected task");

		this.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_ELCL_REMOVE));
		this.setDisabledImageDescriptor(PlatformUI.getWorkbench()
				.getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_ELCL_REMOVE_DISABLED));
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
			if (xmlTask.isAllFinished()) {
				String message = "Do you really want to delete task \""
						+ xmlTask.getTaskname() + "\" ?";
				boolean canDelete = MessageDialog.openQuestion(tableViewer
						.getTable().getShell(), "Confirm", message);
				if (canDelete) {
					DataFactory.getTaskBox().delete(xmlTask);
					tableViewer.refresh();
					DataFactory.saveTaskBox();
				}
			} else {
				return;
			}
		}
	}

}
