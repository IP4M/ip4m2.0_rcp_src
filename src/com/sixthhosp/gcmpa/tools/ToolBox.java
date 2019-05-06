package com.sixthhosp.gcmpa.tools;

import java.util.Collection;
import java.util.LinkedHashMap;

import com.sixthhosp.gcmpa.xmlbeans.toolconf.SectionType;
import com.sixthhosp.gcmpa.xmlbeans.toolconf.ToolboxDocument;
import com.sixthhosp.gcmpa.xmlbeans.toolconf.ToolboxType;

/**
 * 模块分类树的内容封装类，用有序字典保存了模块的分类信息。
 * 
 * @author zhengzequn
 * 
 */
public class ToolBox {
	private LinkedHashMap<String, ToolSection> tool_panel;

	public ToolBox() {
		tool_panel = new LinkedHashMap<String, ToolSection>();
	}

	public ToolBox(LinkedHashMap<String, ToolSection> linkedHashMap) {
		// TODO Auto-generated constructor stub
		this();
		setTool_panel(linkedHashMap);
	}

	public ToolBox(ToolboxDocument toolboxDocument) {
		this();
		ToolboxType toolboxType = toolboxDocument.getToolbox();
		for (SectionType sectionType : toolboxType.getSectionArray()) {
			ToolSection toolSection = new ToolSection(sectionType, null);
			String id = toolSection.getId();
			tool_panel.put(id, toolSection);
		}

	}

	/**
	 * 返回模块分类对象数组
	 * 
	 * @return
	 */
	public ToolSection[] getToolSections() {
		Collection<ToolSection> collection = tool_panel.values();
		return collection.toArray(new ToolSection[collection.size()]);
	}

	public ToolSection getFirstToolSection() {
		return tool_panel.get(tool_panel.keySet().iterator().next());
	}

	public LinkedHashMap<String, ToolSection> getTool_panel() {
		return tool_panel;
	}

	public void setTool_panel(LinkedHashMap<String, ToolSection> tool_panel) {
		this.tool_panel = tool_panel;
	}
}
