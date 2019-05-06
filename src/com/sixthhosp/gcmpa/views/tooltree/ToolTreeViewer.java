package com.sixthhosp.gcmpa.views.tooltree;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;

import com.sixthhosp.gcmpa.data.DataFactory;
import com.sixthhosp.gcmpa.views.tooltree.provider.ToolContentProvider;
import com.sixthhosp.gcmpa.views.tooltree.provider.ToolLabelProvider;

/**
 * 模块分类树
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

		// 获取tree viewer
		toolTreeViewer = this.getViewer();

		// 设置内容
		toolTreeViewer.setContentProvider(new ToolContentProvider());

		// 设置标签
		toolTreeViewer.setLabelProvider(new ToolLabelProvider());

		toolTreeViewer.setUseHashlookup(true);

		// 初始化Tool Panel的输入
		toolTreeViewer.setInput(DataFactory.getToolBox());

	}

	public TreeViewer getToolTreeViewer() {
		return toolTreeViewer;
	}

}
