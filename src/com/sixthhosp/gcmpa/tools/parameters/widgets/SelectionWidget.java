package com.sixthhosp.gcmpa.tools.parameters.widgets;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Composite;

import com.sixthhosp.gcmpa.xmlbeans.tool.ParamType.Display;
import com.sixthhosp.gcmpa.xmlbeans.tool.ParamType.Display.Enum;
import com.sixthhosp.gcmpa.tools.parameters.provider.SelectWidgetProvider;

/**
 * ѡ������GUI����ĳ������
 * 
 * @author zhengzequn
 * 
 */
public abstract class SelectionWidget implements IWidgetMethods {

	/**
	 * ��������
	 * 
	 * @param input
	 */
	public abstract void setInput(Object input);

	/**
	 * ���ر�ѡ�ж�������
	 * 
	 * @return
	 */
	public abstract Object[] getSelection();

	/**
	 * �������ж����Ƿ�ѡ�е�״̬��Ϣ��
	 * 
	 * @return
	 */
	public abstract ArrayList<ObjectWithStat> getObjectsWithSelectionStat();

	/**
	 * �õ������ṩ��
	 * 
	 * @return
	 */
	public abstract ISelectionTypeProvider getProvider();

	/**
	 * ���������ṩ��
	 * 
	 * @param provider
	 */
	public abstract void setProvider(ISelectionTypeProvider provider);

	/**
	 * ���ÿؼ���С
	 * 
	 * @param width
	 * @param height
	 */
	public abstract void setSize(int width, int height);

	/**
	 * ���ѡ������GUI����Ƿ�Ϊ�գ����Ƿ��ж���ѡ��
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
	 * ע���¼���ÿһ�θı�������ֵ���ᴥ�����¼���������checkBox��checkTable
	 * 
	 * @param listener
	 */
	public abstract void addInputValueChangeListener(
			InputValueChangeListener listener);

	/**
	 * ��ʼ��ѡ�����Ϳؼ��ľ�̬��������������provider,����setinput��Ҫ���յĲ�������Ϊoptions[].
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
