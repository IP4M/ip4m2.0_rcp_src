package com.sixthhosp.gcmpa.views.tasktree.provider;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.sixthhosp.gcmpa.tasks.TaskBox;

public class TaskContentProvider implements IStructuredContentProvider {

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object[] getElements(Object inputElement) {
		TaskBox taskBox = (TaskBox) inputElement;
		return taskBox.getTasksArrary();
	}

}
