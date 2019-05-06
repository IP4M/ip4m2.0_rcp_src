package com.sixthhosp.gcmpa.views.filetree.actions;

import java.io.File;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.program.Program;

import com.sixthhosp.gcmpa.views.filetree.FileTreeViewer;


/**
 * 文件浏览器的打开（open）操作
 * 
 * @author zhengzequn
 * 
 */
public class OpenAction extends Action implements IDoubleClickListener {

	private FileTreeViewer fileTreeViewer;

	public OpenAction(FileTreeViewer viewer) {
		// TODO Auto-generated constructor stub
		fileTreeViewer = viewer;
		this.setText("Open");
		this.setToolTipText("Open the file");
	}

	public void run() {
		IStructuredSelection selection = (IStructuredSelection) fileTreeViewer
				.getSelection();
		if (selection.size() != 1) {
			return;
		}
		File file = (File) selection.getFirstElement();
		openFile(file);
	}

	@Override
	public void doubleClick(DoubleClickEvent event) {
		IStructuredSelection selection = (IStructuredSelection) event
				.getSelection();
		File file = (File) selection.getFirstElement();
		openFile(file);

	}

	private void openFile(File file) {
		if (file.exists()) {
			if (file.isFile()) {
				boolean isOpenSuccessed = Program
						.launch(file.getAbsolutePath());
				if (!isOpenSuccessed) {
					fileTreeViewer.showError("Cann't open the file \""
							+ file.getName() + "\"");
				}
			} else if (file.isDirectory()) {
				fileTreeViewer.openFolder(file);
			}
		} else {
			fileTreeViewer.showError("File or directory \"" + file.getName()
					+ "\" has been deleted.");
		}
	}

}
