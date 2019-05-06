package com.sixthhosp.gcmpa.views.filetree;

import java.io.File;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;

import com.sixthhosp.gcmpa.views.filetree.provider.FileContentProvider;
import com.sixthhosp.gcmpa.views.filetree.provider.FileLabelProvider;

/**
 * 文件树，用于显示分析结果目录下的文件。
 * 
 * @author zhengzequn
 * 
 */
public class FileTreeViewer extends TreeViewer {

	public FileTreeViewer(Composite parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
		// 设置内容
		this.setContentProvider(new FileContentProvider());

		// 设置标签
		this.setLabelProvider(new FileLabelProvider());

		// 设置排序，文件夹在前，文件在后
		this.setSorter(new ViewerSorter() {
			public int category(Object element) {
				File file = (File) element;
				if (file.isDirectory())
					return 0;
				else
					return 1;
			}
		});

		// 默认展开一个等级
		this.expandToLevel(1);
	}

	/*
	 * 打开文件夹操作，该操作会将文件夹展开。
	 */
	public void openFolder(File folder) {
		this.setExpandedState(folder, true);
		// this.setSelection(new StructuredSelection(folder), true);
	}

	public void showMessage(String message) {
		MessageDialog.openInformation(this.getControl().getShell(), "Files",
				message);
	}

	public void showError(String message) {
		MessageDialog.openError(this.getControl().getShell(), "Files", message);
	}

	public void showWarning(String message) {
		MessageDialog.openWarning(this.getControl().getShell(), "Files",
				message);
	}

	public void selectFirtstElement() {
		// TODO Auto-generated method stub
		TreeItem item = this.getTree().getItem(0);
		this.getTree().setSelection(item);
	}
}
