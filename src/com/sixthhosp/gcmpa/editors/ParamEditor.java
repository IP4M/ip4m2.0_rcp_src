package com.sixthhosp.gcmpa.editors;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.xmlbeans.XmlException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;

import com.sixthhosp.gcmpa.configs.ConfigFile;
import com.sixthhosp.gcmpa.data.DataFactory;
import com.sixthhosp.gcmpa.editors.ei.ParamEditorInput;
import com.sixthhosp.gcmpa.editors.paramui.ParamHeaderPart;
import com.sixthhosp.gcmpa.editors.paramui.SeparatorBar;
import com.sixthhosp.gcmpa.tasks.XmlTask;
import com.sixthhosp.gcmpa.tools.Tool;
import com.sixthhosp.gcmpa.tools.Utils;
import com.sixthhosp.gcmpa.tools.misc.CommandLine;
import com.sixthhosp.gcmpa.tools.misc.ConfigureFiles;
import com.sixthhosp.gcmpa.tools.parameters.HtmlToolOutput;
import com.sixthhosp.gcmpa.tools.parameters.ToolOutput;
import com.sixthhosp.gcmpa.views.tooltree.provider.ToolLabelProvider;
import com.sixthhosp.gcmpa.xmlbeans.tool.InputsType;
import com.sixthhosp.gcmpa.xmlbeans.tool.OutputsType;
import com.sixthhosp.gcmpa.xmlbeans.tool.ToolDocument;
import com.sixthhosp.gcmpa.xmlbeans.tool.ToolType;
import com.sixthhosp.gcmpa.xmlbeans.tool.ToolType.Command;
import com.sixthhosp.gcmpa.xmlbeans.tool.ToolType.Configfiles;

public class ParamEditor extends EditorPart {

	public static final String ID = "com.sixthhosp.gcmpa.editors.parameditor";

	private Tool tool;

	/**
	 * 编辑器图标
	 */
	private Image image;

	/**
	 * 滚动窗口，父容器
	 */
	private ScrolledComposite sc;

	private Composite topComposite;

	/**
	 * 模块输入参数数有序字典
	 */
	private LinkedHashMap<String, Object> inputs;

	/**
	 * 模块的工作目录，在ParamEditor构造函数中被创建，模块运行结束后被删除
	 */
	private String workingDirectoryPath;

	/**
	 * 模块工作目录File对象
	 */
	private File workingDirectoryFile;

	/**
	 * 模块输出结果目录
	 */
	private String outputDirectoryPath;

	/**
	 * 模块输出结果目录File对象
	 */
	private File outputDirectoryFile;

	/**
	 * 配置文件数据模型对象
	 */
	private ConfigureFiles configureFiles;

	/**
	 * 模块的命令行模板对象
	 */
	private CommandLine commandLine;

	/**
	 * 模块输出参数有序字典
	 */
	private LinkedHashMap<String, ToolOutput> outputs;

	/**
	 * 模块html输出结果对象
	 */
	private HtmlToolOutput htmlToolOutput;

	/**
	 * 运行模块按钮
	 */
	private Button executeButton;

	/**
	 * 从模块获取到的数据模型
	 */
	private HashMap<String, Object> toolDataModel;

	/**
	 * 最终可执行的命令行程序
	 */
	private String executeCommand;

	/**
	 * 任务起始时间
	 */
	private int[] time;

