package com.sixthhosp.gcmpa.tools.parameters;

import com.sixthhosp.gcmpa.xmlbeans.tool.OptionType;

/**
 * ѡ���װ�࣬���ڱ���ѡ���������Ϣ.
 * 
 * @author zhengzq
 * 
 */
public class Option {

	private String text;
	private String value;
	private boolean selected;

	public Option() {
		// TODO Auto-generated constructor stub
	}

	public Option(OptionType optionType) {
		// TODO Auto-generated constructor stub
		this.text = optionType.getStringValue();
		this.value = optionType.getValue();
		this.selected = Boolean.parseBoolean(optionType.getSelected());
	}

	public Option(String text, String value, boolean isSelected) {
		// TODO Auto-generated constructor stub
		this.text = text;
		this.value = value;
		this.selected = isSelected;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Option:" + " [text|" + text + "] " + "[value|" + value + "] "
				+ "[selected|" + selected + "]";
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}