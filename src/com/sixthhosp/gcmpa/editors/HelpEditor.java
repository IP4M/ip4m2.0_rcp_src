package com.sixthhosp.gcmpa.editors;

import java.io.File;
import java.net.MalformedURLException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.sixthhosp.gcmpa.configs.ConfigFile;
import com.sixthhosp.gcmpa.editors.ei.HelpEditorInput;

public class HelpEditor extends EditorPart {

	public static final String ID = "com.sixthhosp.gcmpa.editors.helpeditor";

	private String helpPath;

	// �ؼ�����Դ
	// �༭��ͼƬ��Դ
	private Image image;
	// ������ؼ�
	private Browser browser;

	public HelpEditor() {
		super();
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
		// TODO Auto-generated method stub
		if (input instanceof HelpEditorInput) {
			HelpEditorInput helpEditorInput = (HelpEditorInput) input;
			setSite(site);
			setInput(helpEditorInput);
			setPartName(input.getName());

			// ���ñ༭��ҳ��ͼ��
			String path = ConfigFile.getProjectFolderPath()
					+ "/icons/editors/help_contents(1).gif";
			image = new Image(Display.getCurrent(), path);
			setTitleImage(image);
			//

			helpPath = helpEditorInput.getTool().getHelpPath();
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

	@Override
	public void createPartControl(Composite parent) {
		try {
			parent.setLayout(new FillLayout());
			File helpFile = new File(helpPath);
			String url = helpFile.toURI().toURL().toString();
			browser = new Browser(parent, SWT.NONE);
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
