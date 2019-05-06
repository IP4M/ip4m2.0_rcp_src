package com.sixthhosp.gcmpa.editors;

import java.io.File;
import java.net.MalformedURLException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.sixthhosp.gcmpa.configs.ConfigFile;
import com.sixthhosp.gcmpa.editors.ei.HtmlEditorInput;

public class HtmlEditor extends EditorPart {

	public static final String ID = "com.sixthhosp.gcmpa.editors.htmleditor";

	private String outputPath;

	// 控件和资源
	// 编辑器图片资源
	private Image image;
	// 浏览器控件
	private Browser browser;

	public HtmlEditor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		if (input instanceof HtmlEditorInput) {
			HtmlEditorInput htmlEditorInput = (HtmlEditorInput) input;
			setSite(site);
			setInput(htmlEditorInput);
			setPartName(input.getName());

			// 设置编辑器页面图标
			String path = ConfigFile.getProjectFolderPath()
					+ "/icons/editors/element.gif";
			image = new Image(Display.getCurrent(), path);
			setTitleImage(image);
			//

			outputPath = htmlEditorInput.getXmlTask().getHtmloutpath();
		}

	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		try {

			parent.setLayout(new FillLayout());
			Composite composite = new Composite(parent, SWT.NONE);
			composite.setBackground(composite.getDisplay().getSystemColor(
					SWT.COLOR_WHITE));

			composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			GridLayout gridLayout = new GridLayout(1, false);
			gridLayout.marginHeight = 0;
			gridLayout.marginWidth = 0;
			composite.setLayout(gridLayout);

			final Image refImage = new Image(composite.getDisplay(),
					ConfigFile.getProjectFolderPath() + "/"
							+ "icons/browser/arrow_rotate_clockwise.png");
			final Image backImage = new Image(composite.getDisplay(),
					ConfigFile.getProjectFolderPath() + "/"
							+ "icons/browser/arrow_left.png");
			final Image forImage = new Image(composite.getDisplay(),
					ConfigFile.getProjectFolderPath() + "/"
							+ "icons/browser/arrow_right.png");

			ToolBar toolBar = new ToolBar(composite, SWT.FLAT);
			GridData gridData = new GridData();
			gridData.horizontalIndent = 5;
			toolBar.setLayoutData(gridData);

			// 刷新
			ToolItem refreshItem = new ToolItem(toolBar, SWT.PUSH);
			refreshItem.setImage(refImage);
			refreshItem.setToolTipText("Refresh Page");
			refreshItem.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					browser.refresh();
				}
			});

			// 后退
			ToolItem backItem = new ToolItem(toolBar, SWT.PUSH);
			backItem.setImage(backImage);
			backItem.setToolTipText("Backward");
			backItem.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					browser.back();
				}
			});

			// 前进
			ToolItem forwardItem = new ToolItem(toolBar, SWT.PUSH);
			forwardItem.setImage(forImage);
			forwardItem.setToolTipText("Forward");
			forwardItem.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					browser.forward();
				}
			});

			//
			toolBar.addDisposeListener(new DisposeListener() {
				@Override
				public void widgetDisposed(DisposeEvent e) {
					refImage.dispose();
					backImage.dispose();
					forImage.dispose();
				}
			});

			//
			Label sepLabel = new Label(composite, SWT.SEPARATOR
					| SWT.HORIZONTAL);
			sepLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			// sepLabel.setBackground(sepLabel.getDisplay().getSystemColor(SWT.COLOR_WHITE));

			File outputFile = new File(outputPath);
			String url = outputFile.toURI().toURL().toString();
			browser = new Browser(composite, SWT.NONE);
			browser.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
					| GridData.FILL_VERTICAL));
			browser.setUrl(url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		browser.setFocus();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
		image.dispose();
	}

}
