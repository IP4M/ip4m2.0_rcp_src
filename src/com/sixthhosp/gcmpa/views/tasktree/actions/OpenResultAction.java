package com.sixthhosp.gcmpa.views.tasktree.actions;

import java.io.File;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.sixthhosp.gcmpa.editors.HtmlEditor;
import com.sixthhosp.gcmpa.editors.LogEditor;
import com.sixthhosp.gcmpa.editors.ei.HtmlEditorInput;
import com.sixthhosp.gcmpa.editors.ei.LogEditorInput;
import com.sixthhosp.gcmpa.tasks.ITask;
import com.sixthhosp.gcmpa.tasks.XmlTask;
import com.sixthhosp.gcmpa.views.filetree.FileTreeViewer;

public class OpenResultAction extends Action implements IDoubleClickListener {

	private TableViewer tableViewer;
	private TreeViewer treeViewer;

	public OpenResultAction(TableViewer tableViewer,
			FileTreeViewer fileTreeViewer) {
		this.treeViewer = fileTreeViewer;
		this.tableViewer = tableViewer;
		setText("Open Result");
		this.setToolTipText("Open the selected task result");

		this.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_OBJ_FILE));
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
				if (new File(xmlTask.getHtmloutpath()).exists()) {
					HtmlEditorInput htmlEditorInput = new HtmlEditorInput(
							xmlTask);
					IWorkbenchPage workbenchPage = PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getActivePage();
					try {
						workbenchPage
								.openEditor(htmlEditorInput, HtmlEditor.ID);
						treeViewer.setInput(new File(xmlTask.getHtmloutpath())
								.getParentFile());
					} catch (PartInitException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				} else {
					treeViewer.setInput(null);
					MessageDialog.openError(tableViewer.getTable().getShell(),
							"Error in task: " + xmlTask.getTaskname(),
							"The output file is not exist!");
					return;
				}
			} else if (xmlTask.isAllFinished()
					&& (xmlTask.getTaskstate().equals(ITask.FAILED) || xmlTask
							.getTaskstate().equals(ITask.TERMINATED))) {
				try {
					LogEditorInput logEditorInput = new LogEditorInput(xmlTask);
					IWorkbenchPage workbenchPage = PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getActivePage();

					workbenchPage.openEditor(logEditorInput, LogEditor.ID);
				} catch (PartInitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void doubleClick(DoubleClickEvent event) {
		run();
	}
}
