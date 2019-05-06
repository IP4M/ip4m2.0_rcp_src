package com.sixthhosp.gcmpa.tools.parameters.widgets;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Composite;

import com.sixthhosp.gcmpa.xmlbeans.tool.ParamType.Display;
import com.sixthhosp.gcmpa.xmlbeans.tool.ParamType.Display.Enum;
import com.sixthhosp.gcmpa.tools.parameters.provider.SelectWidgetProvider;

/**
 * 选项类型GUI组件的抽象基类
 * 
 * @author zhengzequn
 * 
 */
public abstract class SelectionWidget implements IWidgetMethods {

	/**
	 * 设置输入
	 * 
	 * @param input
	 */
	public abstract void setInput(Object input);

	/**
	 * 返回被选中对象数组
	 * 
	 * @return
	 */
	public abstract Object[] getSelection();

	/**
	 * 返回所有对象是否被选中的状态信息，
	 * 
	 * @return
	 */
	public abstract ArrayList<ObjectWithStat> getObjectsWithSelectionStat();

	/**
	 * 得到内容提供器
	 * 
	 * @return
	 */
	public abstract ISelectionTypeProvider getProvider();

	/**
	 * 设置内容提供器
	 * 
	 * @param provider
	 */
	public abstract void setProvider(ISelectionTypeProvider provider);

	/**
	 * 设置控件大小
	 * 
	 * @param width
	 * @param height
	 */
	public abstract void setSize(int width, int height);

	/**
	 * 检测选项类型GUI组件是否为空，即是否有对象被选中
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		if (getSelection().length > 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 注册事件，每一次改变了输入值都会触发该事件，适用于checkBox和checkTable
	 * 
	 * @param listener
	 */
	public abstract void addInputValueChangeListener(
			InputValueChangeListener listener);

	/**
	 * 初始化选项类型控件的静态方法，并设置了provider,这样setinput需要接收的参数类型为options[].
	 * 
	 * @param parent
	 * @param display
	 * @param multiple
	 * @return
	 */
	public static SelectionWidget init(Composite parent, Enum display,
			boolean multiple) {
		SelectWidgetProvider provider = new SelectWidgetProvider();

		if (display == Display.DEFAULT) {
			if (!multiple) {
				ComboGroup comboGroup = new ComboGroup(parent);
				comboGroup.setProvider(provider);
				return comboGroup;
			} else {
				CheckTableGroup checkTableGroup = new CheckTableGroup(parent);
				checkTableGroup.setProvider(provider);
				return checkTableGroup;
			}
		} else if (display == Display.RADIO) {
			RadioGroup radioGroup = new RadioGroup(parent);
			radioGroup.setProvider(provider);
			return radioGroup;

		} else if (display == Display.CHECKBOXES) {
			CheckBoxGroup checkBoxGroup = new CheckBoxGroup(parent);
			checkBoxGroup.setProvider(provider);
			return checkBoxGroup;
		}

		return null;
	}

	public static SelectionWidget init(Composite parent, Enum display,
			boolean multiple, String name) {
		SelectWidgetProvider provider = new SelectWidgetProvider();

		if (display == Display.DEFAULT) {
			if (!multiple) {
				ComboGroup comboGroup = new ComboGroup(parent);
				comboGroup.setProvider(provider);
				return comboGroup;
			} else {
				CheckTableGroup checkTableGroup = new CheckTableGroup(parent);
				checkTableGroup.setProvider(provider);
				return checkTableGroup;
			}
		} else if (display == Display.RADIO) {
			RadioGroup radioGroup = new RadioGroup(parent);
			radioGroup.setProvider(provider);
			return radioGroup;

		} else if (display == Display.CHECKBOXES) {
			if (name.startsWith("two")) {
				TwoColumnCheckBoxGroup checkBoxGroup = new TwoColumnCheckBoxGroup(parent);
				checkBoxGroup.setProvider(provider);
				return checkBoxGroup;
			} else {
				CheckBoxGroup checkBoxGroup = new CheckBoxGroup(parent);
				checkBoxGroup.setProvider(provider);
				return checkBoxGroup;
			}
		}

		return null;
	}
}
