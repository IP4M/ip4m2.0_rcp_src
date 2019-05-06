package com.sixthhosp.gcmpa.tools.parameters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.xmlbeans.XmlObject;
import org.eclipse.swt.widgets.Composite;

import com.sixthhosp.gcmpa.xmlbeans.tool.WhenType;

import com.sixthhosp.gcmpa.tools.Utils;

/**
 * 条件类型参数对应的条件封装类，保存不同选项值对应的参数内容
 * 
 * @author zhengzequn
 * 
 */
public class ConditionalWhen {

	private String value;
	private LinkedHashMap<String, Object> inputs;

	public ConditionalWhen(WhenType whenType) {
		// TODO Auto-generated constructor stub
		setValue(whenType.getValue());

		XmlObject[] xmlObjects = whenType.selectChildren(Utils
				.getInputsAllQnameSet());
		inputs = Utils.parseInputElement(xmlObjects);
	}

	public ConditionalWhen(String value, LinkedHashMap<String, Object> map) {
		this.value = value;
		inputs = map;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return inputs.toString();
	}

	/**
	 * 创建inputs对应的控件
	 * 
	 * @param parent
	 * @param topComposite
	 */
	public void creatContents(Composite parent, Composite topComposite) {
		Utils.creatInputsContents(inputs, parent, topComposite);
	}

	/**
	 * 验证inputs中的用户的输入
	 * 
	 * @return
	 */
	public boolean startValidating() {
		return Utils.startValidatingForInputs(inputs);
	}

	/**
	 * 验证保存的输入值是否满足限定
	 * 
	 * @return
	 */
	public boolean validateSavedValue() {
		return Utils.validateSavedValueForInputs(inputs);
	}

	/**
	 * 清除参数DataToolParameter保存在成员变量中的值
	 */
	public void clearDataToolParameterSavedVaule() {
		// TODO Auto-generated method stub
		Utils.clearDataToolParameterSavedVauleForInputs(inputs);
	}

	/**
	 * 将inputs中的用户输入保存到成员变量
	 */
	public void doSave() {
		Utils.doSaveForInputs(inputs);
	}

	/**
	 * 返回inputs对应的数据模型
	 * 
	 * @return
	 */
	public HashMap<String, Object> getDataModelMap() {
		return Utils.getDataModelFromInputs(inputs);
	}

	/**
	 * 保存为XMLBeans对象
	 * 
	 * @return
	 */
	public WhenType saveAsWhenType() {
		WhenType whenType = WhenType.Factory.newInstance();
		whenType.setValue(value);

		ArrayList<XmlObject> xmlObjects = Utils.getXmlObjectsFromInputs(inputs);
		for (XmlObject child : xmlObjects) {
			Utils.addInputsXMLObjectsToParentObject(whenType, child);
		}
		return whenType;
	}

	/**
	 * 确认其中的inputs是否为空
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		if (inputs.size() == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 替换文件输入类型参数的样式，用于工作流模式，在控件产生之前调用
	 */
	public void registerLabelDataWidget() {
		Utils.registerLabelDataWidgetForInputs(inputs);
	}

	/**
	 * 注册始终执行检查，在控件被创建之前注册
	 * 
	 * @param inputs
	 */
	public void registerAlwaysValidating() {
		Utils.registerAlwaysValidatingForInputs(inputs);
	}

	/**
	 * 返回DataToolParameter对象的封装类list
	 * 
	 * @return
	 */
	public ArrayList<DataToolParameterEncapsulation> getDataToolParameterEncapsulationList(
			String labelPrefix) {
		ArrayList<DataToolParameterEncapsulation> list = new ArrayList<DataToolParameterEncapsulation>();
		list.addAll(Utils.getDataToolParameterEncapsulationListFromInputs(
				inputs, labelPrefix));
		return list;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public LinkedHashMap<String, Object> getInputs() {
		return inputs;
	}

	public void setInputs(LinkedHashMap<String, Object> inputs) {
		this.inputs = inputs;
	}

}
