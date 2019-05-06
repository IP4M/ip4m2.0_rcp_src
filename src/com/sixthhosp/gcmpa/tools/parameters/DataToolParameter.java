package com.sixthhosp.gcmpa.tools.parameters;

import java.io.File;
import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import com.sixthhosp.gcmpa.xmlbeans.tool.ParamType;
import com.sixthhosp.gcmpa.tools.parameters.validators.ParameterValidateException;
import com.sixthhosp.gcmpa.tools.parameters.widgets.FileChooseWidget;
import com.sixthhosp.gcmpa.tools.parameters.widgets.InputValueChangeListener;

/**
 * 选择输入文件参数
 * 
 * @author zhengzequn
 * 
 */
public class DataToolParameter extends ToolParameter {

	private String format;

	/**
	 * 用于workflow模式，提供输入的模块或者文件在字典中的id值，用于取得上一个模块，在上一个模块的outputs中获取到对应的输入文件路径信息
	 */
	private String id;

	/**
	 * 用于workflow模式，提供输入的模块对应的输出部分的name值
	 */
	private String outputName;

	private String fileAbsPath;
	private boolean workflowModel;
	private boolean multiple;

	private FileChooseWidget fileChooseWidget;
	private Label workflowDataLabel;

	public DataToolParameter(ParamType paramType) {
		super(paramType);
		// TODO Auto-generated constructor stub
		setFormat(paramType.getFormat());
		setId(paramType.getId());
		setOutputName(paramType.getLastoutname());

		multiple = Boolean.parseBoolean(paramType.getMultiple());

		fileAbsPath = paramType.getPath();
		if (StringUtils.isBlank(fileAbsPath)) {
			fileAbsPath = "";
		}

		workflowModel = false;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "DataToolParameter";
	}

