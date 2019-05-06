package com.sixthhosp.gcmpa.tools.parameters.widgets;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

/**
 * 文本框（域）GUI组件封装类
 * 
 * @author zhengzequn
 * 
 */
public class TextWidget implements IWidgetMethods {

	private int width;
	private int height;
	private String defaultText;

	private Composite composite;
	private Text text;

	public TextWidget(Composite parent, String defaultString, String size,
			boolean isArea) {
		// TODO Auto-generated constructor stub
		composite = new Composite(parent, SWT.NONE);
		if (defaultString == null) {
			defaultText = "";
		} else {
			defaultText = defaultString;
		}
		if (isArea) {
			String[] strings = size.split("x");
			height = Integer.parseInt(strings[0]);
			width = Integer.parseInt(strings[1]);

			text = new Text(composite, SWT.BORDER | SWT.MULTI);

			GC gc = new GC(text);
			FontMetrics fontMetrics = gc.getFontMetrics();

			// 在单个字体的宽度上加了一个单位的像素
			int widthPixel = width * (fontMetrics.getAverageCharWidth() + 1);
			int heightPixel = height * fontMetrics.getHeight();
			gc.dispose();

			text.setSize(text.computeSize(widthPixel, heightPixel));
			text.setText(this.defaultText);
		} else {
			width = Integer.parseInt(size);
			height = 1;
			text = new Text(composite, SWT.BORDER | SWT.SINGLE);

			GC gc = new GC(text);
			FontMetrics fontMetrics = gc.getFontMetrics();
			int widthPixel = width * (fontMetrics.getAverageCharWidth() + 1);
			int heightPixel = height * fontMetrics.getHeight();
			gc.dispose();

			text.setSize(text.computeSize(widthPixel, heightPixel));
			text.setText(this.defaultText);
		}
	}

	public boolean isEmpty() {
		if (StringUtils.isBlank(text.getText())) {
			return true;
		} else {
			return false;
		}
	}

	public void addInputValueChangeListener(
			final InputValueChangeListener listener) {
		text.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				listener.InputValueChangeEvent();
			}
		});
	}

	/**
	 * 获取文本框的输入 字符串
	 * 
	 * @return
	 */
	public String getInputString() {
		return text.getText();
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Text getText() {
		return text;
	}

	public String getDefaultText() {
		return defaultText;
	}

	@Override
	public Control getControl() {
		// TODO Auto-generated method stub
		return composite;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		composite.dispose();
	}

	@Override
	public void setWidgetBackgroudColor(Color color) {
		// TODO Auto-generated method stub
		composite.setBackground(color);
		text.setBackground(color);
	}
}
