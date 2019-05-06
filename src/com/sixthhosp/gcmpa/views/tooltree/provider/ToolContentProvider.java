package com.sixthhosp.gcmpa.views.tooltree.provider;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.sixthhosp.gcmpa.tools.Tool;
import com.sixthhosp.gcmpa.tools.ToolBox;
import com.sixthhosp.gcmpa.tools.ToolSection;
import com.sixthhosp.gcmpa.tools.ToolSectionLabel;

/**
 * 模块分类树的内容提供器
 * 
 * @author zhengzequn
 * 
 */
public class ToolContentProvider implements ITreeContentProvider {

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object[] getElements(Object inputElement) {
		// TODO Auto-generated method stub
		ToolBox toolBox = (ToolBox) inputElement;
		return toolBox.getToolSections();
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		// TODO Auto-generated method stub
		if (parentElement instanceof ToolSection) {
			ToolSection toolSection = (ToolSection) parentElement;
			return toolSection.getChildren();
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		// TODO Auto-generated method stub
		if (element instanceof Tool) {
			Tool tool = (Tool) element;
			return tool.getToolSection();
		} else if (element instanceof ToolSectionLabel) {
			ToolSectionLabel toolSectionLabel = (ToolSectionLabel) element;
			return toolSectionLabel.getToolSection();
		} else if ( element instanceof ToolSection){
			ToolSection toolSection = (ToolSection) element;
			return toolSection.getParentToolSection();
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		// TODO Auto-generated method stub
		if (element instanceof ToolSection) {
			return true;
		}
		return false;
	}

}
