package com.sixthhosp.gcmpa.tools;

import java.util.LinkedHashMap;

import org.apache.xmlbeans.XmlObject;

import com.sixthhosp.gcmpa.xmlbeans.toolconf.LabelType;
import com.sixthhosp.gcmpa.xmlbeans.toolconf.SectionType;
import com.sixthhosp.gcmpa.xmlbeans.toolconf.ToolType;

/**
 * 模块分类树的分组信息封装类，用有序字典保存了模块的信息
 * 
 * @author zhengzequn
 * 
 */
public class ToolSection {

	private String id;
	private String name;
	private String icon;
	private ToolSection parentToolSection;

	private LinkedHashMap<String, Object> section_panel;

	public ToolSection() {
		section_panel = new LinkedHashMap<String, Object>();
	}

	public ToolSection(SectionType sectionType, ToolSection parentToolSection) {
		this();
		setId(sectionType.getId());
		setName(sectionType.getName());
		setIcon(sectionType.getIcon());
		setParentToolSection(parentToolSection);
		XmlObject[] xmlObjects = sectionType.selectChildren(Utils
				.getSectionAllQnameSet());
		for (XmlObject xmlObject : xmlObjects) {
			if (xmlObject instanceof LabelType) {
				LabelType labelType = (LabelType) xmlObject;
				ToolSectionLabel toolSectionLabel = new ToolSectionLabel(
						labelType, this);
				String id = toolSectionLabel.getId();
				section_panel.put(id, toolSectionLabel);
			} else if (xmlObject instanceof ToolType) {
				ToolType toolType = (ToolType) xmlObject;
				Tool tool = new Tool(toolType, this);
				String id = tool.getId();
				section_panel.put(id, tool);
			} else if (xmlObject instanceof SectionType) {
				SectionType type = (SectionType) xmlObject;
				ToolSection toolSection = new ToolSection(type, this);
				String id = toolSection.getId();
				section_panel.put(id, toolSection);
			}
		}
	}

	public Object[] getChildren() {
		return section_panel.values().toArray();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Tool Section: " + name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public LinkedHashMap<String, Object> getSection_panel() {
		return section_panel;
	}

	public void setSection_panel(LinkedHashMap<String, Object> section_panel) {
		this.section_panel = section_panel;
	}

	/**
	 * @return the parentToolSection
	 */
	public ToolSection getParentToolSection() {
		return parentToolSection;
	}

	/**
	 * @param parentToolSection
	 *            the parentToolSection to set
	 */
	public void setParentToolSection(ToolSection parentToolSection) {
		this.parentToolSection = parentToolSection;
	}

}
