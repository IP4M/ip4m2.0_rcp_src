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
 * �ļ�����������ʾ�������Ŀ¼�µ��ļ���
 * 
 * @author zhengzequn
 * 
 */
public class FileTreeViewer extends TreeViewer {

	public FileTreeViewer(Composite parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
		// ��������
		this.setContentProvider(new FileContentProvider());

		// ���ñ�ǩ
		this.setLabelProvider(new FileLabelProvider());

		// ���������ļ�����ǰ���ļ��ں�
		this.setSorter(new ViewerSorter() {
			public int category(Object element) {
				File file = (File) element;
				if (file.isDirectory())
					return 0;
				else
					return 1;
			}
		});

		// Ĭ��չ��һ���ȼ�
		this.expandToLevel(1);
	}

	/*
	 * ���ļ��в������ò����Ὣ�ļ���չ����
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
