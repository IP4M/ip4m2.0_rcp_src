package com.sixthhosp.gcmpa.tools.parameters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.sixthhosp.gcmpa.xmlbeans.tool.OptionType;
import com.sixthhosp.gcmpa.xmlbeans.tool.ParamType;
import com.sixthhosp.gcmpa.xmlbeans.tool.ParamType.Display;
import com.sixthhosp.gcmpa.xmlbeans.tool.ParamType.Display.Enum;
import com.sixthhosp.gcmpa.xmlbeans.tool.ParamType.Options;
import com.sixthhosp.gcmpa.configs.ConfigFile;
import com.sixthhosp.gcmpa.tools.parameters.validators.ParameterValidateException;
import com.sixthhosp.gcmpa.tools.parameters.widgets.CheckBoxGroup;
import com.sixthhosp.gcmpa.tools.parameters.widgets.InputValueChangeListener;
import com.sixthhosp.gcmpa.tools.parameters.widgets.ObjectWithStat;
import com.sixthhosp.gcmpa.tools.parameters.widgets.RadioGroup;
import com.sixthhosp.gcmpa.tools.parameters.widgets.SelectionWidget;

/**
 * 选项类型参数
 * 
 * @author zhengzequn
 * 
 */
public class SelectToolParameter extends ToolParameter {

	private Enum display;
	private boolean multiple;
	private String separator;
	private Option[] options;

	private SelectionWidget selectionWidget;

	public SelectToolParameter(ParamType paramType) {
		super(paramType);
		// TODO Auto-generated constructor stub
		setDisplay(paramType.getDisplay());
		setMultiple(Boolean.parseBoolean(paramType.getMultiple()));
		setSeparator(paramType.getSeparator());

		// 对于允许复选的情况，复写optional的默认情况为true
		if (!paramType.isSetOptional()) {
			if (display == Display.DEFAULT && multiple) {
				setOptional(true);
			} else if (display == Display.CHECKBOXES) {
				setOptional(true);
			}
		}

		// 初始化选项
		if (paramType.isSetOptions()) {
			Options opts = paramType.getOptions();
			options = readOptionsFromLocFile(opts);
		} else {
			OptionType[] optionTypes = paramType.getOptionArray();
			options = new Option[optionTypes.length];
			for (int i = 0; i < options.length; i++) {
				options[i] = new Option(optionTypes[i]);
			}
		}

		boolean isEmpty = true;
		for (Option option : options) {
			if (option.isSelected()) {
				isEmpty = false;
			}
		}
		if ((display == Display.DEFAULT && !multiple)
				|| display == Display.RADIO) {
			if (isEmpty) {
				options[0].setSelected(true);
			}
		}
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "SelectToolParameter";
	}

