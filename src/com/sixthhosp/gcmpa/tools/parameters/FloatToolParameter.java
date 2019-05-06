package com.sixthhosp.gcmpa.tools.parameters;

import java.math.BigDecimal;
import com.sixthhosp.gcmpa.xmlbeans.tool.ParamType;

import com.sixthhosp.gcmpa.tools.parameters.validators.ParameterValidateException;
import com.sixthhosp.gcmpa.tools.parameters.validators.Validator;

/**
 * 实数类型参数
 * 
 * @author zhengzequn
 * 
 */
public class FloatToolParameter extends TextToolParameter {

	public FloatToolParameter(ParamType paramType) {
		super(paramType);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "FloatToolParameter";
	}

	@Override
	public void validate() throws ParameterValidateException {
		// TODO Auto-generated method stub
		if (!isOptional() && isEmpty()) {
			throw new ParameterValidateException("A input value is required.");
		}
		try {
			String input = getInputString();
			new BigDecimal(input);
		} catch (NumberFormatException exception) {
			throw new ParameterValidateException("A real number is required.");
		}
		checkValidators();
	}

	@Override
	public boolean validateSavedValue() {
		// TODO Auto-generated method stub
		boolean b = true;
		try {

			if (!isOptional() && this.getValue().equals("")) {
				throw new ParameterValidateException(
						"A input value is required");
			}

			try {
				String input = this.getValue();
				new BigDecimal(input);
			} catch (NumberFormatException exception) {
				throw new ParameterValidateException("A real number is required.");
			}

			for (Validator validator : this.getValidators()) {
				String msg = validator.validate(this.getValue());
				if (msg != null) {
					throw new ParameterValidateException(msg);
				}
			}

		} catch (ParameterValidateException e) {
			b = false;
		}

		return b;
	}
}
