package com.sixthhosp.gcmpa.tools.parameters;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.sixthhosp.gcmpa.xmlbeans.tool.ParamType;
import com.sixthhosp.gcmpa.tools.parameters.validators.ParameterValidateException;
import com.sixthhosp.gcmpa.tools.parameters.validators.Validator;
import com.sixthhosp.gcmpa.tools.parameters.widgets.InputValueChangeListener;
import com.sixthhosp.gcmpa.tools.parameters.widgets.TextWidget;

/**
 * 文本类型参数
 * 
 * @author zhengzequn
 * 
 */
public class TextToolParameter extends ToolParameter {

	private boolean area;
	private String size;
	private String value;

	private TextWidget textWidget;

	public TextToolParameter(ParamType paramType) {
		super(paramType);
		// TODO Auto-generated constructor stub
		setArea(Boolean.parseBoolean(paramType.getArea()));
		setSize(paramType.getSize());
		setValue(paramType.getValue());
		if (value == null) {
			value = "";
		}
		if (area && !paramType.isSetSize()) {
			size = "5x50";
		}
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "TextToolParameter";
	}

	@Override
	protected void creatParamWidget() {
		// TODO Auto-generated method stub
		Composite composite = getPrimaryComposite();
		textWidget = new TextWidget(composite, value, size, area);
		textWidget.setWidgetBackgroudColor(composite.getDisplay()
				.getSystemColor(SWT.COLOR_WHITE));
	}

	@Override
	public Control getParamWidgetPrimaryControl() {
		// TODO Auto-generated method stub
		return textWidget.getControl();
	}

	@Override
	public String getInputString() {
		// TODO Auto-generated method stub
		if (textWidget != null && !textWidget.getText().isDisposed()) {
			if (textWidget.isEmpty()) {
				return "";
			} else {
				return textWidget.getInputString();
			}
		} else {
			return value;
		}
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return textWidget.isEmpty();
	}

	@Override
	public void validate() throws ParameterValidateException {
		// TODO Auto-generated method stub
		if (!isOptional() && isEmpty()) {
			throw new ParameterValidateException("A input value is required");
		}
		checkValidators();
	}

	@Override
	public boolean validateSavedValue() {
		// TODO Auto-generated method stub
		boolean b = true;
		try {

			if (!isOptional() && value.equals("")) {
				throw new ParameterValidateException(
						"A input value is required");
			}

			for (Validator validator : this.getValidators()) {
				String msg = validator.validate(value);
				if (msg != null) {
					throw new ParameterValidateException(msg);
				}
			}

		} catch (ParameterValidateException e) {
			b = false;
		}

		return b;
	}

	@Override
	public void doSave() {
		// TODO Auto-generated method stub
		saveDataModel();
		if (textWidget != null && !textWidget.getText().isDisposed()) {
			value = textWidget.getInputString();
		}
	}

	@Override
	public ParamType saveAsParamType() {
		// TODO Auto-generated method stub
		ParamType newParamType = super.saveAsParamType();

		newParamType.setArea(String.valueOf(area));
		newParamType.setSize(size);
		newParamType.setValue(value);

		return newParamType;
	}

	public boolean isArea() {
		return area;
	}

	public void setArea(boolean area) {
		this.area = area;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public void addValidatingEvent() {
		// TODO Auto-generated method stub
		textWidget.addInputValueChangeListener(new InputValueChangeListener() {

			@Override
			public void InputValueChangeEvent() {
				// TODO Auto-generated method stub
				startValidating();
			}
		});
	}

}