	@Override
	protected void creatParamWidget() {
		// TODO Auto-generated method stub
		Composite composite = getPrimaryComposite();
		selectionWidget = SelectionWidget.init(composite, display, multiple, this.getName());
		selectionWidget.setInput(options);
		selectionWidget.setWidgetBackgroudColor(composite.getDisplay()
				.getSystemColor(SWT.COLOR_WHITE));
		if (selectionWidget instanceof CheckBoxGroup) {
			CheckBoxGroup checkBoxGroup = (CheckBoxGroup) selectionWidget;
			Control control = checkBoxGroup.getControl();
			Composite group = checkBoxGroup.getGroup();

			// 添加鼠标按下，取得最高父容器获得焦点事件
			control.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
					// TODO Auto-generated method stub
					getTopComposite().setFocus();
				}
			});

			group.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
					// TODO Auto-generated method stub
					getTopComposite().setFocus();
				}
			});
		} else if (selectionWidget instanceof RadioGroup) {
			RadioGroup radioGroup = (RadioGroup) selectionWidget;
			Composite group = radioGroup.getGroup();

			// 添加鼠标按下，取得最高父容器获得焦点事件
			group.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
					// TODO Auto-generated method stub
					getTopComposite().setFocus();
				}
			});
		}

	}

	@Override
	public Control getParamWidgetPrimaryControl() {
		// TODO Auto-generated method stub
		return selectionWidget.getControl();
	}

	@Override
	public String getInputString() {
		if (selectionWidget != null
				&& !selectionWidget.getControl().isDisposed()) {
			if (selectionWidget.isEmpty()) {
				return "";
			} else {
				Object[] selectedObjects = selectionWidget.getSelection();
				ArrayList<String> list = new ArrayList<String>();
				for (Object object : selectedObjects) {
					Option option = (Option) object;
					list.add(option.getValue());
				}
				String[] strings = list.toArray(new String[list.size()]);
				return StringUtils.join(strings, separator);
			}
		} else {
			ArrayList<String> list = new ArrayList<String>();
			for (Option option : options) {
				if (option.isSelected()) {
					list.add(option.getValue());
				}
			}
			if ((display == Display.DEFAULT && !multiple)
					|| display == Display.RADIO) {
				if (list.size() == 0) {
					list.add(options[0].getValue());
				} else if (list.size() > 1) {
					String string = list.get(list.size() - 1);
					list = new ArrayList<String>();
					list.add(string);
				}
			}
			String[] strings = list.toArray(new String[list.size()]);
			return StringUtils.join(strings, separator);
		}
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return selectionWidget.isEmpty();
	}

	@Override
	public void validate() throws ParameterValidateException {
		// TODO Auto-generated method stub
		if (!isOptional() && isEmpty()) {
			throw new ParameterValidateException(
					"No option was selected, but input is not optional.");
		}
	}

	@Override
	public boolean validateSavedValue() {
		// TODO Auto-generated method stub
		boolean isEmpty = true;
		for (Option option : options) {
			if (option.isSelected()) {
				isEmpty = false;
			}
		}
		if (!isOptional() && isEmpty) {
			return false;
		}
		return true;
	}

	@Override
	public void addValidatingEvent() {
		// TODO Auto-generated method stub
		selectionWidget
				.addInputValueChangeListener(new InputValueChangeListener() {
					@Override
					public void InputValueChangeEvent() {
						// TODO Auto-generated method stub
						startValidating();
					}
				});
	}

	@Override
	public void doSave() {
		// TODO Auto-generated method stub
		saveDataModel();
		if (selectionWidget != null
				&& !selectionWidget.getControl().isDisposed()) {
			ArrayList<ObjectWithStat> list = selectionWidget
					.getObjectsWithSelectionStat();
			ArrayList<Option> optionsList = new ArrayList<Option>();
			for (ObjectWithStat object : list) {
				Option option = (Option) object.getObject();
				option.setSelected(object.isSelected());
				optionsList.add(option);
			}
			options = optionsList.toArray(new Option[optionsList.size()]);
		}
	}

	@Override
	public ParamType saveAsParamType() {
		// TODO Auto-generated method stub
		ParamType newParamType = super.saveAsParamType();

		newParamType.setDisplay(display);
		newParamType.setMultiple(String.valueOf(multiple));
		newParamType.setSeparator(separator);

		OptionType[] optionTypes = new OptionType[options.length];
		for (int i = 0; i < options.length; i++) {
			optionTypes[i] = OptionType.Factory.newInstance();

			optionTypes[i].setSelected(String.valueOf(options[i].isSelected()));
			optionTypes[i].setValue(options[i].getValue());
			optionTypes[i].setStringValue(options[i].getText());
		}
		newParamType.setOptionArray(optionTypes);

		return newParamType;
	}

	public Enum getDisplay() {
		return display;
	}

	public void setDisplay(Enum display) {
		this.display = display;
	}

	public boolean isMultiple() {
		return multiple;
	}

	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	/**
	 * 读取options标签中的内容，得到Option[]数组信息,其中from_file属性对应的文件必须在目录loc下
	 * 
	 * @param options
	 * @return
	 */
	public static Option[] readOptionsFromLocFile(Options options) {
		ArrayList<Option> list = new ArrayList<Option>();

		String rel_file = options.getFromFile();
		String abs_file = ConfigFile.getLocFolderPath() + "/" + rel_file;

		int value_col = options.getValueCol();
		int text_col = options.getDisplayCol();

		File file = new File(abs_file);
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (!line.startsWith("#")) {
					String[] strings = line.split("\t");
					Option option = new Option(strings[text_col - 1],
							strings[value_col - 1], false);
					list.add(option);
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return list.toArray(new Option[list.size()]);
	}

}
