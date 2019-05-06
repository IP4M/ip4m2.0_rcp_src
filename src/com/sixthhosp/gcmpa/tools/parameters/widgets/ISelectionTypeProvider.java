package com.sixthhosp.gcmpa.tools.parameters.widgets;

/**
 * 选项类型GUI组件内容接口
 *
 * @author zhengzequn
 *
 */
public interface ISelectionTypeProvider {
	/**
	 * 获取输入元素
	 * 
	 * @param inputElement
	 * @return
	 */
	public Object[] getElements(Object inputElement);

	/**
	 * 获取元素的显示文本
	 * 
	 * @param element
	 * @return
	 */
	public String getText(Object element);

	/**
	 * 设置元素是否选中，true为选中，false为未选中
	 * 
	 * @param element
	 * @return
	 */
	public boolean getSelectionStat(Object element);
}
