package com.sixthhosp.gcmpa.tools.parameters.widgets;

/**
 * ѡ������GUI������ݽӿ�
 *
 * @author zhengzequn
 *
 */
public interface ISelectionTypeProvider {
	/**
	 * ��ȡ����Ԫ��
	 * 
	 * @param inputElement
	 * @return
	 */
	public Object[] getElements(Object inputElement);

	/**
	 * ��ȡԪ�ص���ʾ�ı�
	 * 
	 * @param element
	 * @return
	 */
	public String getText(Object element);

	/**
	 * ����Ԫ���Ƿ�ѡ�У�trueΪѡ�У�falseΪδѡ��
	 * 
	 * @param element
	 * @return
	 */
	public boolean getSelectionStat(Object element);
}
