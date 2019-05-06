package com.sixthhosp.gcmpa.views.tasktree.actions;

import java.io.File;
import java.io.IOException;

import org.apache.xmlbeans.XmlException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.sixthhosp.gcmpa.editors.ParamEditor;
import com.sixthhosp.gcmpa.editors.ei.ParamEditorInput;
import com.sixthhosp.gcmpa.tasks.XmlTask;
import com.sixthhosp.gcmpa.tools.Tool;
import com.sixthhosp.gcmpa.xmlbeans.tool.ToolDocument;
import com.sixthhosp.gcmpa.xmlbeans.tool.ToolType;

public class ReRunAction extends Action {

	private TableViewer tableViewer;

	public ReRunAction(TableViewer tableViewer) {
		this.tableViewer = tableViewer;
		setText("ReRun");
		this.setToolTipText("ReRun the selected task");

		this.setImageDescriptor(ImageDescriptor.createFromFile(
				ReRunAction.class, "icons/Rotate360AntiClockwise2Red.png"));
	}

	@Override
	public void run() {
		IStructuredSelection selection = (IStructuredSelection) tableViewer
				.getSelection();
		if (selection.size() != 1) {
			return;
		}

		Object object = selection.getFirstElement();
		if (object instanceof XmlTask) {
			try {

				XmlTask xmlTask = (XmlTask) object;
				String xmlFilePath = xmlTask.getXmlinfopath();
				File xmlFile = new File(xmlFilePath);
				if (xmlTask.isAllFinished()) {
					if (xmlFile.exists()) {
						ToolDocument toolDocument = ToolDocument.Factory
								.parse(xmlFile);
						ToolType toolType = toolDocument.getTool();
						Tool tool = new Tool(toolType, xmlFilePath);

						ParamEditorInput paramEditorInput = new ParamEditorInput(
								tool);
						IWorkbenchPage workbenchPage = PlatformUI
								.getWorkbench().getActiveWorkbenchWindow()
								.getActivePage();
						workbenchPage.openEditor(paramEditorInput,
								ParamEditor.ID);
					} else {
						MessageDialog.openError(tableViewer.getTable()
								.getShell(),
								"Error in task: " + xmlTask.getTaskname(),
								"The param config file is not exist!");
						return;
					}
				}
			} catch (XmlException | IOException | PartInitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
