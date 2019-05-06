package com.sixthhosp.gcmpa.tools;

import com.sixthhosp.gcmpa.xmlbeans.toolconf.LabelType;

/**
 * 模块的分隔标签
 * 
 * @author zhengzequn
 *
 */
public class ToolSectionLabel {

	private String id;
	private String text;
	private ToolSection toolSection;

	public ToolSectionLabel(LabelType labelType, ToolSection toolSection) {
		setId(labelType.getId());
		setText(labelType.getText());
		setToolSection(toolSection);
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Tool Section Label: " + text;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public ToolSection getToolSection() {
		return toolSection;
	}

	public void setToolSection(ToolSection toolSection) {
		this.toolSection = toolSection;
	}
}
