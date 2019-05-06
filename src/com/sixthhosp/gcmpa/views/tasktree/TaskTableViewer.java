package com.sixthhosp.gcmpa.views.tasktree;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.sixthhosp.gcmpa.data.DataFactory;
import com.sixthhosp.gcmpa.views.tasktree.provider.TaskContentProvider;
import com.sixthhosp.gcmpa.views.tasktree.provider.TaskLableProvider;

public class TaskTableViewer extends TableViewer {

	public TaskTableViewer(Composite parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
		Table table = this.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(false);
		TableColumn column1 = new TableColumn(table, SWT.LEFT);
		// column1.setAlignment(SWT.LEFT);
		column1.setText("Task Name");
		column1.setWidth(300);
		TableColumn column2 = new TableColumn(table, SWT.LEFT);
		// column2.setAlignment(SWT.LEFT);
		column2.setText("Start Time");
		column2.setWidth(120);
		TableColumn column3 = new TableColumn(table, SWT.LEFT);
		// column3.setAlignment(SWT.LEFT);
		column3.setText("End Time");
		column3.setWidth(120);
		TableColumn column4 = new TableColumn(table, SWT.LEFT);
		column4.setText("State");
		column4.setWidth(120);
		
		this.setContentProvider(new TaskContentProvider());
		this.setLabelProvider(new TaskLableProvider(this));
		this.setInput(DataFactory.getTaskBox());
	}
}
