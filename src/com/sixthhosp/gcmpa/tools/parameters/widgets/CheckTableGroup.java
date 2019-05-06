package com.sixthhosp.gcmpa.tools.parameters.widgets;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

/**
 * 多选框GUI组件封装类
 * 
 * @author zhengzequn
 * 
 */
public class CheckTableGroup extends SelectionWidget {
	private Composite composite;
	private Table table;
	private TableItem[] tableItem;

	private Object[] objects;
	private ISelectionTypeProvider provider;

	public CheckTableGroup(Composite parent) {
		composite = new Composite(parent, SWT.NONE);
		table = new Table(composite, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL
				| SWT.H_SCROLL);

		table.setSize(350, 120);

		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				if (!(e.detail == SWT.CHECK)) {
					table.deselectAll();
				}
			}
		});
	}

	public void setInput(Object input) {
		objects = provider.getElements(input);
		tableItem = new TableItem[objects.length];
		for (int i = 0; i < objects.length; i++) {
			tableItem[i] = new TableItem(table, SWT.NONE);
			tableItem[i].setText(provider.getText(objects[i]));
			tableItem[i].setChecked(provider.getSelectionStat(objects[i]));
		}
	}

	public Object[] getSelection() {
		ArrayList<Object> list = new ArrayList<Object>();
		for (int i = 0; i < objects.length; i++) {
			if (tableItem[i].getChecked()) {
				list.add(objects[i]);
			}
		}
		return list.toArray();
	}

	public ArrayList<ObjectWithStat> getObjectsWithSelectionStat() {
		ArrayList<ObjectWithStat> list = new ArrayList<ObjectWithStat>();
		for (int i = 0; i < objects.length; i++) {
			if (tableItem[i].getChecked()) {
				list.add(new ObjectWithStat(objects[i], true));
			} else {
				list.add(new ObjectWithStat(objects[i], false));
			}
		}
		return list;
	}

	/**
	 * 返回table容器
	 * 
	 * @return
	 */
	public Table getTable() {
		return table;
	}

	public ISelectionTypeProvider getProvider() {
		return provider;
	}

	public void setProvider(ISelectionTypeProvider provider) {
		this.provider = provider;
	}

	public void dispose() {
		composite.dispose();
	}

	@Override
	public void setSize(int width, int height) {
		// TODO Auto-generated method stub
		table.setSize(width, height);
	}

	@Override
	public Control getControl() {
		// TODO Auto-generated method stub
		return composite;
	}

	@Override
	public void setWidgetBackgroudColor(Color color) {
		// TODO Auto-generated method stub
		composite.setBackground(color);
		table.setBackground(color);
	}

	@Override
	public void addInputValueChangeListener(
			final InputValueChangeListener listener) {
		// TODO Auto-generated method stub
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				if (e.detail == SWT.CHECK) {
					listener.InputValueChangeEvent();
				}
			}
		});
	}

}
