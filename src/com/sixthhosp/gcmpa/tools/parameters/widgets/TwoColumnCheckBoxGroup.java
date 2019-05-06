package com.sixthhosp.gcmpa.tools.parameters.widgets;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * 复选框GUI组件封装类
 * 
 * @author zhengzequn
 * 
 */
public class TwoColumnCheckBoxGroup extends SelectionWidget {
	private Composite composite;
	private Button SelectAll;
	private Button UnSelectAll;
	private Composite group;
	private Button[] buttons;

	private Object[] objects;
	private ISelectionTypeProvider provider;

	public TwoColumnCheckBoxGroup(Composite parent) {
		composite = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.verticalSpacing = 0;
		composite.setLayout(gridLayout);

		SelectAll = new Button(composite, SWT.PUSH);
		SelectAll.setText("Select All");
		SelectAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				for (int i = 0; i < buttons.length; i++) {
					buttons[i].setSelection(true);
				}
			}
		});

		UnSelectAll = new Button(composite, SWT.PUSH);
		UnSelectAll.setText("UnSelect All");
		UnSelectAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				for (int i = 0; i < buttons.length; i++) {
					buttons[i].setSelection(false);
				}
			}
		});

		group = new Composite(composite, SWT.NONE);
		GridData gridData = new GridData();
		gridData.horizontalSpan = 2;
		group.setLayoutData(gridData);
		// System.out.println("run tow");
		// FillLayout fillLayout = new FillLayout(SWT.VERTICAL);
		// fillLayout.spacing = 3;
		// group.setLayout(fillLayout);

		GridLayout gridLayout2 = new GridLayout(2, true);
		gridLayout2.verticalSpacing = 3;
		gridLayout2.horizontalSpacing = 80;
		group.setLayout(gridLayout2);

	}

	public void setInput(Object input) {
		objects = provider.getElements(input);
		buttons = new Button[objects.length];
		for (int i = 0; i < objects.length; i++) {
			buttons[i] = new Button(group, SWT.CHECK | SWT.LEFT);
			buttons[i].setText(provider.getText(objects[i]));
			buttons[i].setSelection(provider.getSelectionStat(objects[i]));
		}
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
	 * 返回Check Buttons的容器
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
		composite.dispose();
	}

	@Override
	public void setSize(int width, int height) {
		// TODO Auto-generated method stub
		composite.setSize(width, height);
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
		group.setBackground(color);
		// SelectAll.setBackground(color);
		// UnSelectAll.setBackground(color);
		for (Button button : buttons) {
			button.setBackground(color);
		}
	}

	@Override
	public void addInputValueChangeListener(
			final InputValueChangeListener listener) {
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

		SelectAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				listener.InputValueChangeEvent();
			}
		});

		UnSelectAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				listener.InputValueChangeEvent();
			}
		});
	}

}
