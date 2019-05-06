package com.sixthhosp.gcmpa.editors.paramui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * 容器分隔符，可以设置颜色，适用于GridLayout的容器
 * 
 * @author zhengzequn
 * 
 */
public class SeparatorBar {

	private RGB backgroudRgb;

	private Composite composite;

	public SeparatorBar() {
		// TODO Auto-generated constructor stub
		backgroudRgb = new RGB(138, 43, 226);
	}

	public void creatContents(Composite parent) {
		composite = new Composite(parent, SWT.NONE);
		GridData compositeGridData = new GridData(GridData.FILL_HORIZONTAL);
		compositeGridData.heightHint = 1;

		composite.setLayoutData(compositeGridData);
		composite.setLayout(new GridLayout());
		// Label label = new Label(composite, SWT.NONE);

		final Color color = new Color(Display.getCurrent(), backgroudRgb);

		composite.setBackground(color);

		composite.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				// TODO Auto-generated method stub
				color.dispose();
			}
		});
	}

	public Composite getComposite() {
		return composite;
	}

	public void setComposite(Composite composite) {
		this.composite = composite;
	}

	public RGB getBackgroudRgb() {
		return backgroudRgb;
	}

	public void setBackgroudRgb(RGB backgroudRgb) {
		this.backgroudRgb = backgroudRgb;
	}

}
