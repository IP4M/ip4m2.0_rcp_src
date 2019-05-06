package com.sixthhosp.gcmpa.tools.misc;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import com.sixthhosp.gcmpa.tools.template.TemplateEngine;
import com.sixthhosp.gcmpa.xmlbeans.tool.ToolType.Command;

/**
 * 命令行封装器，包括插值替换前的模板信息，解释器（如果存在），插值替换之后的具体命令行。
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
	 * 利用velocty引擎进行插值替换，需提供数据模型
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
	 * 返回最终可执行的命令行
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
	 * 保存为XMLBeans对象
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
