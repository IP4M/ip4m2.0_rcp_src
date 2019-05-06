package com.sixthhosp.gcmpa.tools.parameters.validators;

import java.math.BigDecimal;

import com.sixthhosp.gcmpa.xmlbeans.tool.ValidatorType;

/**
 * 验证输入参数的字符串长度不能超出min和max范围
 * 
 * @author zhengzequn
 * 
 */
public class LengthValidator extends Validator {

	private BigDecimal min;
	private BigDecimal max;

	public LengthValidator(ValidatorType validatorType) {
		super(validatorType);
		// TODO Auto-generated constructor stub
		setMin(validatorType.getMin());
		setMax(validatorType.getMax());
	}

	@Override
	public String validate(String input) {
		// TODO Auto-generated method stub
		BigDecimal length = new BigDecimal(input.length());

		if (min != null && min.compareTo(length) > 0) {
			String messages = this.getMessage();
			if (messages != null) {
				return messages;
			} else {
				return "String must have length of at least " + "\"" + min
						+ "\"";
			}
		}

		if (max != null && max.compareTo(length) < 0) {
			String messages = this.getMessage();
			if (messages != null) {
				return messages;
			} else {
				return "String must have length no more than " + "\"" + max
						+ "\"";
			}
		}

		return null;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String string = "Length Validator: min = " + getMin() + " max = "
				+ getMax() + " message = " + getMessage();
		return string;
	}

	public BigDecimal getMin() {
		return min;
	}

	public void setMin(BigDecimal min) {
		this.min = min;
	}

	public BigDecimal getMax() {
		return max;
	}

	public void setMax(BigDecimal max) {
		this.max = max;
	}

}
