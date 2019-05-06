package com.sixthhosp.gcmpa.editors;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.sixthhosp.gcmpa.configs.ConfigFile;
import com.sixthhosp.gcmpa.editors.ei.LogEditorInput;
import com.sixthhosp.gcmpa.views.tasktree.actions.OpenFileUtils;

public class LogEditor extends EditorPart {

	public static final String ID = "com.sixthhosp.gcmpa.editors.logditor";

	private String cmdString;
	private String stderrString;
	private String stdoutString;

	// 控件和资源
	// 编辑器图片资源
	private Image image;

	/**
	 * 最高层面板，上一层为滚动面板
	 */
	private Composite composite;

	/**
	 * 打开结果目录按钮
	 */
	private Button button;

	/**
	 * 输出结果目录
	 */
	private String resultsFolderPath;

	public LogEditor() {
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
		if (input instanceof LogEditorInput) {
			LogEditorInput logEditorInput = (LogEditorInput) input;
			setSite(site);
			setInput(logEditorInput);
			setPartName(input.getName());

			// 设置编辑器页面图标
			String path = ConfigFile.getProjectFolderPath()
					+ "/icons/editors/logs.gif";
			image = new Image(Display.getCurrent(), path);
			setTitleImage(image);
			//

			cmdString = logEditorInput.getXmlTask().getCommandline();
			try {
				stdoutString = FileUtils.readFileToString(new File(
						logEditorInput.getXmlTask().getStdoutpath()));
				stderrString = FileUtils.readFileToString(new File(
						logEditorInput.getXmlTask().getStderrpath()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				stderrString = "";
				stdoutString = "";
			}

			resultsFolderPath = logEditorInput.getXmlTask().getOutputsfolderpath();
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
		parent.setLayout(new FillLayout());
		ScrolledComposite sc = new ScrolledComposite(parent, SWT.V_SCROLL
				| SWT.H_SCROLL);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		ScrollBar vBar = sc.getVerticalBar();
		vBar.setIncrement(15);

		FontData[] fontDatas = Display.getCurrent().getSystemFont()
				.getFontData();
		for (FontData fontData : fontDatas) {
			fontData.setStyle(SWT.BOLD);
		}
		final Font font = new Font(Display.getCurrent(), fontDatas);
		Color whiteColor = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);

		composite = new Composite(sc, SWT.NONE);
		sc.setContent(composite);
		composite.setBackground(whiteColor);
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginLeft = 5;
		gridLayout.marginTop = 10;
		composite.setLayout(gridLayout);

		Label cmdLabel = new Label(composite, SWT.LEFT);
		cmdLabel.setText("Task Command Line:");
		cmdLabel.setFont(font);
		cmdLabel.setBackground(whiteColor);

		Text cmdText = new Text(composite, SWT.BORDER | SWT.WRAP
				| SWT.READ_ONLY | SWT.V_SCROLL);
		GC gc = new GC(cmdText);
		FontMetrics fontMetrics = gc.getFontMetrics();
		int h = fontMetrics.getHeight();
		int w = fontMetrics.getAverageCharWidth();
		gc.dispose();

		cmdText.setBackground(whiteColor);
		cmdText.setText(cmdString);
		GridData cmdTextGridData = new GridData();
		cmdTextGridData.heightHint = h * 4;
		cmdTextGridData.widthHint = w * 150;
		cmdText.setLayoutData(cmdTextGridData);

		GridData boxGridData = new GridData();
		boxGridData.heightHint = h * 10;
		boxGridData.widthHint = w * 150;

		new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL)
				.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label stdoutLabel = new Label(composite, SWT.LEFT);
		stdoutLabel.setText("Task Standard Output:");
		stdoutLabel.setFont(font);
		stdoutLabel.setBackground(whiteColor);
		Text stdoutText = new Text(composite, SWT.BORDER | SWT.WRAP
				| SWT.READ_ONLY | SWT.V_SCROLL);
		stdoutText.setBackground(whiteColor);
		stdoutText.setText(stdoutString);
		stdoutText.setLayoutData(boxGridData);

		new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL)
				.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label stderrLabel = new Label(composite, SWT.LEFT);
		stderrLabel.setText("Task Standard Error Output:");
		stderrLabel.setFont(font);
		stderrLabel.setBackground(whiteColor);
		Text stderrText = new Text(composite, SWT.BORDER | SWT.WRAP
				| SWT.READ_ONLY | SWT.V_SCROLL);
		stderrText.setBackground(whiteColor);
		stderrText.setText(stderrString);
		stderrText.setLayoutData(boxGridData);

		new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL)
				.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		button = new Button(composite, SWT.NONE);
		button.setText("Click here to open the results folder");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					OpenFileUtils.opneFile(new File(resultsFolderPath));
				} catch (Exception e1) {
					MessageDialog.openError(button.getShell(), "Error",
							e1.getMessage());
				}
			}
		});

		composite.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				button.setFocus();
			}
		});
		cmdLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				button.setFocus();
			}
		});
		stderrLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				button.setFocus();
			}
		});
		stdoutLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				button.setFocus();
			}
		});
		sc.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		composite.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				font.dispose();
			}
		});
	}

	@Override
	public void setFocus() {
		button.setFocus();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
		image.dispose();
	}

}
