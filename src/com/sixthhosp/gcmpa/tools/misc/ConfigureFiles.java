package com.sixthhosp.gcmpa.tools.misc;

import java.util.ArrayList;
import java.util.HashMap;

import com.sixthhosp.gcmpa.xmlbeans.tool.ToolType.Configfiles;
import com.sixthhosp.gcmpa.xmlbeans.tool.ToolType.Configfiles.Configfile;



/**
 * 配置文件集合类
 * 
 * @author zhengzequn
 * 
 */
public class ConfigureFiles {

	private ArrayList<ConfigureFile> configureFilesArrayList;
	private String workingDirectoryPath;

	public ConfigureFiles() {
		// TODO Auto-generated constructor stub
		configureFilesArrayList = new ArrayList<ConfigureFile>();
	}

	public ConfigureFiles(Configfiles configfiles, String workingDirectoryPath) {
		// TODO Auto-generated constructor stub
		this();
		this.workingDirectoryPath = workingDirectoryPath;
		if (configfiles != null) {
			Configfile[] configs = configfiles.getConfigfileArray();
			for (Configfile config : configs) {
				configureFilesArrayList.add(new ConfigureFile(config,
						this.workingDirectoryPath));
			}
		}
	}

	/**
	 * 根据数据模型，结合模板信息生成包含的所有配置文件
	 * 
	 * @param dataModel
	 */
	public void generateConfigFiles(HashMap<String, Object> dataModel) {
		for (ConfigureFile configureFile : configureFilesArrayList) {
			configureFile.generateConfigFile(dataModel);
		}
	}

	/**
	 * 根据配置文件的信息，更新数据模型
	 * 
	 * @param dataModel
	 */
	public HashMap<String, Object> updateDataModel(
			HashMap<String, Object> dataModel) {
		for (ConfigureFile configureFile : configureFilesArrayList) {
			dataModel.put(configureFile.getName(),
					configureFile.getConfigFileAbsPath());
		}
		return dataModel;
	}

	/**
	 * 添加配置文件封装类
	 * 
	 * @param configureFile
	 */
	public void add(ConfigureFile configureFile) {
		configureFilesArrayList.add(configureFile);
	}

	/**
	 * 保存为XMLBeans对象
	 * 
	 * @return
	 */
	public Configfiles saveAsConfigfiles() {
		Configfiles configfiles = Configfiles.Factory.newInstance();
		for (ConfigureFile file : configureFilesArrayList) {
			configfiles.addNewConfigfile().set(file.saveAsConfigfile());
		}

		return configfiles;
	}

	public ArrayList<ConfigureFile> getConfigureFilesArrayList() {
		return configureFilesArrayList;
	}

	public void setConfigureFilesArrayList(
			ArrayList<ConfigureFile> configureFilesArrayList) {
		this.configureFilesArrayList = configureFilesArrayList;
	}

	public String getWorkingDirectoryPath() {
		return workingDirectoryPath;
	}

	public void setWorkingDirectoryPath(String workingDirectoryPath) {
		this.workingDirectoryPath = workingDirectoryPath;
	}

}
