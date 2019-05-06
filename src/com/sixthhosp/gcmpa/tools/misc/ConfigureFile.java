package com.sixthhosp.gcmpa.tools.misc;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;

import com.sixthhosp.gcmpa.configs.Utils;
import com.sixthhosp.gcmpa.tools.template.TemplateEngine;
import com.sixthhosp.gcmpa.xmlbeans.tool.ToolType.Configfiles.Configfile;

/**
 * �����ļ���װ��������������������������ļ���Ϣ
 * 
 * @author zhengzequn
 * 
 */
public class ConfigureFile {

	private String content;
	private String name;

	private String workingDirectoryPath;
	private String configFileAbsPath;

	public ConfigureFile() {
		// TODO Auto-generated constructor stub
	}

	public ConfigureFile(Configfile configfile) {
		// TODO Auto-generated constructor stub
		this();
		name = configfile.getName();
		content = configfile.getStringValue();
	}

	public ConfigureFile(Configfile configfile, String workingDirectoryPath) {
		this(configfile);
		this.workingDirectoryPath = workingDirectoryPath;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Config: " + name + "\nPath: " + configFileAbsPath;
	}

	/**
	 * ��������ģ��������Ӧ�������ļ��������ļ�����ڹ���Ŀ¼��
	 * 
	 * @param dataModel
	 */
	public void generateConfigFile(HashMap<String, Object> dataModel) {
		try {
			File dir = new File(workingDirectoryPath);
			if (!dir.exists()) {
				FileUtils.forceMkdir(dir);
			}

			File file = new File(workingDirectoryPath + "/"
					+ Utils.getRandomStringByUUID() + ".txt");
			configFileAbsPath = file.getAbsolutePath();
			String string = TemplateEngine.combineDataModelAndTemplate(
					dataModel, content);
			FileUtils.writeStringToFile(file, string);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * ����ΪXMLBeans����
	 * 
	 * @return
	 */
	public Configfile saveAsConfigfile() {
		Configfile configfile = Configfile.Factory.newInstance();

		configfile.setName(name);
		configfile.setStringValue(content);

		return configfile;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getConfigFileAbsPath() {
		return configFileAbsPath;
	}

	public String getWorkingDirectoryPath() {
		return workingDirectoryPath;
	}

	public void setWorkingDirectoryPath(String workingDirectoryPath) {
		this.workingDirectoryPath = workingDirectoryPath;
	}
}
