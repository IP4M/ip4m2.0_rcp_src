package com.sixthhosp.gcmpa.views;

import java.io.File;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;

import com.sixthhosp.gcmpa.editors.HtmlEditor;
import com.sixthhosp.gcmpa.editors.ei.HtmlEditorInput;
import com.sixthhosp.gcmpa.views.filetree.FileTreeViewer;
import com.sixthhosp.gcmpa.views.filetree.actions.CollapseAllAction;
import com.sixthhosp.gcmpa.views.filetree.actions.CopyAction;
import com.sixthhosp.gcmpa.views.filetree.actions.DeleteAction;
import com.sixthhosp.gcmpa.views.filetree.actions.OpenAction;
import com.sixthhosp.gcmpa.views.filetree.actions.OpenResultFolderAction;
import com.sixthhosp.gcmpa.views.filetree.actions.RefreshAction;

public class FilesView extends ViewPart {

	public static final String ID = "com.sixthhosp.gcmpa.views.filesview";

	private FileTreeViewer fileTreeViewer;

	private OpenAction openAction;
	private RefreshAction refreshAction;
	private CollapseAllAction collapseAllAction;
	private DeleteAction deleteAction;
	private CopyAction copyAction;
	private OpenResultFolderAction openResultFolderAction;

	@Override
	public void createPartControl(Composite parent) {
		fileTreeViewer = new FileTreeViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL);
		fileTreeViewer.setInput(null);

		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();

		IWorkbenchPage page = getSite().getPage();

		IPartListener2 listener = new IPartListener2() {
			@Override
			public void partVisible(IWorkbenchPartReference partRef) {
			}

			@Override
			public void partOpened(IWorkbenchPartReference partRef) {
			}

			@Override
			public void partInputChanged(IWorkbenchPartReference partRef) {
			}

			@Override
			public void partHidden(IWorkbenchPartReference partRef) {
			}

			@Override
			public void partDeactivated(IWorkbenchPartReference partRef) {
			}

			@Override
			public void partClosed(IWorkbenchPartReference partRef) {
			}

			@Override
			public void partBroughtToTop(IWorkbenchPartReference partRef) {
				IWorkbenchPart part = partRef.getPart(true);
				if (part instanceof HtmlEditor) {
					HtmlEditor editor = (HtmlEditor) part;
					HtmlEditorInput editorInput = (HtmlEditorInput) editor
							.getEditorInput();
					fileTreeViewer.setInput(new File(editorInput.getXmlTask()
							.getOutputsfolderpath()));
				}
			}

			@Override
			public void partActivated(IWorkbenchPartReference partRef) {
				// System.out.println("ID:" + partRef.getId() + " Title:" +
				// partRef.getTitle());
			}
		};
		page.addPartListener(listener);
	}

	private void makeActions() {
		IStatusLineManager statusline = this.getViewSite().getActionBars()
				.getStatusLineManager();

		openAction = new OpenAction(fileTreeViewer);
		refreshAction = new RefreshAction(fileTreeViewer);
		collapseAllAction = new CollapseAllAction(fileTreeViewer);
		deleteAction = new DeleteAction(fileTreeViewer, statusline);

		copyAction = new CopyAction(fileTreeViewer, statusline);
		openResultFolderAction = new OpenResultFolderAction(fileTreeViewer);

		IHandlerService handlerService = (IHandlerService) getSite()
				.getService(IHandlerService.class);
		handlerService.activateHandler("org.eclipse.ui.edit.copy",
				new ActionHandler(copyAction));
		handlerService.activateHandler("org.eclipse.ui.edit.delete",
				new ActionHandler(deleteAction));
		handlerService.activateHandler("org.eclipse.ui.file.refresh",
				new ActionHandler(refreshAction));
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		fillContextMenu(menuMgr);

		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				IStructuredSelection selection = (IStructuredSelection) fileTreeViewer
						.getSelection();

				if (selection.size() == 1) {
					openAction.setEnabled(true);
				} else {
					openAction.setEnabled(false);
				}

				if (!selection.isEmpty()) {
					copyAction.setEnabled(true);
					deleteAction.setEnabled(true);
				} else {
					copyAction.setEnabled(false);
					deleteAction.setEnabled(false);
				}
			}
		});

		Menu menu = menuMgr.createContextMenu(fileTreeViewer.getControl());
		fileTreeViewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, fileTreeViewer);
	}

	private void fillContextMenu(IMenuManager manager) {
		// 将操作添加到右键菜单
		manager.add(openAction);

		manager.add(new Separator());
		manager.add(copyAction);
		manager.add(deleteAction);

		manager.add(new Separator());
		manager.add(openResultFolderAction);

		manager.add(new Separator());
		manager.add(collapseAllAction);
		manager.add(refreshAction);

		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void hookDoubleClickAction() {
		fileTreeViewer.addDoubleClickListener(openAction);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(openResultFolderAction);
		manager.add(new Separator());
		manager.add(refreshAction);
	}

	@Override
	public void setFocus() {
		fileTreeViewer.getControl().setFocus();
	}

	public FileTreeViewer getFileTreeViewer() {
		return fileTreeViewer;
	}

}
