package com.sixthhosp.gcmpa.views.tooltree.actions;

import java.io.File;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.sixthhosp.gcmpa.editors.HelpEditor;
import com.sixthhosp.gcmpa.editors.ei.HelpEditorInput;
import com.sixthhosp.gcmpa.tools.Tool;
import com.sixthhosp.gcmpa.tools.ToolSection;
import com.sixthhosp.gcmpa.tools.ToolSectionLabel;

public class OpenHelpAction extends Action {

	public static final String ID = "eubiosoft.tooltree.actions.OpenHelpAction";

	private TreeViewer treeViewer;

	public OpenHelpAction(TreeViewer treeViewer) {
		// TODO Auto-generated constructor stub
		this.treeViewer = treeViewer;
		this.setText("Open Help");
		this.setToolTipText("Open the selected tool help document");

		this.setImageDescriptor(ImageDescriptor.createFromFile(
				OpenHelpAction.class, "icons/help_contents(1).gif"));
		this.setDisabledImageDescriptor(ImageDescriptor.createFromFile(
				OpenHelpAction.class, "icons/help_contents.gif"));

		this.setId(ID);
		this.setActionDefinitionId("com.sixthhosp.gcmpa.toolhelp");
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		IStructuredSelection selection = (IStructuredSelection) treeViewer
				.getSelection();
		if (selection.size() != 1) {
			return;
		}

		Object object = selection.getFirstElement();

		if (object instanceof ToolSectionLabel) {
			return;
		} else if (object instanceof ToolSection) {
			return;
		} else if (object instanceof Tool) {
			Tool tool = (Tool) object;
			String helpPath = tool.getHelpPath();
			if (StringUtils.isNotBlank(helpPath) && new File(helpPath).exists()) {
				HelpEditorInput helpEditorInput = new HelpEditorInput(tool);
				IWorkbenchPage workbenchPage = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage();
				IEditorPart editor = workbenchPage.findEditor(helpEditorInput);

				if (editor != null) {
					workbenchPage.bringToTop(editor);
				} else {
					try {
						workbenchPage
								.openEditor(helpEditorInput, HelpEditor.ID);
					} catch (PartInitException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else {
				Shell shell = treeViewer.getTree().getShell();
				MessageDialog.openError(shell, tool.getName(),
						"No Tool Help Document");
			}

		}
	}
}
