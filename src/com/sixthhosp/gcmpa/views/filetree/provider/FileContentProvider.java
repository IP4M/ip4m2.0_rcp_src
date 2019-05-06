package com.sixthhosp.gcmpa.views.filetree.provider;

import java.io.File;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;


/**
 * 文件浏览器内容提供器
 * 
 * @author zhengzequn
 * 
 */
public class FileContentProvider implements ITreeContentProvider {

	@Override
	public Object[] getChildren(Object parent) {
		File file = (File) parent;
		return file.listFiles();
	}

	public Object[] getElements(Object inputElement) {
		File[] files = ((File) inputElement).listFiles();
		if (files == null)
			return new Object[0];
		return files;
	}

	@Override
	public Object getParent(Object element) {
		File file = (File) element;
		return file.getParentFile();
	}

	@Override
	public boolean hasChildren(Object parent) {
		File file = (File) parent;
		return file.isDirectory();
	}

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		
	}

}
