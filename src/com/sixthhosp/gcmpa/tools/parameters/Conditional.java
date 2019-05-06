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
 * 条件类型参数
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
	 * 最高层级的容器
	 */
	private Composite topComposite;

	/**
	 * 条件类型参数组件的父容器
	 */
	private Composite mainComposite;

	/**
	 * 条件类型组件的参数标签
	 */
	private Label paramLabelWidget;

	/**
	 * 条件类型组件的下拉框
	 */
	private ComboGroup comboGroup;

	/**
	 * 条件类型组件的帮助标签
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
	 * 创建条件类型参数的全部控件内容，包含单选框控件，条件单元内部的控件
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

			// 添加鼠标点击事件，最高层父容器获取到焦点
			addGroupMouseDownFoucusEvent();

			group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			group.setLayout(new GridLayout(1, false));
			selectedWhen.creatContents(group, this.topComposite);
		} else {
			group = new Group(mainComposite, SWT.NONE);
			group.setBackground(group.getDisplay().getSystemColor(
					SWT.COLOR_WHITE));

			// 添加鼠标点击事件，最高层父容器获取到焦点
			addGroupMouseDownFoucusEvent();

			group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			group.setLayout(new GridLayout(1, false));
			Label label = new Label(group, SWT.NONE);
			label.setBackground(label.getDisplay().getSystemColor(
					SWT.COLOR_WHITE));

			// 添加鼠标点击事件，最高层父容器获取到焦点
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
					// 如果是工作流模式，则不保存之前选项值对应的用户输入值，非工作流模式进行保存
					// if (!isClearDataToolParameter) {
					conditionalWhens[oldIndex].doSave();
					// }

					// 如果是工作流模式，则清空原来DataToolParameter的输入值
					if (isClearDataToolParameter) {
						conditionalWhens[oldIndex]
								.clearDataToolParameterSavedVaule();
					}

					// 重新赋值oldIndex
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

						// 添加鼠标点击事件，最高层父容器获取到焦点
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

						// 添加鼠标点击事件，最高层父容器获取到焦点
						addGroupMouseDownFoucusEvent();

						group.moveAbove(separatorLine);
						group.setLayoutData(new GridData(
								GridData.FILL_HORIZONTAL));
						group.setLayout(new GridLayout(1, false));
						Label label = new Label(group, SWT.NONE);

						// 添加鼠标点击事件，最高层父容器获取到焦点
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

		// 添加鼠标点击事件，最高层父容器获取到焦点
		addMouseDownFoucusEvent();
	}

	/**
	 * 按下鼠标,将焦点设置到最高层容器
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
	 * 对group添加事件，最高层容器获取到焦点
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
	 * 验证用户的输入
	 * 
	 * @return
	 */
	public boolean startValidating() {
		int index = comboGroup.getCombo().getSelectionIndex();
		return conditionalWhens[index].startValidating();
	}

	/**
	 * 验证保存的输入值是否满足限定
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
	 * 清除参数DataToolParameter保存在成员变量中的值
	 */
	public void clearDataToolParameterSavedVaule() {
		// TODO Auto-generated method stub
		for (int i = 0; i < conditionalWhens.length; i++) {
			conditionalWhens[i].clearDataToolParameterSavedVaule();
		}
	}

	/**
	 * 将用户输入保存到成员变量
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
	 * 返回条件控件的数据模型
	 * 
	 * @return
	 */
	public HashMap<String, Object> getDataModelMap() {
		return dataModelMap;
	}

	/**
	 * 保存为XMLBeans对象
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
	 * 替换文件输入类型参数的样式，用于工作流模式，在控件产生之前调用，并且会在条件选项改变时，清除原有DataToolParameter的输入值
	 */
	public void registerLabelDataWidget() {
		isClearDataToolParameter = true;
		for (int i = 0; i < conditionalWhens.length; i++) {
			conditionalWhens[i].registerLabelDataWidget();
		}
	}

	/**
	 * 注册始终执行检查，在控件被创建之前注册
	 */
	public void registerAlwaysValidating() {
		for (int i = 0; i < conditionalWhens.length; i++) {
			conditionalWhens[i].registerAlwaysValidating();
		}
	}

	/**
	 * 返回DataToolParameter对象的封装类list，Save之后调用
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
	 * 释放控件
	 * 
	 */
	public void dispose() {
		mainComposite.dispose();
	}

	/**
	 * 返回包含条件选项控件的父容器
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
