package com.sixthhosp.gcmpa.views.tooltree.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * È«²¿ÕÛµþ²Ù×÷
 * 
 * @author zhengzequn
 * 
 */
public class CollapseAllAction extends Action {

	private TreeViewer treeViewer;

	public CollapseAllAction(TreeViewer treeViewer) {
		// TODO Auto-generated constructor stub
		this.treeViewer = treeViewer;
		this.setText("Collapse All");
		this.setToolTipText("Collapse All");

		this.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_ELCL_COLLAPSEALL));
		this.setDisabledImageDescriptor(PlatformUI
				.getWorkbench()
				.getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_ELCL_COLLAPSEALL_DISABLED));
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		treeViewer.collapseAll();
	}
}
