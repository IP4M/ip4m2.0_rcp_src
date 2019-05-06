package com.sixthhosp.gcmpa.tools.parameters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import com.sixthhosp.gcmpa.xmlbeans.tool.ParamType;
import com.sixthhosp.gcmpa.xmlbeans.tool.TypeType;
import com.sixthhosp.gcmpa.xmlbeans.tool.TypeType.Enum;
import com.sixthhosp.gcmpa.xmlbeans.tool.ValidatorType;

import com.sixthhosp.gcmpa.tools.Utils;
import com.sixthhosp.gcmpa.tools.parameters.validators.ParameterValidateException;
import com.sixthhosp.gcmpa.tools.parameters.validators.Validator;

/**
 * 参数基类，先利用构造函数初始化数据模型，接着调用creatContents方法初始化控件
 * 
 * @author zhengzequn
 */
public abstract class ToolParameter {
	private String name;
	private Enum type;
	private String label;
	private String help;
	private boolean optional;
	private ValidatorType[] validatorTypes;

	private boolean isAlwaysValidate;
	private List<Validator> validators;
	private boolean HelpExist;

	private HashMap<String, String> dataModelMap;

	/**
	 * 最高层级的容器
	 */
	private Composite topComposite;

	/**
	 * 参数组件的父容器
	 */
	protected Composite composite;

	/**
	 * 参数组件的内容标签
	 */
	private Label labelWidget;

	/**
	 * 参数组件的错误提示
	 */
	private CLabel errorTip;

	/**
	 * 参数组件的帮助标签
	 */
	private Label helpWidget;

	/**
	 * 标签分割符
	 * 
	 * @param paramType
	 */
	private Label separatorLine;

	public ToolParameter() {
		validators = new ArrayList<Validator>();
	}

	public ToolParameter(ParamType paramType) {
		// TODO Auto-generated constructor stub
		this();
		setName(paramType.getName());
		setType(paramType.getType());
		setLabel(paramType.getLabel());
		setHelp(paramType.getHelp());
		setOptional(Boolean.parseBoolean(paramType.getOptional()));

		// 如果标签为空，则初始化为与name一致
		if (StringUtils.isBlank(label)) {
			label = name;
		}

		if (StringUtils.isBlank(help)) {
			help = "";
			setHelpExist(false);
		} else {
			setHelpExist(true);
		}

		// 初始化参数检查类的实例顺序容器
		validatorTypes = paramType.getValidatorArray();
		for (ValidatorType validatorType : validatorTypes) {
			Validator validator = Validator.init(validatorType);
			validators.add(validator);
		}

		isAlwaysValidate = false;
	}

	/**
	 * 创建参数所需的全部控件内容，包哈label,widget,validatorLabel,help，参数必须是gridlayout
	 * 
	 */
	public void creatContents(Composite parent, Composite topComposite) {
		setTopComposite(topComposite);

		composite = new Composite(parent, SWT.NONE);
		composite.setBackground(composite.getDisplay().getSystemColor(
				SWT.COLOR_WHITE));
		GridData compositeGridData = new GridData(GridData.FILL_HORIZONTAL);
		compositeGridData.verticalIndent = -5;
		composite.setLayoutData(compositeGridData);

		composite.setLayout(new GridLayout(1, false));

		FontData[] fontDatas = Display.getCurrent().getSystemFont()
				.getFontData();
		for (FontData fontData : fontDatas) {
			fontData.setStyle(SWT.BOLD);
		}
		Font font = new Font(Display.getCurrent(), fontDatas);

		labelWidget = new Label(composite, SWT.NONE);
		labelWidget.setBackground(labelWidget.getDisplay().getSystemColor(
				SWT.COLOR_WHITE));
		String labelText = label + "";
		labelText = WordUtils.wrap(labelText, 100);
		labelWidget.setText(labelText);
		labelWidget.setFont(font);

		creatParamWidget();

		if (isHelpExist()) {
			helpWidget = new Label(composite, SWT.NONE);
			helpWidget.setBackground(helpWidget.getDisplay().getSystemColor(
					SWT.COLOR_WHITE));
			String helpText = help;
			helpText = WordUtils.wrap(helpText, 100);
			helpWidget.setText(helpText);
			Color color = Display.getCurrent().getSystemColor(
					SWT.COLOR_DARK_GRAY);
			helpWidget.setForeground(color);
		}

		separatorLine = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		separatorLine.setLayoutData(gridData);

		font.dispose();

		// 添加鼠标点击事件，最高层父容器获取到焦点
		addMouseDownFoucusEvent();

		if (isAlwaysValidate) {
			startValidating();
			addValidatingEvent();
		}

	}

