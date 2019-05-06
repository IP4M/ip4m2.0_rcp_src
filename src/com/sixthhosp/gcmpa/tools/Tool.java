package com.sixthhosp.gcmpa.tools;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.xmlbeans.XmlException;

import com.sixthhosp.gcmpa.configs.ConfigFile;
import com.sixthhosp.gcmpa.xmlbeans.toolconf.ToolType;

/**
 * ToolBox中的模块封装类
 * 
 * @author zhengzequn
 * 
 */
public class Tool {

	/**
	 * 模块的id
	 */
	private String id;

	/**
	 * 模块的名称
	 */
	private String name;

	/**
	 * 模块的版本
	 */
	private String version;

	/**
	 * 模块的简明描述
	 */
	private String description;

	/**
	 * html帮助文件所在的相对路径
	 */
	private String help;

	/**
	 * html帮助文件所在的绝对路径
	 */
	private String helpPath;

	/**
	 * 模块图片所在的相对路径
	 */
	private String icon;

	/**
	 * 模块图片所在的绝对路径
	 */
	private String iconPath;

	private ToolSection toolSection;

	/**
	 * XML配置文件所在的相对路径
	 */
	private String file;

	/**
	 * XML配置文件所在的绝对路径
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
	 * 用于模块重跑回调
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
	 * 返回模块的id
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
	 * 返回模块的名称
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
	 * 返回模块的简明描述，null则表明描述信息不存在
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
	 * 返回XML配置文件所在的绝对路径
	 */
	public String getPath() {
		return filePath;
	}

	public void setPath(String path) {
		this.filePath = path;
	}

	/**
	 * 返回html帮助文件所在的绝对路径
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
	 * 返回模块的版本信息
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
	 * 返回模块图片所在的绝对路径信息
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
	 * 获取XML配置文件所在的绝对路径信息
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
