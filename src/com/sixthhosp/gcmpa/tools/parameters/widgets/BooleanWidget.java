package com.sixthhosp.gcmpa.tools.parameters.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * 布尔类型GUI组件封装类
 * 
 * @author zhengzequn
 * 
 */
public class BooleanWidget implements IWidgetMethods {

	private boolean checked;

	private Button checkButton;

	public BooleanWidget(Composite parent, boolean isChecked) {
		// TODO Auto-generated constructor stub
		checked = isChecked;

		checkButton = new Button(parent, SWT.CHECK);
		checkButton.setSelection(checked);
	}

	@Override
	public Control getControl() {
		// TODO Auto-generated method stub
		return checkButton;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	/**
	 * 返回布尔类型GUI组件的状态，Returns true if the receiver is selected, and false
	 * otherwise.
	 * 
	 * @return
	 */
	public boolean isSelected() {
		return checkButton.getSelection();
	}

	/**
	 * 返回布尔类型GUI组件的默认是否选中状态
	 * 
	 * @return
	 */
	public boolean getChecked() {
		return checked;
	}

	/**
	 * 返回Check Button
	 * 
	 * @return
	 */
	public Button getCheckButton() {
		return checkButton;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		checkButton.dispose();
	}

	@Override
	public void setWidgetBackgroudColor(Color color) {
		// TODO Auto-generated method stub
		checkButton.setBackground(color);
	}

}
