package com.sixthhosp.gcmpa.tools.parameters;

import java.io.File;
import java.util.HashMap;

/**
 * 输出参数类
 * 
 * @author zhengzequn
 *
 */
public abstract class ToolOutput {

	protected String name;
	protected String filename;
	/**
	 * 模块的输出目录
	 */
	private String outDirectory;
	private HashMap<String, String> dataModelMap;
	private String absOutputFilePath;

	/**
	 * 保存参数信息到数据模型
	 */
	public void doSave() {
		saveDataModel();
	}

	public void saveDataModel() {
		HashMap<String, String> map = new HashMap<String, String>();
		absOutputFilePath = outDirectory + "/" + filename;
		absOutputFilePath = new File(absOutputFilePath).getAbsolutePath();
		map.put(name, absOutputFilePath);
		dataModelMap = map;
	}

	/**
	 * 返回输出参数组件的数据模型
	 * 
	 * @return
	 */
	public HashMap<String, String> getDataModelMap() {
		return dataModelMap;
	}

	/**
	 * @return the outDirectory
	 */
	public String getOutDirectory() {
		return outDirectory;
	}

	/**
	 * @param outDirectory
	 *            the outDirectory to set
	 */
	public void setOutDirectory(String outDirectory) {
		this.outDirectory = outDirectory;
	}

	public String getName() {
		return name;
	}

	public String getFilename() {
		return filename;
	}

	/**
	 * @return the absOutputFilePath
	 */
	public String getAbsOutputFilePath() {
		return absOutputFilePath;
	}

}
