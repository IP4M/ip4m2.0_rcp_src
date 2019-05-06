package com.sixthhosp.gcmpa.tools.parameters;

import com.sixthhosp.gcmpa.xmlbeans.tool.HtmlType;

public class HtmlToolOutput extends ToolOutput {

	public HtmlToolOutput(HtmlType htmlType) {
		this.name = htmlType.getName();
		this.filename = htmlType.getFilename();
	}

	public HtmlType saveAsHtmlType() {
		HtmlType type = HtmlType.Factory.newInstance();
		type.setName(name);
		type.setFilename(filename);
		return type;
	}
}
