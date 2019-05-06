package com.sixthhosp.gcmpa.views;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.ViewPart;

import com.sixthhosp.gcmpa.tasks.ITask;
import com.sixthhosp.gcmpa.tasks.XmlTask;
import com.sixthhosp.gcmpa.views.filetree.FileTreeViewer;
import com.sixthhosp.gcmpa.views.tasktree.TaskTableViewer;
import com.sixthhosp.gcmpa.views.tasktree.actions.DeleteAction;
import com.sixthhosp.gcmpa.views.tasktree.actions.DeleteAllAction;
import com.sixthhosp.gcmpa.views.tasktree.actions.OpenLogAction;
import com.sixthhosp.gcmpa.views.tasktree.actions.OpenResultAction;
import com.sixthhosp.gcmpa.views.tasktree.actions.OpenResultFolderAction;
import com.sixthhosp.gcmpa.views.tasktree.actions.OpenRootFolderAction;
import com.sixthhosp.gcmpa.views.tasktree.actions.ReRunAction;
import com.sixthhosp.gcmpa.views.tasktree.actions.StopAction;
import com.sixthhosp.gcmpa.views.tasktree.actions.StopAllAction;
import com.sixthhosp.gcmpa.views.tasktree.provider.TaskLableProvider;

public class TasksView extends ViewPart {

	public static final String ID = "com.sixthhosp.gcmpa.views.taskview";

	private TableViewer taskTableViewer;

	private OpenResultAction openResultAction;
	private ReRunAction reRunAction;
	private StopAction stopAction;
	private StopAllAction stopAllAction;
	private OpenLogAction openLogAction;
	private OpenResultFolderAction openResultFolderAction;
	private DeleteAction deleteAction;
	private DeleteAllAction deleteAllAction;
	private OpenRootFolderAction openRootFolderAction;

	public void createPartControl(Composite parent) {
		taskTableViewer = new TaskTableViewer(parent, SWT.SINGLE | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION);

		// 对于Running状态的任务，启动一个后台线程，每隔一秒刷新状态，使之产生动态效果
		IBaseLabelProvider labelProvider = taskTableViewer.getLabelProvider();
		TaskLableProvider taskLableProvider = (TaskLableProvider) labelProvider;
		taskLableProvider.startDynamicRunningStat();

		makeActions();
		hookContextMenu();
		hookMouseAction();
		hookDoubleClickAction();
	}

	private void makeActions() {
		IViewPart viewPart = getSite().getPage().findView(FilesView.ID);
		Assert.isNotNull(viewPart);
		FileTreeViewer fileTreeViewer = ((FilesView) viewPart)
				.getFileTreeViewer();

		openResultAction = new OpenResultAction(taskTableViewer, fileTreeViewer);
		reRunAction = new ReRunAction(taskTableViewer);
		stopAction = new StopAction(taskTableViewer);
		stopAllAction = new StopAllAction(taskTableViewer);
		openLogAction = new OpenLogAction(taskTableViewer);
		openResultFolderAction = new OpenResultFolderAction(taskTableViewer);
		deleteAction = new DeleteAction(taskTableViewer);
		deleteAllAction = new DeleteAllAction(taskTableViewer);
		openRootFolderAction = new OpenRootFolderAction(taskTableViewer);
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.add(openResultAction);
		menuMgr.add(openResultFolderAction);
		menuMgr.add(openRootFolderAction);
		menuMgr.add(openLogAction);
		menuMgr.add(new Separator());
		menuMgr.add(reRunAction);
		menuMgr.add(new Separator());
		menuMgr.add(stopAction);
		menuMgr.add(stopAllAction);
		menuMgr.add(new Separator());
		menuMgr.add(deleteAction);
		menuMgr.add(deleteAllAction);

		// Other plug-ins can contribute there actions here
		menuMgr.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));

		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				IStructuredSelection selection = (IStructuredSelection) taskTableViewer
						.getSelection();
				if (selection.size() != 1) {
					openResultAction.setEnabled(false);
					openResultFolderAction.setEnabled(false);
					reRunAction.setEnabled(false);
					stopAction.setEnabled(false);
					openLogAction.setEnabled(false);
					deleteAction.setEnabled(false);
				} else {
					Object object = selection.getFirstElement();
					XmlTask xmlTask = (XmlTask) object;
					if (xmlTask.isAllFinished()) {
						if (xmlTask.getTaskstate().equals(ITask.FINISHED)) {
							openResultAction.setEnabled(true);
							openResultFolderAction.setEnabled(true);
						} else {
							openResultAction.setEnabled(false);
							openResultFolderAction.setEnabled(false);
						}
						reRunAction.setEnabled(true);
						openLogAction.setEnabled(true);
						deleteAction.setEnabled(true);
					} else {
						openResultAction.setEnabled(false);
						openResultFolderAction.setEnabled(false);
						reRunAction.setEnabled(false);
						openLogAction.setEnabled(false);
						deleteAction.setEnabled(false);
					}

					if (xmlTask.canStop()) {
						stopAction.setEnabled(true);
					} else {
						stopAction.setEnabled(false);
					}
				}

			}
		});

		Menu menu = menuMgr.createContextMenu(taskTableViewer.getControl());
		taskTableViewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, taskTableViewer);
	}

	private void hookMouseAction() {
		// TODO Auto-generated method stub

	}

	private void hookDoubleClickAction() {
		taskTableViewer.addDoubleClickListener(openResultAction);
	}

	public void setFocus() {
		taskTableViewer.getTable().setFocus();
	}

	/**
	 * @return the taskTableViewer
	 */
	public TableViewer getTaskTableViewer() {
		return taskTableViewer;
	}

}