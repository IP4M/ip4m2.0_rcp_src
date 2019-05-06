package com.sixthhosp.gcmpa.tools.misc;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import com.sixthhosp.gcmpa.tools.template.TemplateEngine;
import com.sixthhosp.gcmpa.xmlbeans.tool.ToolType.Command;

/**
 * �����з�װ����������ֵ�滻ǰ��ģ����Ϣ����������������ڣ�����ֵ�滻֮��ľ��������С�
 * 
 * @author zhengzequn
 * 
 */
public class CommandLine {

	private String template;
	private String interpreter;
	private String commandLineContent;

	public CommandLine() {
		// TODO Auto-generated constructor stub
	}

	public CommandLine(Command command) {
		// TODO Auto-generated constructor stub
		interpreter = command.getInterpreter();
		if (StringUtils.isBlank(interpreter)) {
			interpreter = "";
		}
		template = command.getStringValue();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Module Command Line";
	}

	/**
	 * ����velocty������в�ֵ�滻�����ṩ����ģ��
	 * 
	 * @param dataModel
	 */
	public void velocityinterpolate(HashMap<String, Object> dataModel) {
		String string = TemplateEngine.combineDataModelAndTemplate(dataModel,
				template);
		string = string.replaceAll("\\n", " ").replaceAll("\\r", " ");
		string = StringUtils.trimToEmpty(string);
		commandLineContent = string;
	}

	/**
	 * �������տ�ִ�е�������
	 * 
	 * @return
	 */
	public String getExecuteCommandLine() {
		if (StringUtils.isBlank(interpreter)) {
			return commandLineContent;
		} else {
			return interpreter + " " + commandLineContent;
		}
	}

	/**
	 * ����ΪXMLBeans����
	 * 
	 * @return
	 */
	public Command saveAsCommand() {
		Command command = Command.Factory.newInstance();
		if (StringUtils.isNotBlank(interpreter)) {
			command.setInterpreter(interpreter);
		}
		command.setStringValue(template);
		return command;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getInterpreter() {
		return interpreter;
	}

	public void setInterpreter(String interpreter) {
		this.interpreter = interpreter;
	}

	public String getCommandLineContent() {
		return commandLineContent;
	}

	public void setCommandLineContent(String commandLineContent) {
		this.commandLineContent = commandLineContent;
	}
}
