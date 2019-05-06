package com.sixthhosp.gcmpa.views.filetree.actions;

import java.io.File;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.sixthhosp.gcmpa.views.filetree.FileTreeViewer;
import com.sixthhosp.gcmpa.views.tasktree.actions.OpenFileUtils;

public class OpenResultFolderAction extends Action implements
		IDoubleClickListener {

	private TreeViewer treeViewer;

	public OpenResultFolderAction(FileTreeViewer fileTreeViewer) {
		this.treeViewer = fileTreeViewer;

		setText("Open folder in OS Explorer");
		this.setToolTipText("Open folder in OS Explorer");

		this.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_OBJ_FOLDER));
	}

	@Override
	public void run() {
		try {
			File file = (File) treeViewer.getInput();
			if (file != null) {
				OpenFileUtils.opneFile(file);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void doubleClick(DoubleClickEvent event) {
		run();
	}
}
