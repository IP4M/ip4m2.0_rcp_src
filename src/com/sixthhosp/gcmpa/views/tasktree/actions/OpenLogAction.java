package com.sixthhosp.gcmpa.views.tasktree.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.sixthhosp.gcmpa.editors.LogEditor;
import com.sixthhosp.gcmpa.editors.ei.LogEditorInput;
import com.sixthhosp.gcmpa.tasks.XmlTask;

public class OpenLogAction extends Action {

	private TableViewer tableViewer;

	public OpenLogAction(TableViewer tableViewer) {
		this.tableViewer = tableViewer;
		this.setText("View Log");
		this.setToolTipText("View the selected task's log");

		this.setImageDescriptor(ImageDescriptor.createFromFile(
				OpenLogAction.class, "icons/logs.gif"));
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
				try {
					LogEditorInput logEditorInput = new LogEditorInput(xmlTask);
					IWorkbenchPage workbenchPage = PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getActivePage();

					workbenchPage.openEditor(logEditorInput, LogEditor.ID);
				} catch (PartInitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				return;
			}
		}
	}
}
