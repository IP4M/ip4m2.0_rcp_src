package com.sixthhosp.gcmpa.tools.parameters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.xmlbeans.XmlObject;
import org.eclipse.swt.widgets.Composite;

import com.sixthhosp.gcmpa.xmlbeans.tool.WhenType;

import com.sixthhosp.gcmpa.tools.Utils;

/**
 * �������Ͳ�����Ӧ��������װ�࣬���治ͬѡ��ֵ��Ӧ�Ĳ�������
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
	 * ����inputs��Ӧ�Ŀؼ�
	 * 
	 * @param parent
	 * @param topComposite
	 */
	public void creatContents(Composite parent, Composite topComposite) {
		Utils.creatInputsContents(inputs, parent, topComposite);
	}

	/**
	 * ��֤inputs�е��û�������
	 * 
	 * @return
	 */
	public boolean startValidating() {
		return Utils.startValidatingForInputs(inputs);
	}

	/**
	 * ��֤���������ֵ�Ƿ������޶�
	 * 
	 * @return
	 */
	public boolean validateSavedValue() {
		return Utils.validateSavedValueForInputs(inputs);
	}

	/**
	 * �������DataToolParameter�����ڳ�Ա�����е�ֵ
	 */
	public void clearDataToolParameterSavedVaule() {
		// TODO Auto-generated method stub
		Utils.clearDataToolParameterSavedVauleForInputs(inputs);
	}

	/**
	 * ��inputs�е��û����뱣�浽��Ա����
	 */
	public void doSave() {
		Utils.doSaveForInputs(inputs);
	}

	/**
	 * ����inputs��Ӧ������ģ��
	 * 
	 * @return
	 */
	public HashMap<String, Object> getDataModelMap() {
		return Utils.getDataModelFromInputs(inputs);
	}

	/**
	 * ����ΪXMLBeans����
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
	 * ȷ�����е�inputs�Ƿ�Ϊ��
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
	 * �滻�ļ��������Ͳ�������ʽ�����ڹ�����ģʽ���ڿؼ�����֮ǰ����
	 */
	public void registerLabelDataWidget() {
		Utils.registerLabelDataWidgetForInputs(inputs);
	}

	/**
	 * ע��ʼ��ִ�м�飬�ڿؼ�������֮ǰע��
	 * 
	 * @param inputs
	 */
	public void registerAlwaysValidating() {
		Utils.registerAlwaysValidatingForInputs(inputs);
	}

	/**
	 * ����DataToolParameter����ķ�װ��list
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
