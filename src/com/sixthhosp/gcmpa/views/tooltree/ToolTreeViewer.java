package com.sixthhosp.gcmpa.views.tooltree;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;

import com.sixthhosp.gcmpa.data.DataFactory;
import com.sixthhosp.gcmpa.views.tooltree.provider.ToolContentProvider;
import com.sixthhosp.gcmpa.views.tooltree.provider.ToolLabelProvider;

/**
 * ģ�������
 * 
 * @author zhengzequn
 * 
 */
public class ToolTreeViewer extends FilteredTree {

	private TreeViewer toolTreeViewer;

	public ToolTreeViewer(Composite parent, int treeStyle,
			PatternFilter filter, boolean useNewLook) {
		super(parent, treeStyle, filter, useNewLook);
		// TODO Auto-generated constructor stub

		// ��ȡtree viewer
		toolTreeViewer = this.getViewer();

		// ��������
		toolTreeViewer.setContentProvider(new ToolContentProvider());

		// ���ñ�ǩ
		toolTreeViewer.setLabelProvider(new ToolLabelProvider());

		toolTreeViewer.setUseHashlookup(true);

		// ��ʼ��Tool Panel������
		toolTreeViewer.setInput(DataFactory.getToolBox());

	}

	public TreeViewer getToolTreeViewer() {
		return toolTreeViewer;
	}

}