	/**
	 * 按下鼠标,将焦点设置到最高层容器
	 */
	protected void addMouseDownFoucusEvent() {
		composite.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				ToolParameter.this.topComposite.setFocus();
			}
		});

		labelWidget.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				ToolParameter.this.topComposite.setFocus();
			}
		});

		if (isHelpExist()) {
			helpWidget.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
					// TODO Auto-generated method stub
					ToolParameter.this.topComposite.setFocus();
				}
			});
		}

		separatorLine.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				ToolParameter.this.topComposite.setFocus();
			}
		});
	}

	/**
	 * 注册始终执行检查
	 * 
	 * @param inputs
	 */
	public void registerAlwaysValidating() {

		if (composite != null && !composite.isDisposed() && !isAlwaysValidate) {
			addValidatingEvent();
		}

		isAlwaysValidate = true;
	}

	/**
	 * 注册验证事件，用户每一次改变输入值的操作，都会进行输入值验证，适用于text，integer，float，select，data。
	 * 在控件创建之前注册
	 */
	public abstract void addValidatingEvent();

	/**
	 * 创建参数控件,即widget,其中composite可以利用父类的方法getPrimaryComposite()获得
	 * 
	 * @param composite
	 */
	protected abstract void creatParamWidget();

	/**
	 * 获取参数ＧＵＩ组件的最高层级控件
	 * 
	 * @return
	 */
	public abstract Control getParamWidgetPrimaryControl();

	/**
	 * 获取参数组件的输入,在控件被释放之前调用
	 * 
	 * @return
	 */
	public abstract String getInputString();

	/**
	 * 判断参数组件的输入是否为空,适用于text integer float select data类型的参数组件，在控件被释放之前调用
	 * 
	 * @return
	 */
	public abstract boolean isEmpty();

	/**
	 * 启动输入验证，在createContents之后调用， 返回true则通过验证，false反之，在控件被释放之前调用
	 */
	public boolean startValidating() {
		boolean b = true;
		boolean isNeedRefresh = false;
		if (errorTip != null && !errorTip.isDisposed()) {
			errorTip.dispose();
			isNeedRefresh = true;
		}
		try {
			validate();
		} catch (ParameterValidateException e) {
			// TODO Auto-generated catch block
			b = false;
			String msg = e.getMessage();

			errorTip = new CLabel(composite, SWT.LEFT);
			errorTip.setBackground(errorTip.getDisplay().getSystemColor(
					SWT.COLOR_WHITE));

			ImageData errorImageData = Display.getCurrent()
					.getSystemImage(SWT.ICON_ERROR).getImageData()
					.scaledTo(16, 16);
			final Image errorImage = ImageDescriptor.createFromImageData(
					errorImageData).createImage();

			errorTip.addDisposeListener(new DisposeListener() {
				@Override
				public void widgetDisposed(DisposeEvent e) {
					errorImage.dispose();
				}
			});

			errorTip.setImage(errorImage);
			errorTip.setText(msg);
			errorTip.setForeground(Display.getCurrent().getSystemColor(
					SWT.COLOR_RED));
			errorTip.moveBelow(this.getParamWidgetPrimaryControl());
			isNeedRefresh = true;

			// 添加鼠标按下，取得最高父容器获得焦点事件
			errorTip.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
					// TODO Auto-generated method stub
					ToolParameter.this.topComposite.setFocus();
				}
			});
		}
		if (isNeedRefresh) {
			topComposite.layout(new Control[] { labelWidget });
			Utils.reSize(topComposite);
		}
		return b;
	}

	/**
	 * 验证用户输入是否满足限定，在控件被释放之前调用
	 */
	public abstract void validate() throws ParameterValidateException;

	/**
	 * 利用 validators中的参数检查对象验证用户输入，检查不通过则会抛出参数验证异常，适用于text,integer,float类型的参数
	 */
	public void checkValidators() throws ParameterValidateException {
		for (Validator validator : validators) {
			String msg = validator.validate(getInputString());
			if (msg != null) {
				throw new ParameterValidateException(msg);
			}
		}
	}

	/**
	 * 保存参数信息，将控件的状态保存到成员变量，在控件被释放之前调用
	 * 
	 * @return
	 */
	public abstract void doSave();

	/**
	 * 将参数信息(成员变量信息)保存为XMLBeans对象，继承的子类必须复写该方法
	 * 
	 * @return
	 */
	public ParamType saveAsParamType() {
		ParamType newParamType = ParamType.Factory.newInstance();
		newParamType.setName(name);
		newParamType.setType(type);
		newParamType.setLabel(label);
		newParamType.setHelp(help);
		newParamType.setOptional(String.valueOf(optional));
		newParamType.setValidatorArray(validatorTypes);

		return newParamType;
	}

	/**
	 * 保存数据模型到成员变量，在控件被释放之前调用
	 */
	public void saveDataModel() {
		String key = this.getName();
		String value = this.getInputString();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(key, value);
		dataModelMap = map;
	}

	/**
	 * 验证保存的输入值是否满足限定
	 * 
	 * @return
	 */
	public abstract boolean validateSavedValue();

	/**
	 * 返回参数组件的数据模型
	 * 
	 * @return
	 */
	public HashMap<String, String> getDataModelMap() {
		return dataModelMap;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Enum getType() {
		return type;
	}

	public void setType(Enum type) {
		this.type = type;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getHelp() {
		return help;
	}

	public void setHelp(String help) {
		this.help = help;
	}

	public boolean isOptional() {
		return optional;
	}

	public void setOptional(boolean optional) {
		this.optional = optional;
	}

	public ValidatorType[] getValidatorTypes() {
		return validatorTypes;
	}

	public void setValidatorTypes(ValidatorType[] validatorTypes) {
		this.validatorTypes = validatorTypes;
	}

	public List<Validator> getValidators() {
		return validators;
	}

	public void setValidators(List<Validator> validators) {
		this.validators = validators;
	}

	public boolean isHelpExist() {
		return HelpExist;
	}

	public void setHelpExist(boolean helpExist) {
		HelpExist = helpExist;
	}

	public boolean isAlwaysValidate() {
		return isAlwaysValidate;
	}

	public void setAlwaysValidate(boolean isAlwaysValidate) {
		this.isAlwaysValidate = isAlwaysValidate;
	}

	public Label getLabelWidget() {
		return labelWidget;
	}

	public CLabel getErrorTip() {
		return errorTip;
	}

	public Label getHelpWidget() {
		return helpWidget;
	}

	public Label getSeparatorLine() {
		return separatorLine;
	}

	/**
	 * 释放控件
	 * 
	 */
	public void dispose() {
		composite.dispose();
	}

	/**
	 * 返回包含label,widget,help的父容器
	 * 
	 * @return
	 */
	public Composite getPrimaryComposite() {
		return composite;
	}

	/**
	 * 初始化参数类型实例
	 * 
	 * @param paramType
	 * @return
	 */
	public static ToolParameter init(ParamType paramType) {
		Enum type = paramType.getType();
		if (type == TypeType.TEXT) {
			return new TextToolParameter(paramType);
		} else if (type == TypeType.INTEGER) {
			return new IntegerToolParameter(paramType);
		} else if (type == TypeType.FLOAT) {
			return new FloatToolParameter(paramType);
		} else if (type == TypeType.DATA) {
			return new DataToolParameter(paramType);
		} else if (type == TypeType.SELECT) {
			return new SelectToolParameter(paramType);
		} else if (type == TypeType.BOOLEAN) {
			return new BooleanToolParameter(paramType);
		} else if (type == TypeType.HIDDEN) {
			return new HiddenToolParameter(paramType);
		}

		return null;
	}

	public Composite getTopComposite() {
		return topComposite;
	}

	public void setTopComposite(Composite topComposite) {
		this.topComposite = topComposite;
	}

}