	public ParamEditor() {
		super();
		workingDirectoryPath = com.sixthhosp.gcmpa.configs.Utils
				.generateRandomToolWorkingDir();
		workingDirectoryFile = new File(workingDirectoryPath);

		// 初始化成员对象
		toolDataModel = new HashMap<String, Object>();
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
		if (input instanceof ParamEditorInput) {
			ParamEditorInput paramEditorInput = (ParamEditorInput) input;
			setSite(site);
			setInput(paramEditorInput);
			setPartName(input.getName());
			tool = paramEditorInput.getTool();

			// 设置编辑器页面图标
			String path = tool.getIconPath();
			if (StringUtils.isBlank(path)) {
				path = ToolLabelProvider.getDefaultToolImagePath();
			}
			image = new Image(Display.getCurrent(), path);
			setTitleImage(image);
			//

			String xmlFilePath = tool.getFilePath();

			// 初始化模块XML文件的配置信息

			ToolDocument toolDocument;
			try {
				File toolXMLFile = new File(xmlFilePath);
				toolDocument = ToolDocument.Factory.parse(toolXMLFile);
				ToolType toolType = toolDocument.getTool();
				InputsType inputsType = toolType.getInputs();
				OutputsType outputsType = toolType.getOutputs();
				Command command = toolType.getCommand();
				Configfiles configfiles = toolType.getConfigfiles();

				inputs = Utils.parseInputsType(inputsType);
				outputs = Utils.parseOutputsType(outputsType);
				htmlToolOutput = Utils.getHtmlToolOutput(outputs);
				configureFiles = new ConfigureFiles(configfiles,
						workingDirectoryPath);
				commandLine = new CommandLine(command);
				Utils.registerAlwaysValidatingForInputs(inputs);

			} catch (XmlException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

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
		sc = new ScrolledComposite(parent, SWT.V_SCROLL | SWT.H_SCROLL);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		ScrollBar vBar = sc.getVerticalBar();
		vBar.setIncrement(15);

		topComposite = new Composite(sc, SWT.NONE);
		sc.setContent(topComposite);
		topComposite.setLayout(new GridLayout(1, false));

		topComposite.setBackground(topComposite.getDisplay().getSystemColor(
				SWT.COLOR_WHITE));

		creatHeaderPart();
		creatInputPart();
		creatSeparatorLine();
		creatButtons();

		//
		topComposite.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				topComposite.setFocus();
			}
		});
		sc.setMinSize(topComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	/**
	 * 
	 */
	private void creatButtons() {
		executeButton = new Button(topComposite, SWT.PUSH);
		executeButton.setText("Execute");
		executeButton.setToolTipText("Execute the tool");

		FontData[] fontDatas = executeButton.getFont().getFontData();
		for (FontData fontData : fontDatas) {
			fontData.setHeight((int) (fontData.getHeight() * 1.3));
			fontData.setStyle(SWT.BOLD);
		}
		final Font font = new Font(Display.getCurrent(), fontDatas);
		executeButton.setFont(font);
		executeButton.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				font.dispose();
			}
		});
		GridData executeGridData = new GridData();
		executeGridData.widthHint = 300;
		executeGridData.horizontalIndent = 5;
		executeGridData.verticalIndent = 5;
		// executeGridData.heightHint = 40;
		executeButton.setLayoutData(executeGridData);

		registerButtonEvent();
	}

	private void registerButtonEvent() {
		executeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean isInputsOK = checkValidate();
				if (isInputsOK) {
					if (MessageDialog.openQuestion(executeButton.getShell(),
							"Confirm", "Do you want to run this tool?")) {
						IWorkbenchPage workbenchPage = PlatformUI
								.getWorkbench().getActiveWorkbenchWindow()
								.getActivePage();
						// 设置输出工作目录
						time = com.sixthhosp.gcmpa.configs.Utils
								.getCurrentTime3();
						String t = time[0] + "." + time[1] + "." + time[2]
								+ "_" + time[3] + "." + time[4] + "." + time[5];
						outputDirectoryPath = ConfigFile
								.getTaskResultsFolderPath()
								+ "/"
								+ tool.getName().replaceAll("\\s+", "_")
								+ "_"
								+ t;
						outputDirectoryFile = new File(outputDirectoryPath);

						// 在控件被释放前保存参数信息
						doSave();
						workbenchPage.closeEditor(ParamEditor.this, false);
						runToolTask();
					}
				} else {
					MessageDialog.openError(executeButton.getShell(),
							"Inputs Validation Error",
							"Please Checking The Inputs!");
				}
			}
		});

	}

	/**
	 * 运行任务，同时将任务添加到任务管理器中。
	 */
	protected void runToolTask() {
		storeToDataModel();
		createConfigFiles();
		createCommandLine();
		final XmlTask xmlTask = new XmlTask(this);
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				XmlTask.addJobsNum();
				DataFactory.getTaskBox().add(xmlTask);
				final TableViewer tableViewer = xmlTask.getTableViewer();
				tableViewer.getTable().getDisplay().asyncExec(new Runnable() {
					public void run() {
						if (tableViewer != null
								&& !tableViewer.getTable().isDisposed()) {
							tableViewer.refresh();
							tableViewer.setSelection(new StructuredSelection(
									xmlTask), true);
						}
					}
				});
				xmlTask.start();
				// 实时保存任务信息
				DataFactory.saveTaskBox();
				XmlTask.minusJobsNum();
			}
		};
		Thread taskThread = new Thread(runnable);
		taskThread.start();
	}

	/**
	 * 创建模块要执行的命令行
	 */
	private void createCommandLine() {
		commandLine.velocityinterpolate(toolDataModel);
		executeCommand = commandLine.getExecuteCommandLine();
	}

	/**
	 * 创建配置文件，并更新数据模型
	 */
	private void createConfigFiles() {
		configureFiles.generateConfigFiles(toolDataModel);
		configureFiles.updateDataModel(toolDataModel);
	}

	/**
	 * 将模块的输入参数保存数据模型
	 */
	private void storeToDataModel() {
		HashMap<String, Object> inputsDataModel = Utils
				.getDataModelFromInputs(inputs);
		toolDataModel.putAll(inputsDataModel);

		HashMap<String, String> outputsDataModel = Utils
				.getDataModelFromOutputs(outputs);
		toolDataModel.putAll(outputsDataModel);

		// 保存默认变量信息
		toolDataModel.put("WORKDIR", workingDirectoryPath);
		toolDataModel.put("OUTDIR", outputDirectoryPath);
		toolDataModel.put("SOFTDIR", ConfigFile.getSoftFolderPath());

	}

	/**
	 * 保存用户的输入
	 */
	private void doSave() {
		Utils.doSaveForInputs(inputs);
		Utils.setOutDirectoryPathForOutputs(outputs, outputDirectoryPath);
		Utils.doSaveForOutputs(outputs);
	}

	/**
	 * 确认用户的输入是否正确，在控件创建之后调用
	 * 
	 * @return
	 */
	private boolean checkValidate() {
		boolean b = true;
		if (!Utils.startValidatingForInputs(inputs)) {
			b = false;
		}
		return b;
	}

	/**
	 * 创建分割线
	 */
	private void creatSeparatorLine() {
		SeparatorBar bar = new SeparatorBar();
		bar.creatContents(topComposite);
	}

	/**
	 * 创建参数输入部分
	 */
	private void creatInputPart() {
		Utils.creatInputsContents(inputs, topComposite, topComposite);
	}

	/**
	 * 创建参数界面头部
	 */
	private void creatHeaderPart() {
		ParamHeaderPart headerPart = new ParamHeaderPart(tool);
		headerPart.creatContents(topComposite, topComposite);
	}

	public ToolType saveAsToolType() {
		ToolType toolType = ToolType.Factory.newInstance();
		toolType.setId(tool.getId());
		toolType.setName(tool.getName());
		toolType.setVersion(tool.getVersion());
		toolType.setDescription(tool.getDescription());
		toolType.setIconpath(tool.getIconPath());
		toolType.setHelppath(tool.getHelpPath());

		Command command = this.commandLine.saveAsCommand();
		toolType.setCommand(command);

		InputsType inputsType = Utils.getInputsTypeFromInputs(inputs);
		toolType.setInputs(inputsType);

		OutputsType outputsType = Utils.getOutputsTypeFromOutputs(outputs);
		toolType.setOutputs(outputsType);

		Configfiles configfiles = configureFiles.saveAsConfigfiles();
		toolType.setConfigfiles(configfiles);

		return toolType;
	}

	@Override
	public void setFocus() {
		topComposite.setFocus();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
		image.dispose();
	}

	/**
	 * @return the workingDirectoryFile 模块工作目录
	 */
	public File getWorkingDirectoryFile() {
		return workingDirectoryFile;
	}

	/**
	 * @return the outputDirectoryFile 模块输出目录
	 */
	public File getOutputDirectoryFile() {
		return outputDirectoryFile;
	}

	/**
	 * @return the tool
	 */
	public Tool getTool() {
		return tool;
	}

	/**
	 * @return the executeCommand
	 */
	public String getExecuteCommand() {
		return executeCommand;
	}

	/**
	 * @return the htmlToolOutput
	 */
	public HtmlToolOutput getHtmlToolOutput() {
		return htmlToolOutput;
	}

	/**
	 * @return the time
	 */
	public int[] getTime() {
		return time;
	}

}
