package com.sixthhosp.gcmpa.views.tooltree.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.sixthhosp.gcmpa.editors.ParamEditor;
import com.sixthhosp.gcmpa.editors.ei.ParamEditorInput;
import com.sixthhosp.gcmpa.tools.Tool;
import com.sixthhosp.gcmpa.tools.ToolSection;
import com.sixthhosp.gcmpa.tools.ToolSectionLabel;
import com.sixthhosp.gcmpa.tools.Utils;

/**
 * 运行模块操作
 * 
 * @author zhengzequn
 * 
 */
public class OpenToolAction extends Action implements IDoubleClickListener {

	private TreeViewer toolTreeViewer;

	public OpenToolAction(TreeViewer toolTreeViewer) {
		// TODO Auto-generated constructor stub
		this.toolTreeViewer = toolTreeViewer;

		this.setText("Open Tool");
		this.setToolTipText("Open the selected tool");

		this.setImageDescriptor(ImageDescriptor.createFromFile(
				OpenToolAction.class, "icons/run_tool(1).gif"));
		this.setDisabledImageDescriptor(ImageDescriptor.createFromFile(
				OpenToolAction.class, "icons/run_tool.gif"));
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		IStructuredSelection selection = (IStructuredSelection) toolTreeViewer
				.getSelection();
		if (selection.size() != 1) {
			return;
		}

		Object object = selection.getFirstElement();

		if (object instanceof ToolSectionLabel) {
			return;
		} else if (object instanceof ToolSection) {
			toolTreeViewer.setExpandedState(object,
					!toolTreeViewer.getExpandedState(object));
			return;
		} else if (object instanceof Tool) {
			Tool tool = (Tool) object;

			// 验证模块XML是否正确
			try {
				Utils.validateToolXML(tool.getFilePath());
			} catch (Exception e) {
				MessageDialog.openError(toolTreeViewer.getTree().getShell(),
						"Error in tool: " + tool.getName(), e.getMessage());
				return;
			}

			ParamEditorInput paramEditorInput = new ParamEditorInput(tool);
			IWorkbenchPage workbenchPage = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage();
			try {
				workbenchPage.openEditor(paramEditorInput, ParamEditor.ID);
			} catch (PartInitException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	}

	@Override
	public void doubleClick(DoubleClickEvent event) {
		// TODO Auto-generated method stub
		run();
	}
}
