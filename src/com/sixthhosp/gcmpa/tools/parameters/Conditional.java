package com.sixthhosp.gcmpa.tools.parameters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import com.sixthhosp.gcmpa.xmlbeans.tool.ConditionalType;
import com.sixthhosp.gcmpa.xmlbeans.tool.ConditionalType.Param;
import com.sixthhosp.gcmpa.xmlbeans.tool.OptionType;
import com.sixthhosp.gcmpa.xmlbeans.tool.WhenType;

import com.sixthhosp.gcmpa.tools.Utils;
import com.sixthhosp.gcmpa.tools.parameters.provider.SelectWidgetProvider;
import com.sixthhosp.gcmpa.tools.parameters.widgets.ComboGroup;
import com.sixthhosp.gcmpa.tools.parameters.widgets.ObjectWithStat;

/**
 * �������Ͳ���
 * 
 * @author zhengzequn
 * 
 */
public class Conditional {

	private String name;
	private Param param;
	private String paramName;
	private String paramLabel;
	private String paramHelp;

	private boolean isClearDataToolParameter;
	private int oldIndex;
	private boolean paramHelpExist;
	private Option[] paramOptions;
	private ConditionalWhen[] conditionalWhens;

	private HashMap<String, Object> dataModelMap;

	/**
	 * ��߲㼶������
	 */
	private Composite topComposite;

	/**
	 * �������Ͳ�������ĸ�����
	 */
	private Composite mainComposite;

	/**
	 * ������������Ĳ�����ǩ
	 */
	private Label paramLabelWidget;

	/**
	 * �������������������
	 */
	private ComboGroup comboGroup;

	/**
	 * ������������İ�����ǩ
	 */
	private Label paramHelpWidget;
	private Group group;
	private Label separatorLine;

	public Conditional(ConditionalType conditionalType) {
		// TODO Auto-generated constructor stub
		setName(conditionalType.getName());
		setParam(conditionalType.getParam());
		setParamName(param.getName());
		setParamLabel(param.getLabel());
		setParamHelp(param.getHelp());

		if (StringUtils.isBlank(paramLabel)) {
			paramLabel = paramName;
		}

		if (StringUtils.isBlank(paramHelp)) {
			paramHelp = "";
			setParamHelpExist(false);
		} else {
			setParamHelpExist(true);
		}

		OptionType[] paramOptionTypes = param.getOptionArray();
		paramOptions = new Option[paramOptionTypes.length];
		for (int i = 0; i < paramOptions.length; i++) {
			paramOptions[i] = new Option(paramOptionTypes[i]);
		}

		ArrayList<Option> list = new ArrayList<Option>();
		for (int i = 0; i < paramOptions.length; i++) {
			if (paramOptions[i].isSelected()) {
				list.add(paramOptions[i]);
			}
		}
		if (list.size() == 0) {
			paramOptions[0].setSelected(true);
		} else if (list.size() > 1) {
			for (int i = 0; i < list.size() - 1; i++) {
				list.get(i).setSelected(false);
			}
		}

		conditionalWhens = new ConditionalWhen[paramOptions.length];
		for (int i = 0; i < paramOptions.length; i++) {
			conditionalWhens[i] = new ConditionalWhen(
					paramOptions[i].getValue(),
					new LinkedHashMap<String, Object>());
		}

		WhenType[] whenTypes = conditionalType.getWhenArray();
		for (WhenType whenType : whenTypes) {
			ConditionalWhen when = new ConditionalWhen(whenType);
			String value = when.getValue();
			for (int i = 0; i < conditionalWhens.length; i++) {
				if (value.equals(conditionalWhens[i].getValue())) {
					conditionalWhens[i] = when;
					break;
				}
			}
		}
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Conditional";
	}

