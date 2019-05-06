package com.sixthhosp.gcmpa.tools.parameters.widgets;

import java.io.File;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;

/**
 * 选择文件GUI组件封装类
 * 
 * @author zhengzequn
 * 
 */
public class FileChooseWidget implements IWidgetMethods {

	public static String SEP = "__SEP__";
	private Composite composite;
	private Text text;
	private Button button;
	private InputValueChangeListener listener;
	private boolean multiple;

	private String absolutePath;
	protected String[] extensions;

	public FileChooseWidget(Composite parent, boolean multiple, String[] extension) {
		// TODO Auto-generated constructor stub
		this.extensions = extension;
		this.multiple = multiple;
		composite = new Composite(parent, SWT.NONE);
		// composite.setLayout(new GridLayout(7, true));
		composite.setLayout(new GridLayout(2, true));

		text = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
		// GridData data = new GridData(GridData.FILL_HORIZONTAL);
		GridData data = new GridData();
		// data.horizontalSpan = 6;
		data.widthHint = 400;
		text.setLayoutData(data);

		button = new Button(composite, SWT.PUSH);
		button.setText("Browse...");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!FileChooseWidget.this.multiple) {
					FileDialog fileDialog = new FileDialog(button.getShell(),
							SWT.OPEN);
					fileDialog.setText("Select A File");
					fileDialog.setFilterExtensions(extensions);
					String path = fileDialog.open();
					if (path != null) {
						absolutePath = path;
						setDefaultText(absolutePath);
						if (listener != null) {
							listener.InputValueChangeEvent();
						}
					}
				} else {
					FileDialog fileDialog = new FileDialog(button.getShell(),
							SWT.OPEN | SWT.MULTI);
					fileDialog.setText("Select Multiple Files");
					fileDialog.setFilterExtensions(extensions);
					String path = fileDialog.open();
					if (path != null) {
						String[] fileNames = fileDialog.getFileNames();
						String dir = fileDialog.getFilterPath();
						ArrayList<String> files = new ArrayList<String>();
						for (String fileName : fileNames) {
							files.add(new File(dir + File.separator + fileName)
									.getAbsolutePath());
						}

						absolutePath = StringUtils.join(files, SEP);
						String display = StringUtils.join(fileNames, " | ");
						setDefaultText(display);
						if (listener != null) {
							listener.InputValueChangeEvent();
						}
					}
				}

			}
		});
	}

	public boolean isEmpty() {
		if (StringUtils.isBlank(absolutePath)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 设置默认选中的File
	 * 
	 * @return
	 */
	public void setDefaultFile(File file) {
		this.absolutePath = file.getAbsolutePath();
		setDefaultText(absolutePath);
	}

	/**
	 * 设置Text控件显示的文本内容
	 * 
	 * @param string
	 */
	public void setDefaultText(String string) {
		if (string.length() > 500) {
			string = string.substring(0, 500);
			string = string + "...etc.";
		}

		text.setText(string);
	}

	public void setPath(String file) {
		this.absolutePath = file;
	}

	/**
	 * 获取输入文件的绝对路径
	 * 
	 * @return
	 */
	public String getAbsolutePath() {
		return absolutePath;
	}

	public Button getButton() {
		return button;
	}

	@Override
	public Control getControl() {
		return composite;
	}

	@Override
	public void dispose() {
		composite.dispose();
	}

	@Override
	public void setWidgetBackgroudColor(Color color) {
		composite.setBackground(color);
		text.setBackground(color);
		// button.setBackground(color);
	}

	public void addInputValueChangeListener(InputValueChangeListener listener) {
		this.listener = listener;
	}
}
