package com.sixthhosp.gcmpa.tools.parameters.widgets;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Control;

/**
 * 参数GUI组件的基本接口
 * 
 * @author zhengzequn
 *
 */
public interface IWidgetMethods {

	/**
	 * 确认组件的输入是否为空
	 * 
	 * @return
	 */
	public boolean isEmpty();

	/**
	 * 返回参数组件的父控件
	 * 
	 * @return
	 */
	public Control getControl();

	/**
	 * 释放控件
	 * 
	 */
	public void dispose();
	
	/**
	 * 设置控件的背景颜色
	 * 
	 * @param color
	 */
	public void setWidgetBackgroudColor(Color color);
	
}
