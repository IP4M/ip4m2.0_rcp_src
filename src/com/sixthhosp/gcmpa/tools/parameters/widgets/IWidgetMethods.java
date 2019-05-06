package com.sixthhosp.gcmpa.tools.parameters.widgets;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Control;

/**
 * ����GUI����Ļ����ӿ�
 * 
 * @author zhengzequn
 *
 */
public interface IWidgetMethods {

	/**
	 * ȷ������������Ƿ�Ϊ��
	 * 
	 * @return
	 */
	public boolean isEmpty();

	/**
	 * ���ز�������ĸ��ؼ�
	 * 
	 * @return
	 */
	public Control getControl();

	/**
	 * �ͷſؼ�
	 * 
	 */
	public void dispose();
	
	/**
	 * ���ÿؼ��ı�����ɫ
	 * 
	 * @param color
	 */
	public void setWidgetBackgroudColor(Color color);
	
}
