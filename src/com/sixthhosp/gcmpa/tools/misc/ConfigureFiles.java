package com.sixthhosp.gcmpa.tools.misc;

import java.util.ArrayList;
import java.util.HashMap;

import com.sixthhosp.gcmpa.xmlbeans.tool.ToolType.Configfiles;
import com.sixthhosp.gcmpa.xmlbeans.tool.ToolType.Configfiles.Configfile;



/**
 * �����ļ�������
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
	 * ��������ģ�ͣ����ģ����Ϣ���ɰ��������������ļ�
	 * 
	 * @param dataModel
	 */
	public void generateConfigFiles(HashMap<String, Object> dataModel) {
		for (ConfigureFile configureFile : configureFilesArrayList) {
			configureFile.generateConfigFile(dataModel);
		}
	}

	/**
	 * ���������ļ�����Ϣ����������ģ��
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
	 * ��������ļ���װ��
	 * 
	 * @param configureFile
	 */
	public void add(ConfigureFile configureFile) {
		configureFilesArrayList.add(configureFile);
	}

	/**
	 * ����ΪXMLBeans����
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
