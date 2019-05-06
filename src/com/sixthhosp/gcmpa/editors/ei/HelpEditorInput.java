package com.sixthhosp.gcmpa.editors.ei;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.sixthhosp.gcmpa.tools.Tool;

public class HelpEditorInput implements IEditorInput {

	private Tool tool;

	public HelpEditorInput(Tool tool) {
		this.tool = tool;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Help: " + tool.getName();
	}

	@Override
	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getToolTipText() {
		String name = tool.getName();
		String description = tool.getDescription();
		if (description == null) {
			description = "";
			return "Help: " + name;
		} else {
			return "Help: " + name + " " + description;
		}
	}

	public Tool getTool() {
		return tool;
	}

}
