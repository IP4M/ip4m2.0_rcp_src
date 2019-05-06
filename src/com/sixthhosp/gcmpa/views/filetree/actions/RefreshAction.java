package com.sixthhosp.gcmpa.views.filetree.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import com.sixthhosp.gcmpa.views.filetree.FileTreeViewer;


/**
 * 文件浏览器的刷新操作
 * 
 * @author zhengzequn
 * 
 */
public class RefreshAction extends Action {

	public static final String ID = "eubiosoft.filetree.actions.RefreshAction";

	private FileTreeViewer fileTreeViewer;

	public RefreshAction(FileTreeViewer viewer) {
		fileTreeViewer = viewer;
		this.setText("Refresh");
		this.setToolTipText("Refresh the Files Browser");

		this.setId(ID);
		this.setActionDefinitionId("org.eclipse.ui.file.refresh");

		this.setImageDescriptor(ImageDescriptor.createFromFile(
				RefreshAction.class, "icons/refresh_nav(3).gif"));
		this.setDisabledImageDescriptor(ImageDescriptor.createFromFile(
				RefreshAction.class, "icons/refresh_nav(2).gif"));
	}

	public void run() {
		fileTreeViewer.getTree().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				fileTreeViewer.refresh();
				fileTreeViewer.setSelection(null, false);
			}
		});
	}
}