package com.sixthhosp.gcmpa.tools.parameters.provider;

import com.sixthhosp.gcmpa.tools.parameters.Option;
import com.sixthhosp.gcmpa.tools.parameters.widgets.ISelectionTypeProvider;

/**
 * 选项类型参数GUI组件的内容提供器
 * 
 * @author zhengzequn
 *
 */
public class SelectWidgetProvider implements ISelectionTypeProvider {

	@Override
	public Object[] getElements(Object inputElement) {
		// TODO Auto-generated method stub
		return (Option[]) inputElement;
	}

	@Override
	public String getText(Object element) {
		// TODO Auto-generated method stub
		Option option = (Option) element;
		return option.getText();
	}

	@Override
	public boolean getSelectionStat(Object element) {
		// TODO Auto-generated method stub
		Option option = (Option) element;
		return option.isSelected();
	}

}
