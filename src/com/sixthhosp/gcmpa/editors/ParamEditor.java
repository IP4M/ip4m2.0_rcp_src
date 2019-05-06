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
	 * �༭��ͼ��
	 */
	private Image image;

	/**
	 * �������ڣ�������
	 */
	private ScrolledComposite sc;

	private Composite topComposite;

	/**
	 * ģ����������������ֵ�
	 */
	private LinkedHashMap<String, Object> inputs;

	/**
	 * ģ��Ĺ���Ŀ¼����ParamEditor���캯���б�������ģ�����н�����ɾ��
	 */
	private String workingDirectoryPath;

	/**
	 * ģ�鹤��Ŀ¼File����
	 */
	private File workingDirectoryFile;

	/**
	 * ģ��������Ŀ¼
	 */
	private String outputDirectoryPath;

	/**
	 * ģ��������Ŀ¼File����
	 */
	private File outputDirectoryFile;

	/**
	 * �����ļ�����ģ�Ͷ���
	 */
	private ConfigureFiles configureFiles;

	/**
	 * ģ���������ģ�����
	 */
	private CommandLine commandLine;

	/**
	 * ģ��������������ֵ�
	 */
	private LinkedHashMap<String, ToolOutput> outputs;

	/**
	 * ģ��html����������
	 */
	private HtmlToolOutput htmlToolOutput;

	/**
	 * ����ģ�鰴ť
	 */
	private Button executeButton;

	/**
	 * ��ģ���ȡ��������ģ��
	 */
	private HashMap<String, Object> toolDataModel;

	/**
	 * ���տ�ִ�е������г���
	 */
	private String executeCommand;

	/**
	 * ������ʼʱ��
	 */
	private int[] time;

	public ParamEditor() {
		super();
		workingDirectoryPath = com.sixthhosp.gcmpa.configs.Utils
				.generateRandomToolWorkingDir();
		workingDirectoryFile = new File(workingDirectoryPath);

		// ��ʼ����Ա����
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

			// ���ñ༭��ҳ��ͼ��
			String path = tool.getIconPath();
			if (StringUtils.isBlank(path)) {
				path = ToolLabelProvider.getDefaultToolImagePath();
			}
			image = new Image(Display.getCurrent(), path);
			setTitleImage(image);
			//

			String xmlFilePath = tool.getFilePath();

			// ��ʼ��ģ��XML�ļ���������Ϣ

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
						// �����������Ŀ¼
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

						// �ڿؼ����ͷ�ǰ���������Ϣ
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
	 * ��������ͬʱ��������ӵ�����������С�
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
				// ʵʱ����������Ϣ
				DataFactory.saveTaskBox();
				XmlTask.minusJobsNum();
			}
		};
		Thread taskThread = new Thread(runnable);
		taskThread.start();
	}

	/**
	 * ����ģ��Ҫִ�е�������
	 */
	private void createCommandLine() {
		commandLine.velocityinterpolate(toolDataModel);
		executeCommand = commandLine.getExecuteCommandLine();
	}

	/**
	 * ���������ļ�������������ģ��
	 */
	private void createConfigFiles() {
		configureFiles.generateConfigFiles(toolDataModel);
		configureFiles.updateDataModel(toolDataModel);
	}

	/**
	 * ��ģ������������������ģ��
	 */
	private void storeToDataModel() {
		HashMap<String, Object> inputsDataModel = Utils
				.getDataModelFromInputs(inputs);
		toolDataModel.putAll(inputsDataModel);

		HashMap<String, String> outputsDataModel = Utils
				.getDataModelFromOutputs(outputs);
		toolDataModel.putAll(outputsDataModel);

		// ����Ĭ�ϱ�����Ϣ
		toolDataModel.put("WORKDIR", workingDirectoryPath);
		toolDataModel.put("OUTDIR", outputDirectoryPath);
		toolDataModel.put("SOFTDIR", ConfigFile.getSoftFolderPath());

	}

	/**
	 * �����û�������
	 */
	private void doSave() {
		Utils.doSaveForInputs(inputs);
		Utils.setOutDirectoryPathForOutputs(outputs, outputDirectoryPath);
		Utils.doSaveForOutputs(outputs);
	}

	/**
	 * ȷ���û��������Ƿ���ȷ���ڿؼ�����֮�����
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
	 * �����ָ���
	 */
	private void creatSeparatorLine() {
		SeparatorBar bar = new SeparatorBar();
		bar.creatContents(topComposite);
	}

	/**
	 * �����������벿��
	 */
	private void creatInputPart() {
		Utils.creatInputsContents(inputs, topComposite, topComposite);
	}

	/**
	 * ������������ͷ��
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
	 * @return the workingDirectoryFile ģ�鹤��Ŀ¼
	 */
	public File getWorkingDirectoryFile() {
		return workingDirectoryFile;
	}

	/**
	 * @return the outputDirectoryFile ģ�����Ŀ¼
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
