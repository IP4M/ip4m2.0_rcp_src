package com.sixthhosp.gcmpa.views.filetree.actions;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.sixthhosp.gcmpa.views.filetree.FileTreeViewer;
import com.sixthhosp.gcmpa.views.filetree.provider.FileLabelProvider;


/**
 * ¸´ÖÆ²Ù×÷
 * 
 * @author zhengzequn
 * 
 */
public class CopyAction extends Action {

	public static final String ID = "eubiosoft.filetree.actions.CopyAction";

	private FileTreeViewer fileTreeViewer;
	private Clipboard clipboard;
	private IStatusLineManager statusline;

	public CopyAction(FileTreeViewer viewer, IStatusLineManager statusline) {
		fileTreeViewer = viewer;
		clipboard = FileLabelProvider.getClipboard();
		this.statusline = statusline;

		this.setId(ID);
		this.setActionDefinitionId("org.eclipse.ui.edit.copy");

		this.setText("Copy");
		this.setToolTipText("Copy");

		this.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
		this.setDisabledImageDescriptor(PlatformUI.getWorkbench()
				.getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_TOOL_COPY_DISABLED));
	}

	public void run() {
		IStructuredSelection selection = (IStructuredSelection) fileTreeViewer
				.getSelection();
		if (selection.isEmpty()) {
			return;
		}

		FileTransfer fileTransfer = FileTransfer.getInstance();

		@SuppressWarnings("rawtypes")
		Iterator iterator = selection.iterator();
		ArrayList<String> fileList = new ArrayList<String>();
		while (iterator.hasNext()) {
			File file = (File) iterator.next();
			fileList.add(file.getAbsolutePath());
		}

		String[] strings = fileList.toArray(new String[fileList.size()]);

		clipboard.setContents(new Object[] { strings },
				new Transfer[] { fileTransfer });
		statusline.setMessage("Copy " + selection.size() + " files.");
	}
}
