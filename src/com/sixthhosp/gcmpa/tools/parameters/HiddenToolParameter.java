package com.sixthhosp.gcmpa.tools.parameters;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.sixthhosp.gcmpa.xmlbeans.tool.ParamType;

import com.sixthhosp.gcmpa.tools.parameters.validators.ParameterValidateException;

/**
 * 隐藏类型参数
 * 
 * @author zhengzequn
 * 
 */
public class HiddenToolParameter extends ToolParameter {

	private String value;

	public HiddenToolParameter(ParamType paramType) {
		super(paramType);
		// TODO Auto-generated constructor stub
		setValue(paramType.getValue());
		if (!paramType.isSetValue()) {
			value = "";
		}
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "HiddenToolParameter";
	}

	/**
	 * do nothing
	 */
	@Override
	public void creatContents(Composite parent, Composite topComposite) {
		// TODO Auto-generated method stub
	}

	/**
	 * do nothing
	 */
	@Override
	protected void creatParamWidget() {
		// TODO Auto-generated method stub
	}

	@Override
	public Control getParamWidgetPrimaryControl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getInputString() {
		// TODO Auto-generated method stub
		return value;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean startValidating() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void validate() throws ParameterValidateException {
		// TODO Auto-generated method stub
	}

	@Override
	public void doSave() {
		// TODO Auto-generated method stub
		saveDataModel();
	}

	@Override
	public ParamType saveAsParamType() {
		// TODO Auto-generated method stub
		ParamType newParamType = super.saveAsParamType();

		newParamType.setValue(value);

		return newParamType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public boolean validateSavedValue() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void addValidatingEvent() {
		// TODO Auto-generated method stub

	}
}
