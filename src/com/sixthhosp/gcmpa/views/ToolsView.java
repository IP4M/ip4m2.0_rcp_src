package com.sixthhosp.gcmpa.views;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.dialogs.PatternFilter;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;

import com.sixthhosp.gcmpa.tools.Tool;
import com.sixthhosp.gcmpa.tools.ToolSectionLabel;
import com.sixthhosp.gcmpa.views.tooltree.ToolTreeViewer;
import com.sixthhosp.gcmpa.views.tooltree.actions.CollapseAllAction;
import com.sixthhosp.gcmpa.views.tooltree.actions.OpenHelpAction;
import com.sixthhosp.gcmpa.views.tooltree.actions.OpenToolAction;

/**
 * ¹¤¾ßÀ¸
 * 
 * @author zhengzequn
 *
 */
public class ToolsView extends ViewPart {

	public static final String ID = "com.sixthhosp.gcmpa.views.toolsview";

	private TreeViewer toolTreeViewer;

	private OpenHelpAction openHelpAction;
	private OpenToolAction openToolAction;
	private CollapseAllAction collapseAllAction;

	public void createPartControl(Composite parent) {
		PatternFilter patternFilter = new PatternFilter();
		ToolTreeViewer filteredTreeViewer = new ToolTreeViewer(parent,
				SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL, patternFilter, true);
		toolTreeViewer = filteredTreeViewer.getToolTreeViewer();

		makeActions();
		hookContextMenu();
		hookMouseAction();
		hookDoubleClickAction();
	}

	private void makeActions() {
		openHelpAction = new OpenHelpAction(toolTreeViewer);
		IHandlerService handlerService = (IHandlerService) getSite()
				.getService(IHandlerService.class);
		handlerService.activateHandler("com.sixthhosp.gcmpa.toolhelp",
				new ActionHandler(openHelpAction));

		openToolAction = new OpenToolAction(toolTreeViewer);
		collapseAllAction = new CollapseAllAction(toolTreeViewer);

	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.add(openToolAction);
		menuMgr.add(new Separator());
		menuMgr.add(openHelpAction);
		menuMgr.add(new Separator());
		menuMgr.add(collapseAllAction);

		// Other plug-ins can contribute there actions here
		menuMgr.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));

		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				IStructuredSelection selection = (IStructuredSelection) toolTreeViewer
						.getSelection();
				if (selection.size() != 1) {
					openToolAction.setEnabled(false);
					openHelpAction.setEnabled(false);
				} else {
					Object object = selection.getFirstElement();
					if (object instanceof Tool) {
						openToolAction.setEnabled(true);
						openHelpAction.setEnabled(true);
					} else {
						openToolAction.setEnabled(false);
						openHelpAction.setEnabled(false);
					}
				}
			}
		});

		Menu menu = menuMgr.createContextMenu(toolTreeViewer.getControl());
		toolTreeViewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, toolTreeViewer);
	}

	private void hookMouseAction() {
		toolTreeViewer.getTree().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {

				IStructuredSelection selection = (IStructuredSelection) toolTreeViewer
						.getSelection();
				Object object = selection.getFirstElement();
				if (object instanceof ToolSectionLabel) {
					toolTreeViewer.setSelection(null);
				}
			}
		});

	}

	private void hookDoubleClickAction() {
		toolTreeViewer.addDoubleClickListener(openToolAction);
	}

	public void setFocus() {
		toolTreeViewer.getTree().setFocus();
	}
}