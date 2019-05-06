package com.sixthhosp.gcmpa.tools.parameters;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.sixthhosp.gcmpa.xmlbeans.tool.ParamType;

import com.sixthhosp.gcmpa.tools.parameters.validators.ParameterValidateException;
import com.sixthhosp.gcmpa.tools.parameters.widgets.BooleanWidget;

/**
 * 布尔类型参数
 * 
 * @author zhengzequn
 * 
 */
public class BooleanToolParameter extends ToolParameter {

	private boolean checked;
	private String truevalue;
	private String falsevalue;

	private BooleanWidget booleanWidget;

	public BooleanToolParameter(ParamType paramType) {
		super(paramType);
		// TODO Auto-generated constructor stub
		setChecked(Boolean.parseBoolean(paramType.getChecked()));
		setTruevalue(paramType.getTruevalue());
		setFalsevalue(paramType.getFalsevalue());
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "BooleanToolParameter";
	}

	@Override
	protected void creatParamWidget() {
		// TODO Auto-generated method stub
		Composite composite = getPrimaryComposite();
		booleanWidget = new BooleanWidget(composite, checked);
	}

	@Override
	public Control getParamWidgetPrimaryControl() {
		// TODO Auto-generated method stub
		return booleanWidget.getControl();
	}

	@Override
	public String getInputString() {
		// TODO Auto-generated method stub
		if (booleanWidget != null
				&& !booleanWidget.getCheckButton().isDisposed()) {
			if (booleanWidget.isSelected()) {
				return truevalue;
			} else {
				return falsevalue;
			}
		} else {
			if (checked) {
				return truevalue;
			} else {
				return falsevalue;
			}
		}
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 对于布尔类型参数，不需要进行验证
	 */
	@Override
	public void validate() throws ParameterValidateException {
		// TODO Auto-generated method stub
		return;
	}

	@Override
	public void doSave() {
		// TODO Auto-generated method stub
		saveDataModel();
		if (booleanWidget != null
				&& !booleanWidget.getCheckButton().isDisposed()) {
			checked = booleanWidget.isSelected();
		}
	}

	@Override
	public ParamType saveAsParamType() {
		// TODO Auto-generated method stub
		ParamType newParamType = super.saveAsParamType();

		newParamType.setChecked(String.valueOf(checked));
		newParamType.setTruevalue(truevalue);
		newParamType.setFalsevalue(falsevalue);

		return newParamType;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getTruevalue() {
		return truevalue;
	}

	public void setTruevalue(String truevalue) {
		this.truevalue = truevalue;
	}

	public String getFalsevalue() {
		return falsevalue;
	}

	public void setFalsevalue(String falsevalue) {
		this.falsevalue = falsevalue;
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
