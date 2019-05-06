package com.sixthhosp.gcmpa.tools.parameters.validators;

import com.sixthhosp.gcmpa.xmlbeans.tool.ValidatorType;
import com.sixthhosp.gcmpa.xmlbeans.tool.ValidatorType.Type;
import com.sixthhosp.gcmpa.xmlbeans.tool.ValidatorType.Type.Enum;

/**
 * 参数检查抽象类，适用于text,integer,float参数类型
 * 
 * @author zhengzequn
 * 
 */
public abstract class Validator {

	private Enum type;
	private String message;

	public Validator() {
		// TODO Auto-generated constructor stub
	}
	
	public Validator(ValidatorType validatorType) {
		// TODO Auto-generated constructor stub
		setType(validatorType.getType());
		setMessage(validatorType.getMessage());
	}

	/**
	 * 验证参数调用的方法,返回为null则输入正确.否则返回错误提示信息.
	 * 
	 * @return
	 */
	public abstract String validate(String input);

	/**
	 * 初始化参数检查类
	 * 
	 * @return
	 */
	public static Validator init(ValidatorType validatorType) {
		Enum type = validatorType.getType();
		if (type == Type.IN_RANGE) {
			return new InRangeValidator(validatorType);
		} else if (type == Type.LENGTH) {
			return new LengthValidator(validatorType);
		} else if (type == Type.REGEX) {
			return new RegexValidator(validatorType);
		}
		return null;
	}

	public Enum getType() {
		return type;
	}

	public void setType(Enum type) {
		this.type = type;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
