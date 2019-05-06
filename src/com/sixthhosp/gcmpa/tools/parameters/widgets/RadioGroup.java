package com.sixthhosp.gcmpa.tools.parameters.widgets;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * 单选框GUI组件封装类
 * 
 * @author zhengzequn
 *
 */
public class RadioGroup extends SelectionWidget {
	private Composite group;
	private Button[] buttons;

	private Object[] objects;
	private ISelectionTypeProvider provider;

	public RadioGroup(Composite parent) {
		group = new Composite(parent, SWT.NONE);
		FillLayout layout = new FillLayout(SWT.VERTICAL);
		layout.spacing = 3;

		group.setLayout(layout);
	}

	public void setInput(Object input) {
		objects = provider.getElements(input);
		buttons = new Button[objects.length];

		int selectedIndex = 0;
		for (int i = 0; i < objects.length; i++) {
			buttons[i] = new Button(group, SWT.RADIO | SWT.LEFT);
			buttons[i].setText(provider.getText(objects[i]));
			if (provider.getSelectionStat(objects[i])) {
				selectedIndex = i;
			}
		}
		buttons[selectedIndex].setSelection(true);
	}

	public Object[] getSelection() {
		ArrayList<Object> list = new ArrayList<Object>();
		for (int i = 0; i < objects.length; i++) {
			if (buttons[i].getSelection()) {
				list.add(objects[i]);
			}
		}
		return list.toArray();
	}

	public ArrayList<ObjectWithStat> getObjectsWithSelectionStat() {
		ArrayList<ObjectWithStat> list = new ArrayList<ObjectWithStat>();
		for (int i = 0; i < objects.length; i++) {
			if (buttons[i].getSelection()) {
				list.add(new ObjectWithStat(objects[i], true));
			} else {
				list.add(new ObjectWithStat(objects[i], false));
			}
		}
		return list;
	}

	/**
	 * 返回Radio Buttons的容器
	 * 
	 * @return
	 */
	public Composite getGroup() {
		return group;
	}

	public ISelectionTypeProvider getProvider() {
		return provider;
	}

	public void setProvider(ISelectionTypeProvider provider) {
		this.provider = provider;
	}

	public void dispose() {
		group.dispose();
	}

	@Override
	public void setSize(int width, int height) {
		// TODO Auto-generated method stub
		group.setSize(width, height);
	}

	@Override
	public Control getControl() {
		// TODO Auto-generated method stub
		return group;
	}

	@Override
	public void setWidgetBackgroudColor(Color color) {
		// TODO Auto-generated method stub
		group.setBackground(color);
		for(Button button : buttons){
			button.setBackground(color);
		}
	}

	@Override
	public void addInputValueChangeListener(final InputValueChangeListener listener) {
		// TODO Auto-generated method stub
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					listener.InputValueChangeEvent();
				}
			});
		}
	}
}
