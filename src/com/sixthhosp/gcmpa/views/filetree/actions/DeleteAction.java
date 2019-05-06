package com.sixthhosp.gcmpa.views.filetree.actions;

import java.io.*;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.*;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.sixthhosp.gcmpa.views.filetree.FileTreeViewer;


/**
 * 用于执行文件的删除操作，点击删除之后在后台新建一个线程，循环删除选中的文件
 * 
 * @author zhengzequn
 * 
 */
public class DeleteAction extends Action {

	public static final String ID = "eubiosoft.filetree.actions.DeleteAction";

	private FileTreeViewer fileTreeViewer;
	private IStatusLineManager statusline;

	public DeleteAction(FileTreeViewer viewer, IStatusLineManager statusline) {
		// TODO Auto-generated constructor stub
		fileTreeViewer = viewer;
		this.statusline = statusline;

		this.setId(ID);
		this.setActionDefinitionId("org.eclipse.ui.edit.delete");

		this.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
		this.setDisabledImageDescriptor(PlatformUI.getWorkbench()
				.getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE_DISABLED));

		this.setText("Delete");
		this.setToolTipText("Delete Files or Directories");
	}

	public void run() {
		final IStructuredSelection selection = (IStructuredSelection) fileTreeViewer
				.getSelection();
		if (selection.isEmpty()) {
			return;
		}

		File selected_file = (File) selection.getFirstElement();
		String message;
		if (selection.size() == 1) {
			// 选择一个file
			message = "Do you really want to delete \""
					+ selected_file.getName() + "\" ?";
		} else {
			// 选择多个files
			message = "Do you really want to delete these " + selection.size()
					+ " files or directoies?";
		}
		boolean canDelete = MessageDialog.openQuestion(fileTreeViewer.getTree()
				.getShell(), "Files", message);

		if (canDelete) {
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					for (@SuppressWarnings("rawtypes")
					Iterator i = selection.iterator(); i.hasNext();) {
						final File file = (File) i.next();
						if (file.exists()) {

							fileTreeViewer.getTree().getDisplay()
									.syncExec(new Runnable() {
										@Override
										public void run() {
											String message = "Delete file or folder: \""
													+ file.getName() + "\" ...";
											statusline.setMessage(message);
										}
									});
							boolean isDeleteSuccessed = FileUtils
									.deleteQuietly(file);
							if (!isDeleteSuccessed) {
								fileTreeViewer.getTree().getDisplay()
										.syncExec(new Runnable() {
											@Override
											public void run() {
												fileTreeViewer.showError("Delete file or directory \""
														+ file.getName()
														+ "\" failed.");
											}
										});

							}

						}
						fileTreeViewer.getTree().getDisplay()
								.asyncExec(new Runnable() {
									@Override
									public void run() {
										DeleteAction.this.Refresh(file);
									}
								});
					}
					fileTreeViewer.getTree().getDisplay()
					.syncExec(new Runnable() {
						@Override
						public void run() {
							String message = "Delete file(s) Finished...";
							statusline.setMessage(message);
						}
					});
				}
			};
			new Thread(runnable).start();
		} else {
			return;
		}
	}

	private void Refresh(File file) {

		File parentFile = file.getParentFile();
		if (parentFile.exists()) {
			fileTreeViewer.refresh(parentFile);
		} else {
			Refresh(parentFile);
		}
	}
}