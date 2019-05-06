package com.sixthhosp.gcmpa.editors.paramui;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.sixthhosp.gcmpa.editors.HelpEditor;
import com.sixthhosp.gcmpa.editors.ei.HelpEditorInput;
import com.sixthhosp.gcmpa.tools.Tool;
import com.sixthhosp.gcmpa.xmlbeans.tool.ToolType;

/**
 * 模块参数设置界面的头部部分，显示模块的名称，描述信息，以及帮助信息的入口
 * 
 * @author zhengzequn
 * 
 */
public class ParamHeaderPart {

	private String toolName;
	private String toolDescription;
	private String toolVersion;
	private String helpPath;

	private RGB foregroundRgb;

	/**
	 * 最高层级的容器
	 */
	private Composite topComposite;

	/**
	 * Header的父容器
	 */
	private Composite composite;

	/**
	 * 头部标签
	 */
	private CLabel cLabel;

	/**
	 * 帮助链接
	 */
	private Link helpLink;
	private Tool tool;

	public ParamHeaderPart() {
		// TODO Auto-generated constructor stub
		foregroundRgb = new RGB(188, 52, 52);
	}

	public ParamHeaderPart(Tool tool) {
		// TODO Auto-generated constructor stub
		this();
		toolName = tool.getName();
		toolDescription = tool.getDescription();
		if (toolDescription == null) {
			toolDescription = "";
		}

		toolVersion = tool.getVersion();
		helpPath = tool.getHelpPath();
		this.tool = tool;
	}

	public ParamHeaderPart(ToolType toolType) {
		this();
		toolName = toolType.getName();
		toolName = toolName.replaceAll(" - \\d+$", "");
		toolDescription = toolType.getDescription();
		if (toolDescription == null) {
			toolDescription = "";
		}

		toolVersion = toolType.getVersion();
		helpPath = toolType.getHelppath();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Tool Header: " + toolName;
	}

	public void creatContents(Composite parent, Composite topComposite) {
		setTopComposite(topComposite);

		composite = new Composite(parent, SWT.NONE);
		GridData compositeGridData = new GridData(GridData.FILL_HORIZONTAL);
		composite.setLayoutData(compositeGridData);

		composite.setLayout(new GridLayout(2, false));
		composite.setBackground(composite.getDisplay().getSystemColor(
				SWT.COLOR_WHITE));

		final Color color = new Color(Display.getCurrent(), foregroundRgb);

		FontData[] fontDatas = Display.getCurrent().getSystemFont()
				.getFontData();
		for (FontData fontData : fontDatas) {
			fontData.setStyle(SWT.BOLD);
		}
		final Font font = new Font(Display.getCurrent(), fontDatas);

		// composite.setBackground(color);

		cLabel = new CLabel(composite, SWT.NONE);
		// cLabel.setBackground(color);
		cLabel.setForeground(color);
		cLabel.setBackground(cLabel.getDisplay()
				.getSystemColor(SWT.COLOR_WHITE));
		cLabel.setFont(font);

		String text = "Tool:  " + toolName + " " + toolDescription;
		// + " ( version " + toolVersion + " )";
		cLabel.setText(text);
		composite.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				// TODO Auto-generated method stub
				color.dispose();
				font.dispose();
			}
		});

		helpLink = new Link(composite, SWT.NONE);
		// helpLink.setBackground(color);
		helpLink.setForeground(color);
		helpLink.setBackground(helpLink.getDisplay().getSystemColor(
				SWT.COLOR_WHITE));
		helpLink.setFont(font);
		String helpString = "-  <A>Help Link</A>";
		helpLink.setText(helpString);
		helpLink.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				openHelp();
			}
		});

		// 添加鼠标点击事件，最高层父容器获取到焦点
		addMouseDownFoucusEvent();

	}

	public void openHelp() {

		String helpPath = tool.getHelpPath();
		if (StringUtils.isNotBlank(helpPath) && new File(helpPath).exists()) {
			HelpEditorInput helpEditorInput = new HelpEditorInput(tool);
			IWorkbenchPage workbenchPage = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage();
			IEditorPart editor = workbenchPage.findEditor(helpEditorInput);

			if (editor != null) {
				workbenchPage.bringToTop(editor);
			} else {
				try {
					workbenchPage.openEditor(helpEditorInput, HelpEditor.ID);
				} catch (PartInitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			MessageDialog.openError(composite.getShell(), tool.getName(),
					"No Tool Help Document");
		}

	}

	private void addMouseDownFoucusEvent() {
		// TODO Auto-generated method stub
		composite.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				ParamHeaderPart.this.topComposite.setFocus();
			}
		});

		cLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				ParamHeaderPart.this.topComposite.setFocus();
			}
		});

		helpLink.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				ParamHeaderPart.this.topComposite.setFocus();
			}
		});
	}

	public String getToolName() {
		return toolName;
	}

	public void setToolName(String toolName) {
		this.toolName = toolName;
	}

	public String getToolDescription() {
		return toolDescription;
	}

	public void setToolDescription(String toolDescription) {
		this.toolDescription = toolDescription;
	}

	public String getToolVersion() {
		return toolVersion;
	}

	public void setToolVersion(String toolVersion) {
		this.toolVersion = toolVersion;
	}

	public String getHelpPath() {
		return helpPath;
	}

	public void setHelpPath(String helpPath) {
		this.helpPath = helpPath;
	}

	public RGB getBackgroundRgb() {
		return foregroundRgb;
	}

	public void setBackgroundRgb(RGB backgroundRgb) {
		this.foregroundRgb = backgroundRgb;
	}

	public Composite getComposite() {
		return composite;
	}

	public void setComposite(Composite composite) {
		this.composite = composite;
	}

	public CLabel getcLabel() {
		return cLabel;
	}

	public void setcLabel(CLabel cLabel) {
		this.cLabel = cLabel;
	}

	public Composite getTopComposite() {
		return topComposite;
	}

	public void setTopComposite(Composite topComposite) {
		this.topComposite = topComposite;
	}

	public Link getHelpLink() {
		return helpLink;
	}

	public void setHelpLink(Link helpLink) {
		this.helpLink = helpLink;
	}
}
