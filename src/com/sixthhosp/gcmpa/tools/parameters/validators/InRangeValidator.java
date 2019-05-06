package com.sixthhosp.gcmpa.tools.parameters.validators;

import java.math.BigDecimal;

import com.sixthhosp.gcmpa.xmlbeans.tool.ValidatorType;

/**
 * 验证参数的输入值不能超出min和max范围.
 * 
 * @author zhengzequn
 * 
 */
public class InRangeValidator extends Validator {

	private BigDecimal min;
	private BigDecimal max;

	public InRangeValidator(ValidatorType validatorType) {
		super(validatorType);
		// TODO Auto-generated constructor stub
		setMin(validatorType.getMin());
		setMax(validatorType.getMax());
	}

	@Override
	public String validate(String input) {
		// TODO Auto-generated method stub
		try {
			BigDecimal val = new BigDecimal(input);

			if (min != null && min.compareTo(val) > 0) {
				String messages = this.getMessage();
				if (messages != null) {
					return messages;
				} else {
					return "Value must be greater than " + "\"" + min + "\"";
				}
			}

			if (max != null && max.compareTo(val) < 0) {
				String messages = this.getMessage();
				if (messages != null) {
					return messages;
				} else {
					return "Value must be lower than " + "\"" + max + "\"";
				}
			}
		} catch (NumberFormatException e) {
			// e.printStackTrace();
		}

		return null;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String string = "In Range Validator: min = " + getMin() + " max = "
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
