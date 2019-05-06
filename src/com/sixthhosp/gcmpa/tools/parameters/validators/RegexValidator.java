package com.sixthhosp.gcmpa.tools.parameters.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sixthhosp.gcmpa.xmlbeans.tool.ValidatorType;

/**
 * 验证参数的输入字符串必须满足正则表达式
 * 
 * @author zhengzequn
 * 
 */
public class RegexValidator extends Validator {
	private String regex;

	public RegexValidator(ValidatorType validatorType) {
		super(validatorType);
		// TODO Auto-generated constructor stub
		setRegex(validatorType.getStringValue());
	}

	@Override
	public String validate(String input) {
		// TODO Auto-generated method stub
		if (regex != null) {
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(input);
			boolean b = matcher.matches();
			String messages = this.getMessage();
			if (!b) {
				if (messages != null) {
					return messages;
				} else {
					return "String must match the regex " + "\"" + regex + "\"";
				}
			}
		}
		return null;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String string = "Regex Validator: regex = " + getRegex()
				+ " message = " + getMessage();
		return string;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}
}