	@Override
	protected void creatParamWidget() {
		// TODO Auto-generated method stub
		if (!workflowModel) {
			Composite composite = getPrimaryComposite();

			String[] exts = format.split(",");
			ArrayList<String> newExts = new ArrayList<String>();
			for (String ext : exts) {
				String e = "*." + ext;
				newExts.add(e);
			}
			String ext = StringUtils.join(newExts, ";");
			String[] filterExts = new String[]{ext, "*.*"};
			

			fileChooseWidget = new FileChooseWidget(composite, this.multiple,
					filterExts);
			fileChooseWidget.setWidgetBackgroudColor(composite.getDisplay()
					.getSystemColor(SWT.COLOR_WHITE));

			if (!this.multiple) {
				if (StringUtils.isNotBlank(fileAbsPath)
						&& new File(fileAbsPath).exists()) {
					fileChooseWidget.setDefaultFile(new File(fileAbsPath));
				} else {
					fileAbsPath = "";
				}
			} else {
				if (StringUtils.isNotBlank(fileAbsPath)) {
					String[] files = StringUtils.splitByWholeSeparator(
							fileAbsPath, FileChooseWidget.SEP);
					boolean isMissing = false;
					ArrayList<String> list = new ArrayList<String>();
					for (String file : files) {
						File f = new File(file);
						list.add(f.getName());

						if (!f.exists()) {
							isMissing = true;
						}
					}

					if (isMissing) {
						fileAbsPath = "";
					} else {
						fileChooseWidget.setPath(fileAbsPath);
						fileChooseWidget.setDefaultText(StringUtils.join(list,
								" | "));
					}
				} else {
					fileAbsPath = "";
				}
			}

			Control control = fileChooseWidget.getControl();
			control.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
					// TODO Auto-generated method stub
					getTopComposite().setFocus();
				}
			});
		} else {
			Composite composite = getPrimaryComposite();
			workflowDataLabel = new Label(composite, SWT.NONE);
			String string = null;

			if (StringUtils.isBlank(fileAbsPath)) {
				string = "There is no data input provided ( " + format + " )";
			} else {
				String basename = FilenameUtils.getName(fileAbsPath);
				string = "Data input \"" + basename + "\"" + " ( " + format
						+ " )";
			}
			workflowDataLabel.setText(string);
			workflowDataLabel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
					// TODO Auto-generated method stub
					getTopComposite().setFocus();
				}
			});
		}
	}

	/**
	 * 替换文件输入类型参数的样式，用于工作流模式，在控件产生之前注册
	 */
	public void registerLabelDataWidget() {
		setWorkflowModel(true);
	}

	@Override
	public Control getParamWidgetPrimaryControl() {
		// TODO Auto-generated method stub
		if (!workflowModel) {
			return fileChooseWidget.getControl();
		} else {
			return workflowDataLabel;
		}
	}

	@Override
	public String getInputString() {
		// TODO Auto-generated method stub
		if (!workflowModel) {
			if (fileChooseWidget.isEmpty()) {
				return "";
			} else {
				return fileChooseWidget.getAbsolutePath();
			}
		} else {
			if (StringUtils.isBlank(fileAbsPath)) {
				return "";
			} else {
				return new File(fileAbsPath).getAbsolutePath();
			}
		}
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		if (fileChooseWidget != null
				&& !fileChooseWidget.getControl().isDisposed()) {
			return fileChooseWidget.isEmpty();
		} else {
			return StringUtils.isBlank(fileAbsPath);
		}
	}

	@Override
	public void validate() throws ParameterValidateException {
		// TODO Auto-generated method stub
		if (!isOptional() && isEmpty()) {
			if (!multiple) {
				throw new ParameterValidateException("A input file is required");
			} else {
				throw new ParameterValidateException(
						"Some input files are required");
			}
		}

		if (!workflowModel) {
			// 文件格式检查代码
			// if (!isEmpty()) {
			// String filePath = getInputString();
			// String inputFileFormat = Utils.snifferDataType(filePath);
			// if (!Utils.checkDataTypesCompatible(inputFileFormat, format)) {
			// throw new ParameterValidateException(
			// "File format error: \"" + format
			// + "\" file is required");
			// }
			// }
		}
	}

	@Override
	public boolean validateSavedValue() {
		// TODO Auto-generated method stub
		if (!isOptional() && StringUtils.isBlank(fileAbsPath)) {
			return false;
		}

		// 流程模式的检查是在连线时候进行检测，不需要在后面补上文件格式检查代码

		return true;
	}

	/**
	 * 清空DataToolParameter保存的输入值
	 */
	public void clearSavedVaule() {
		id = null;
		outputName = null;
		fileAbsPath = "";
	}

	@Override
	public void doSave() {
		// TODO Auto-generated method stub
		saveDataModel();
		if (!workflowModel) {
			fileAbsPath = getInputString();
		}
	}

	@Override
	public ParamType saveAsParamType() {
		// TODO Auto-generated method stub
		ParamType newParamType = super.saveAsParamType();

		newParamType.setFormat(format);
		newParamType.setMultiple(String.valueOf(multiple));

		if (isWorkflowModel()) {
			if (StringUtils.isNotBlank(id))
				newParamType.setId(id);
			if (StringUtils.isNotBlank(outputName))
				newParamType.setLastoutname(outputName);
		}

		if (StringUtils.isNotBlank(fileAbsPath)) {
			newParamType.setPath(fileAbsPath);
		}

		return newParamType;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getFileAbsPath() {
		return fileAbsPath;
	}

	public void setFileAbsPath(String fileAbsPath) {
		this.fileAbsPath = fileAbsPath;
	}

	public boolean isWorkflowModel() {
		return workflowModel;
	}

	public void setWorkflowModel(boolean workflowModel) {
		this.workflowModel = workflowModel;
	}

	public Label getWorkflowDataLabel() {
		return workflowDataLabel;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOutputName() {
		return outputName;
	}

	public void setOutputName(String outputName) {
		this.outputName = outputName;
	}

	@Override
	public void addValidatingEvent() {
		// TODO Auto-generated method stub
		if (!workflowModel) {
			fileChooseWidget
					.addInputValueChangeListener(new InputValueChangeListener() {

						@Override
						public void InputValueChangeEvent() {
							// TODO Auto-generated method stub
							startValidating();
						}
					});
		}
	}

}
