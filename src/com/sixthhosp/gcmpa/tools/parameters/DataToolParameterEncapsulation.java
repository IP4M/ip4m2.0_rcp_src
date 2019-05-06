package com.sixthhosp.gcmpa.tools.parameters;

/**
 * �ļ��������Ͳ���ToolParameter�ķ�װ�࣬����workflowģʽ
 * 
 * @author zhengzequn
 * 
 */
public class DataToolParameterEncapsulation {

	private String label;
	private DataToolParameter dataToolParameter;

	public DataToolParameterEncapsulation(String label,
			DataToolParameter dataToolParameter) {
		// TODO Auto-generated constructor stub
		this.label = label;
		this.dataToolParameter = dataToolParameter;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Module Input: " + label;
	}

	public DataToolParameter getDataToolParameter() {
		return dataToolParameter;
	}

	public void setDataToolParameter(DataToolParameter dataToolParameter) {
		this.dataToolParameter = dataToolParameter;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