	/**
	 * �����������Ͳ�����ȫ���ؼ����ݣ�������ѡ��ؼ���������Ԫ�ڲ��Ŀؼ�
	 * 
	 * @param parent
	 */
	public void creatContents(Composite parent, Composite topComposite) {
		setTopComposite(topComposite);

		mainComposite = new Composite(parent, SWT.NONE);
		mainComposite.setBackground(mainComposite.getDisplay().getSystemColor(
				SWT.COLOR_WHITE));
		mainComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		mainComposite.setLayout(new GridLayout(1, false));

		FontData[] fontDatas = Display.getCurrent().getSystemFont()
				.getFontData();
		for (FontData fontData : fontDatas) {
			fontData.setStyle(SWT.BOLD);
		}
		Font font = new Font(Display.getCurrent(), fontDatas);

		paramLabelWidget = new Label(mainComposite, SWT.NONE);
		paramLabelWidget.setBackground(paramLabelWidget.getDisplay()
				.getSystemColor(SWT.COLOR_WHITE));
		String labelString = paramLabel + "  --  Conditional Param";
		labelString = WordUtils.wrap(labelString, 100);
		paramLabelWidget.setText(labelString);
		paramLabelWidget.setFont(font);

		comboGroup = new ComboGroup(mainComposite);
		comboGroup.setProvider(new SelectWidgetProvider());
		comboGroup.setInput(paramOptions);

		if (isParamHelpExist()) {
			paramHelpWidget = new Label(mainComposite, SWT.NONE);
			paramHelpWidget.setBackground(paramHelpWidget.getDisplay()
					.getSystemColor(SWT.COLOR_WHITE));
			String helpString = paramHelp;
			helpString = WordUtils.wrap(helpString, 100);
			paramHelpWidget.setText(helpString);
			Color color = Display.getCurrent().getSystemColor(
					SWT.COLOR_DARK_GRAY);
			paramHelpWidget.setForeground(color);
		}

		font.dispose();

		int index = comboGroup.getCombo().getSelectionIndex();
		oldIndex = index;
		// Option selectedOption = (Option) comboGroup.getSelection()[0];
		ConditionalWhen selectedWhen = conditionalWhens[index];
		if (!selectedWhen.isEmpty()) {
			group = new Group(mainComposite, SWT.NONE);
			group.setBackground(group.getDisplay().getSystemColor(
					SWT.COLOR_WHITE));

			// ���������¼�����߲㸸������ȡ������
			addGroupMouseDownFoucusEvent();

			group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			group.setLayout(new GridLayout(1, false));
			selectedWhen.creatContents(group, this.topComposite);
		} else {
			group = new Group(mainComposite, SWT.NONE);
			group.setBackground(group.getDisplay().getSystemColor(
					SWT.COLOR_WHITE));

			// ���������¼�����߲㸸������ȡ������
			addGroupMouseDownFoucusEvent();

			group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			group.setLayout(new GridLayout(1, false));
			Label label = new Label(group, SWT.NONE);
			label.setBackground(label.getDisplay().getSystemColor(
					SWT.COLOR_WHITE));

			// ���������¼�����߲㸸������ȡ������
			label.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
					// TODO Auto-generated method stub
					Conditional.this.topComposite.setFocus();
				}
			});

			label.setText("No Parameters In It");
		}
		separatorLine = new Label(mainComposite, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		separatorLine.setLayoutData(gridData);

		comboGroup.getCombo().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				Combo combo = (Combo) e.widget;
				int index = combo.getSelectionIndex();
				if (index == oldIndex) {
					oldIndex = index;
				} else {
					// ����ǹ�����ģʽ���򲻱���֮ǰѡ��ֵ��Ӧ���û�����ֵ���ǹ�����ģʽ���б���
					// if (!isClearDataToolParameter) {
					conditionalWhens[oldIndex].doSave();
					// }

					// ����ǹ�����ģʽ�������ԭ��DataToolParameter������ֵ
					if (isClearDataToolParameter) {
						conditionalWhens[oldIndex]
								.clearDataToolParameterSavedVaule();
					}

					// ���¸�ֵoldIndex
					oldIndex = index;

					if (group != null && !group.isDisposed())
						group.dispose();

					// Option selectedOption = (Option)
					// comboGroup.getSelection()[0];
					ConditionalWhen selectedWhen = conditionalWhens[index];
					if (!selectedWhen.isEmpty()) {
						group = new Group(mainComposite, SWT.NONE);
						group.setBackground(group.getDisplay().getSystemColor(
								SWT.COLOR_WHITE));

						// ���������¼�����߲㸸������ȡ������
						addGroupMouseDownFoucusEvent();

						group.moveAbove(separatorLine);
						// group.setText("Choose:  " +
						// selectedOption.getText());
						group.setLayoutData(new GridData(
								GridData.FILL_HORIZONTAL));
						group.setLayout(new GridLayout(1, false));
						selectedWhen.creatContents(group,
								Conditional.this.topComposite);
						Conditional.this.topComposite
								.layout(new Control[] { paramLabelWidget });
						Utils.reSize(Conditional.this.topComposite);
					} else {
						group = new Group(mainComposite, SWT.NONE);
						group.setBackground(group.getDisplay().getSystemColor(
								SWT.COLOR_WHITE));

						// ���������¼�����߲㸸������ȡ������
						addGroupMouseDownFoucusEvent();

						group.moveAbove(separatorLine);
						group.setLayoutData(new GridData(
								GridData.FILL_HORIZONTAL));
						group.setLayout(new GridLayout(1, false));
						Label label = new Label(group, SWT.NONE);

						// ���������¼�����߲㸸������ȡ������
						label.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseDown(MouseEvent e) {
								// TODO Auto-generated method stub
								Conditional.this.topComposite.setFocus();
							}
						});
						label.setBackground(label.getDisplay().getSystemColor(
								SWT.COLOR_WHITE));
						label.setText("No Parameters In It");
						Conditional.this.topComposite
								.layout(new Control[] { paramLabelWidget });
						Utils.reSize(Conditional.this.topComposite);
					}
				}
			}
		});

		// ���������¼�����߲㸸������ȡ������
		addMouseDownFoucusEvent();
	}

	/**
	 * �������,���������õ���߲�����
	 */
	protected void addMouseDownFoucusEvent() {
		mainComposite.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				Conditional.this.topComposite.setFocus();
			}
		});

		paramLabelWidget.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				Conditional.this.topComposite.setFocus();
			}
		});

		if (isParamHelpExist()) {
			paramHelpWidget.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
					// TODO Auto-generated method stub
					Conditional.this.topComposite.setFocus();
				}
			});
		}

		separatorLine.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				Conditional.this.topComposite.setFocus();
			}
		});
	}

	/**
	 * ��group����¼�����߲�������ȡ������
	 */
	protected void addGroupMouseDownFoucusEvent() {
		group.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				Conditional.this.topComposite.setFocus();
			}
		});
	}

	/**
	 * ��֤�û�������
	 * 
	 * @return
	 */
	public boolean startValidating() {
		int index = comboGroup.getCombo().getSelectionIndex();
		return conditionalWhens[index].startValidating();
	}

	/**
	 * ��֤���������ֵ�Ƿ������޶�
	 * 
	 * @return
	 */
	public boolean validateSavedValue() {
		int index = -1;
		for (int i = 0; i < paramOptions.length; i++) {
			if (paramOptions[i].isSelected()) {
				index = i;
				break;
			}
		}
		return conditionalWhens[index].validateSavedValue();
	}

	/**
	 * �������DataToolParameter�����ڳ�Ա�����е�ֵ
	 */
	public void clearDataToolParameterSavedVaule() {
		// TODO Auto-generated method stub
		for (int i = 0; i < conditionalWhens.length; i++) {
			conditionalWhens[i].clearDataToolParameterSavedVaule();
		}
	}

	/**
	 * ���û����뱣�浽��Ա����
	 */
	public void doSave() {
		if (comboGroup != null && !comboGroup.getControl().isDisposed()) {
			ArrayList<ObjectWithStat> list = comboGroup
					.getObjectsWithSelectionStat();
			ArrayList<Option> optionsList = new ArrayList<Option>();
			for (ObjectWithStat object : list) {
				Option option = (Option) object.getObject();
				option.setSelected(object.isSelected());
				optionsList.add(option);
			}
			paramOptions = optionsList.toArray(new Option[optionsList.size()]);

			int index = comboGroup.getCombo().getSelectionIndex();
			conditionalWhens[index].doSave();

			HashMap<String, Object> map = new HashMap<String, Object>();
			Option selectedOption = (Option) comboGroup.getSelection()[0];
			map.put(paramName, selectedOption.getValue());
			map.putAll(conditionalWhens[index].getDataModelMap());

			HashMap<String, Object> map2 = new HashMap<String, Object>();
			map2.put(name, map);
			dataModelMap = map2;
		} else {
			int index = -1;
			for (int i = 0; i < paramOptions.length; i++) {
				if (paramOptions[i].isSelected()) {
					index = i;
					break;
				}
			}

			conditionalWhens[index].doSave();

			HashMap<String, Object> map = new HashMap<String, Object>();
			Option selectedOption = paramOptions[index];
			map.put(paramName, selectedOption.getValue());
			map.putAll(conditionalWhens[index].getDataModelMap());

			HashMap<String, Object> map2 = new HashMap<String, Object>();
			map2.put(name, map);
			dataModelMap = map2;
		}
	}

	/**
	 * ���������ؼ�������ģ��
	 * 
	 * @return
	 */
	public HashMap<String, Object> getDataModelMap() {
		return dataModelMap;
	}

	/**
	 * ����ΪXMLBeans����
	 * 
	 * @return
	 */
	public ConditionalType saveAsConditionalType() {
		ConditionalType conditionalType = ConditionalType.Factory.newInstance();
		conditionalType.setName(name);

		Param param = Param.Factory.newInstance();
		param.setName(paramName);
		param.setLabel(paramLabel);
		param.setHelp(paramHelp);
		OptionType[] optionTypes = new OptionType[paramOptions.length];
		for (int i = 0; i < paramOptions.length; i++) {
			optionTypes[i] = OptionType.Factory.newInstance();
			optionTypes[i].setSelected(String.valueOf(paramOptions[i]
					.isSelected()));
			optionTypes[i].setValue(paramOptions[i].getValue());
			optionTypes[i].setStringValue(paramOptions[i].getText());
		}
		param.setOptionArray(optionTypes);
		conditionalType.setParam(param);

		WhenType[] whenTypes = new WhenType[conditionalWhens.length];
		for (int i = 0; i < conditionalWhens.length; i++) {
			whenTypes[i] = conditionalWhens[i].saveAsWhenType();
		}
		conditionalType.setWhenArray(whenTypes);

		return conditionalType;
	}

	/**
	 * �滻�ļ��������Ͳ�������ʽ�����ڹ�����ģʽ���ڿؼ�����֮ǰ���ã����һ�������ѡ��ı�ʱ�����ԭ��DataToolParameter������ֵ
	 */
	public void registerLabelDataWidget() {
		isClearDataToolParameter = true;
		for (int i = 0; i < conditionalWhens.length; i++) {
			conditionalWhens[i].registerLabelDataWidget();
		}
	}

	/**
	 * ע��ʼ��ִ�м�飬�ڿؼ�������֮ǰע��
	 */
	public void registerAlwaysValidating() {
		for (int i = 0; i < conditionalWhens.length; i++) {
			conditionalWhens[i].registerAlwaysValidating();
		}
	}

	/**
	 * ����DataToolParameter����ķ�װ��list��Save֮�����
	 * 
	 * @return
	 */
	public ArrayList<DataToolParameterEncapsulation> getDataToolParameterEncapsulationList(
			String labelPrefix) {
		ArrayList<DataToolParameterEncapsulation> list = new ArrayList<DataToolParameterEncapsulation>();

		int index = -1;
		for (int i = 0; i < paramOptions.length; i++) {
			if (paramOptions[i].isSelected()) {
				index = i;
				break;
			}
		}

		list.addAll(conditionalWhens[index]
				.getDataToolParameterEncapsulationList(labelPrefix));
		return list;
	}

	/**
	 * �ͷſؼ�
	 * 
	 */
	public void dispose() {
		mainComposite.dispose();
	}

	/**
	 * ���ذ�������ѡ��ؼ��ĸ�����
	 * 
	 * @return
	 */
	public Composite getPrimaryComposite() {
		return mainComposite;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Param getParam() {
		return param;
	}

	public void setParam(Param param) {
		this.param = param;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getParamLabel() {
		return paramLabel;
	}

	public void setParamLabel(String paramLabel) {
		this.paramLabel = paramLabel;
	}

	public String getParamHelp() {
		return paramHelp;
	}

	public void setParamHelp(String paramHelp) {
		this.paramHelp = paramHelp;
	}

	public boolean isParamHelpExist() {
		return paramHelpExist;
	}

	public void setParamHelpExist(boolean paramHelpExist) {
		this.paramHelpExist = paramHelpExist;
	}

	public Option[] getParamOptions() {
		return paramOptions;
	}

	public void setParamOptions(Option[] paramOptions) {
		this.paramOptions = paramOptions;
	}

	public ConditionalWhen[] getConditionalWhens() {
		return conditionalWhens;
	}

	public Composite getTopComposite() {
		return topComposite;
	}

	public void setTopComposite(Composite topComposite) {
		this.topComposite = topComposite;
	}

	public Composite getMainComposite() {
		return mainComposite;
	}

	public Label getParamLabelWidget() {
		return paramLabelWidget;
	}

	public ComboGroup getComboGroup() {
		return comboGroup;
	}

	public Label getParamHelpWidget() {
		return paramHelpWidget;
	}

	public boolean isClearDataToolParameter() {
		return isClearDataToolParameter;
	}

	public void setClearDataToolParameter(boolean isClearDataToolParameter) {
		this.isClearDataToolParameter = isClearDataToolParameter;
	}

}
