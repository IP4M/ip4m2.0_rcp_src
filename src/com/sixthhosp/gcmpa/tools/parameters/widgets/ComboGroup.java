package com.sixthhosp.gcmpa.tools.parameters.widgets;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * 下拉单选框GUI组件封装类
 * 
 * @author zhengzequn
 *
 */
public class ComboGroup extends SelectionWidget {
	private Combo combo;

	private Object[] objects;
	private ISelectionTypeProvider provider;

	public ComboGroup(Composite parent) {
		combo = new Combo(parent, SWT.DROP_DOWN | SWT.READ_ONLY);
	}

	public void setInput(Object input) {
		objects = provider.getElements(input);

		int selectedIndex = 0;
		for (int i = 0; i < objects.length; i++) {
			combo.add(provider.getText(objects[i]));
			if (provider.getSelectionStat(objects[i]))
				selectedIndex = i;
		}
		combo.select(selectedIndex);
	}

	public Object[] getSelection() {
		int i = combo.getSelectionIndex();
		if (i == -1) {
			return new Object[0];
		} else {
			return new Object[] { objects[i] };
		}
	}

	public ArrayList<ObjectWithStat> getObjectsWithSelectionStat() {
		ArrayList<ObjectWithStat> list = new ArrayList<ObjectWithStat>();
		int index = combo.getSelectionIndex();
		for (int i = 0; i < objects.length; i++) {
			if (index == i) {
				list.add(new ObjectWithStat(objects[i], true));
			} else {
				list.add(new ObjectWithStat(objects[i], false));
			}
		}
		return list;
	}

	/**
	 * 返回下拉单选框组件
	 * 
	 * @return
	 */
	public Combo getCombo() {
		return combo;
	}

	public ISelectionTypeProvider getProvider() {
		return provider;
	}

	public void setProvider(ISelectionTypeProvider provider) {
		this.provider = provider;
	}

	public void dispose() {
		combo.dispose();
	}

	@Override
	public void setSize(int width, int height) {
		// TODO Auto-generated method stub
		combo.setSize(width, height);
	}

	@Override
	public Control getControl() {
		// TODO Auto-generated method stub
		return combo;
	}

	@Override
	public void setWidgetBackgroudColor(Color color) {
		// TODO Auto-generated method stub
		combo.setBackground(color);
	}

	@Override
	public void addInputValueChangeListener(
			final InputValueChangeListener listener) {
		// TODO Auto-generated method stub
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				listener.InputValueChangeEvent();
			}
		});
	}

}
