package com.sixthhosp.gcmpa.views.tasktree.actions;

import java.io.File;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.sixthhosp.gcmpa.tasks.ITask;
import com.sixthhosp.gcmpa.tasks.XmlTask;

public class OpenResultFolderAction extends Action implements
		IDoubleClickListener {

	private TableViewer tableViewer;

	public OpenResultFolderAction(TableViewer tableViewer) {
		this.tableViewer = tableViewer;
		setText("Open Result Folder in OS Explorer");
		this.setToolTipText("Open the selected task result folder");

		this.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_OBJ_FOLDER));
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
			if (xmlTask.isAllFinished()
					&& xmlTask.getTaskstate().equals(ITask.FINISHED)) {
				// 确认输出文件是否存在
				if (new File(xmlTask.getOutputsfolderpath()).exists()) {
					try {
						OpenFileUtils.opneFile(new File(xmlTask
								.getOutputsfolderpath()));
					} catch (Exception e) {
						MessageDialog.openError(tableViewer.getTable()
								.getShell(), "Error", e.getMessage());
					}
				} else {
					MessageDialog.openError(tableViewer.getTable().getShell(),
							"Error in task: " + xmlTask.getTaskname(),
							"The output folder is not exist!");
					return;
				}
			}
		}
	}

	@Override
	public void doubleClick(DoubleClickEvent event) {
		run();
	}
}
