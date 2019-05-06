package com.sixthhosp.gcmpa.editors.ei;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.sixthhosp.gcmpa.tasks.XmlTask;

public class HtmlEditorInput implements IEditorInput {

	private XmlTask xmlTask;

	public HtmlEditorInput(XmlTask xmlTask) {
		this.xmlTask = xmlTask;
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
		return "Output: " + xmlTask.getTaskname();
	}

	@Override
	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getToolTipText() {
		return "Output: " + xmlTask.getTaskname() + " | "
				+ xmlTask.getStarttime();
	}

	/**
	 * @return the xmlTask
	 */
	public XmlTask getXmlTask() {
		return xmlTask;
	}

}
