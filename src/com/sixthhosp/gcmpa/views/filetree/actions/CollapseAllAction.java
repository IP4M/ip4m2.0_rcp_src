package com.sixthhosp.gcmpa.views.filetree.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.sixthhosp.gcmpa.views.filetree.FileTreeViewer;


/**
 * 该操作将会收起文件浏览器的的所有节点
 * 
 * @author zhengzequn
 * 
 */
public class CollapseAllAction extends Action {
	
	private FileTreeViewer fileTreeViewer;

	public CollapseAllAction(FileTreeViewer viewer) {
		// TODO Auto-generated constructor stub
		fileTreeViewer = viewer;
		this.setText("Collapse All");
		this.setToolTipText("Collapse All");
		
		this.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_ELCL_COLLAPSEALL));
		this.setDisabledImageDescriptor(PlatformUI
				.getWorkbench()
				.getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_ELCL_COLLAPSEALL_DISABLED));
	}

	public void run() {
		fileTreeViewer.collapseAll();
		fileTreeViewer.setSelection(null, false);
	}
}
