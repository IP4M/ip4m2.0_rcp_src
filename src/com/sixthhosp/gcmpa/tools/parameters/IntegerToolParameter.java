package com.sixthhosp.gcmpa.tools.parameters;

import java.math.BigInteger;

import com.sixthhosp.gcmpa.xmlbeans.tool.ParamType;

import com.sixthhosp.gcmpa.tools.parameters.validators.ParameterValidateException;
import com.sixthhosp.gcmpa.tools.parameters.validators.Validator;

/**
 * 整数类型参数
 * 
 * @author zhengzequn
 * 
 */
public class IntegerToolParameter extends TextToolParameter {

	public IntegerToolParameter(ParamType paramType) {
		super(paramType);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "IntegerToolParameter";
	}

	@Override
	public void validate() throws ParameterValidateException {
		// TODO Auto-generated method stub
		if (!isOptional() && isEmpty()) {
			throw new ParameterValidateException("A input value is required.");
		}
		try {
			String input = getInputString();
			new BigInteger(input);
		} catch (NumberFormatException exception) {
			throw new ParameterValidateException("An integer is required.");
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
				new BigInteger(input);
			} catch (NumberFormatException exception) {
				throw new ParameterValidateException("An integer is required.");
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
