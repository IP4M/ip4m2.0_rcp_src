package com.sixthhosp.gcmpa.tools;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.xmlbeans.XmlException;

import com.sixthhosp.gcmpa.configs.ConfigFile;
import com.sixthhosp.gcmpa.xmlbeans.toolconf.ToolType;

/**
 * ToolBox�е�ģ���װ��
 * 
 * @author zhengzequn
 * 
 */
public class Tool {

	/**
	 * ģ���id
	 */
	private String id;

	/**
	 * ģ�������
	 */
	private String name;

	/**
	 * ģ��İ汾
	 */
	private String version;

	/**
	 * ģ��ļ�������
	 */
	private String description;

	/**
	 * html�����ļ����ڵ����·��
	 */
	private String help;

	/**
	 * html�����ļ����ڵľ���·��
	 */
	private String helpPath;

	/**
	 * ģ��ͼƬ���ڵ����·��
	 */
	private String icon;

	/**
	 * ģ��ͼƬ���ڵľ���·��
	 */
	private String iconPath;

	private ToolSection toolSection;

	/**
	 * XML�����ļ����ڵ����·��
	 */
	private String file;

	/**
	 * XML�����ļ����ڵľ���·��
	 */
	private String filePath;

	public Tool(ToolType toolType, ToolSection toolSection) {
		setFile(toolType.getFile());
		setHelp(toolType.getHelp());
		setIcon(toolType.getIcon());
		setToolSection(toolSection);

		if (StringUtils.isBlank(help)) {
			helpPath = "";
		} else {
			helpPath = new File(ConfigFile.getSoftFolderPath() + "/" + help)
					.getAbsolutePath();
		}

		filePath = new File(ConfigFile.getSoftFolderPath() + "/" + file)
				.getAbsolutePath();

		if (StringUtils.isBlank(icon)) {
			iconPath = "";
		} else {
			iconPath = new File(ConfigFile.getSoftFolderPath() + "/" + icon)
					.getAbsolutePath();
		}

		File toolXMLFile = new File(filePath);
		try {
			com.sixthhosp.gcmpa.xmlbeans.tool.ToolDocument toolDocument = com.sixthhosp.gcmpa.xmlbeans.tool.ToolDocument.Factory
					.parse(toolXMLFile);
			com.sixthhosp.gcmpa.xmlbeans.tool.ToolType tool = toolDocument
					.getTool();

			setId(tool.getId());
			setName(tool.getName());
			setVersion(tool.getVersion());
			setDescription(tool.getDescription());
			if (description == null) {
				description = "";
			}
		} catch (XmlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * ����ģ�����ܻص�
	 * 
	 * @param toolType
	 * @param xmlPath
	 */
	public Tool(com.sixthhosp.gcmpa.xmlbeans.tool.ToolType toolType,
			String xmlPath) {
		helpPath = toolType.getHelppath();
		iconPath = toolType.getIconpath();
		filePath = xmlPath;
		setId(toolType.getId());
		setName(toolType.getName());
		setVersion(toolType.getVersion());
		setDescription(toolType.getDescription());
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Tool: " + name;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public void setHelp(String help) {
		this.help = help;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * ����ģ���id
	 * 
	 * @return
	 */
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ToolSection getToolSection() {
		return toolSection;
	}

	public void setToolSection(ToolSection toolSection) {
		this.toolSection = toolSection;
	}

	/**
	 * ����ģ�������
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * ����ģ��ļ���������null�����������Ϣ������
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * ����XML�����ļ����ڵľ���·��
	 */
	public String getPath() {
		return filePath;
	}

	public void setPath(String path) {
		this.filePath = path;
	}

	/**
	 * ����html�����ļ����ڵľ���·��
	 * 
	 * @return
	 */
	public String getHelpPath() {
		return helpPath;
	}

	public void setHelpPath(String helpPath) {
		this.helpPath = helpPath;
	}

	/**
	 * ����ģ��İ汾��Ϣ
	 * 
	 * @return
	 */
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * ����ģ��ͼƬ���ڵľ���·����Ϣ
	 * 
	 * @return
	 */
	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	/**
	 * ��ȡXML�����ļ����ڵľ���·����Ϣ
	 * 
	 * @return
	 */
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
