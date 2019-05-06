package com.sixthhosp.gcmpa.tools.parameters;

import com.sixthhosp.gcmpa.xmlbeans.tool.DataType;

public class DataToolOutput extends ToolOutput {

	public DataToolOutput(DataType dataType) {
		this.name = dataType.getName();
		this.filename = dataType.getFilename();
	}

	public DataType saveAsDataType() {
		DataType type = DataType.Factory.newInstance();
		type.setName(name);
		type.setFilename(filename);
		return type;
	}
}
