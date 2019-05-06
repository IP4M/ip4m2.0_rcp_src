package com.sixthhosp.gcmpa.views.tasktree.actions;

import java.io.File;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TableViewer;
import com.sixthhosp.gcmpa.configs.ConfigFile;

public class OpenRootFolderAction extends Action {

	private TableViewer tableViewer;

	public OpenRootFolderAction(TableViewer tableViewer) {
		this.tableViewer = tableViewer;
		this.setText("Open Root Folder in OS Explorer");
		this.setToolTipText("Open the root folder for placing the task results");

		this.setImageDescriptor(ImageDescriptor.createFromFile(
				StopAllAction.class, "icons/compressed_folder_obj.gif"));
	}

	@Override
	public void run() {
		try {
			OpenFileUtils.opneFile(new File(ConfigFile
					.getTaskResultsFolderPath()));
		} catch (Exception e) {
			MessageDialog.openError(tableViewer.getTable().getShell(), "Error",
					"The task root folder is not exist!");
			return;
		}

	}

}
