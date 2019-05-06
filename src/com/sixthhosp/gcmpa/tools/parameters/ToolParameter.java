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
 * �������࣬�����ù��캯����ʼ������ģ�ͣ����ŵ���creatContents������ʼ���ؼ�
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
	 * ��߲㼶������
	 */
	private Composite topComposite;

	/**
	 * ��������ĸ�����
	 */
	protected Composite composite;

	/**
	 * ������������ݱ�ǩ
	 */
	private Label labelWidget;

	/**
	 * ��������Ĵ�����ʾ
	 */
	private CLabel errorTip;

	/**
	 * ��������İ�����ǩ
	 */
	private Label helpWidget;

	/**
	 * ��ǩ�ָ��
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

		// �����ǩΪ�գ����ʼ��Ϊ��nameһ��
		if (StringUtils.isBlank(label)) {
			label = name;
		}

		if (StringUtils.isBlank(help)) {
			help = "";
			setHelpExist(false);
		} else {
			setHelpExist(true);
		}

		// ��ʼ������������ʵ��˳������
		validatorTypes = paramType.getValidatorArray();
		for (ValidatorType validatorType : validatorTypes) {
			Validator validator = Validator.init(validatorType);
			validators.add(validator);
		}

		isAlwaysValidate = false;
	}

	/**
	 * �������������ȫ���ؼ����ݣ�����label,widget,validatorLabel,help������������gridlayout
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

		// ���������¼�����߲㸸������ȡ������
		addMouseDownFoucusEvent();

		if (isAlwaysValidate) {
			startValidating();
			addValidatingEvent();
		}

	}

	/**
	 * �������,���������õ���߲�����
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
	 * ע��ʼ��ִ�м��
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
	 * ע����֤�¼����û�ÿһ�θı�����ֵ�Ĳ����������������ֵ��֤��������text��integer��float��select��data��
	 * �ڿؼ�����֮ǰע��
	 */
	public abstract void addValidatingEvent();

	/**
	 * ���������ؼ�,��widget,����composite�������ø���ķ���getPrimaryComposite()���
	 * 
	 * @param composite
	 */
	protected abstract void creatParamWidget();

	/**
	 * ��ȡ�����ǣգ��������߲㼶�ؼ�
	 * 
	 * @return
	 */
	public abstract Control getParamWidgetPrimaryControl();

	/**
	 * ��ȡ�������������,�ڿؼ����ͷ�֮ǰ����
	 * 
	 * @return
	 */
	public abstract String getInputString();

	/**
	 * �жϲ�������������Ƿ�Ϊ��,������text integer float select data���͵Ĳ���������ڿؼ����ͷ�֮ǰ����
	 * 
	 * @return
	 */
	public abstract boolean isEmpty();

	/**
	 * ����������֤����createContents֮����ã� ����true��ͨ����֤��false��֮���ڿؼ����ͷ�֮ǰ����
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

			// �����갴�£�ȡ����߸�������ý����¼�
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
	 * ��֤�û������Ƿ������޶����ڿؼ����ͷ�֮ǰ����
	 */
	public abstract void validate() throws ParameterValidateException;

	/**
	 * ���� validators�еĲ�����������֤�û����룬��鲻ͨ������׳�������֤�쳣��������text,integer,float���͵Ĳ���
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
	 * ���������Ϣ�����ؼ���״̬���浽��Ա�������ڿؼ����ͷ�֮ǰ����
	 * 
	 * @return
	 */
	public abstract void doSave();

	/**
	 * ��������Ϣ(��Ա������Ϣ)����ΪXMLBeans���󣬼̳е�������븴д�÷���
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
	 * ��������ģ�͵���Ա�������ڿؼ����ͷ�֮ǰ����
	 */
	public void saveDataModel() {
		String key = this.getName();
		String value = this.getInputString();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(key, value);
		dataModelMap = map;
	}

	/**
	 * ��֤���������ֵ�Ƿ������޶�
	 * 
	 * @return
	 */
	public abstract boolean validateSavedValue();

	/**
	 * ���ز������������ģ��
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
	 * �ͷſؼ�
	 * 
	 */
	public void dispose() {
		composite.dispose();
	}

	/**
	 * ���ذ���label,widget,help�ĸ�����
	 * 
	 * @return
	 */
	public Composite getPrimaryComposite() {
		return composite;
	}

	/**
	 * ��ʼ����������ʵ��
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
