package com.sixthhosp.gcmpa.tools.parameters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.xmlbeans.XmlObject;
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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import com.sixthhosp.gcmpa.xmlbeans.tool.RepeatType;
import com.sixthhosp.gcmpa.xmlbeans.tool.UnitType;
import com.sixthhosp.gcmpa.xmlbeans.tool.UnitsType;

import com.sixthhosp.gcmpa.tools.Utils;

/**
 * �ظ����Ͳ���
 * 
 * @author zhengzequn
 * 
 */
public class Repeat {

	private String name;
	private String title;
	private String help;
	private int deFault;
	private int min;
	private int max;

	private boolean workflowModel;
	private boolean isAlwaysValidate;
	private boolean HelpExist;
	private XmlObject[] defaultXmlObjects;
	private ArrayList<LinkedHashMap<String, Object>> repeatInputsUnitList;

	private HashMap<String, Object> dataModelMap;

	/**
	 * ��߲㼶������
	 */
	private Composite topComposite;

	/**
	 * �ظ����Ͳ�������ĸ�����
	 */
	private Composite mainComposite;

	/**
	 * �ظ����Ͳ����ı����ǩ
	 */
	private Label titleWidget;

	/**
	 * �ظ����Ͳ�������İ�����ǩ
	 */
	private Label helpWidget;

	/**
	 * ���Ӱ�ť
	 */
	private Button addButton;

	/**
	 * ɾ����ť��ÿ���ظ���Ԫ��Ӧһ��
	 */
	private ArrayList<Button> deleteButtons;

	/**
	 * �ظ���Ԫ��Ӧ���������������ߺͶ�Ӧ�Ŀؼ�
	 */
	private ArrayList<Composite> repeatUnitComposites;

	/**
	 * �����ؼ����ظ���Ԫ����
	 */
	private ArrayList<Composite> repeatPlaceInputsUnitComposites;

	/**
	 * ÿ���ظ���Ԫ��Ӧ�ı����ǩ
	 */
	private ArrayList<Label> repeatUnitTitleLabelWidgets;

	/**
	 * ��ǩ�ָ����
	 */
	private Label separatorLine;

	public Repeat() {
		repeatInputsUnitList = new ArrayList<LinkedHashMap<String, Object>>();
		deleteButtons = new ArrayList<Button>();
		repeatUnitComposites = new ArrayList<Composite>();
		repeatPlaceInputsUnitComposites = new ArrayList<Composite>();
		repeatUnitTitleLabelWidgets = new ArrayList<Label>();
	}

	public Repeat(RepeatType repeatType) {
		// TODO Auto-generated constructor stub
		this();
		setName(repeatType.getName());
		setTitle(repeatType.getTitle());
		setHelp(repeatType.getHelp());
		setDeFault(repeatType.getDefault());
		setMin(repeatType.getMin());
		setMax(repeatType.getMax());
		workflowModel = false;
		isAlwaysValidate = false;

		if (StringUtils.isBlank(title)) {
			title = name;
		}

		if (StringUtils.isBlank(help)) {
			help = "";
			setHelpExist(false);
		} else {
			setHelpExist(true);
		}

		if (!(min <= deFault && deFault <= max)) {
			throw new RuntimeException(
					"min,max,default are incorrect in repeat element at tool xml");
		}

		defaultXmlObjects = repeatType.selectChildren(Utils
				.getInputsAllQnameSet());

		if (repeatType.isSetUnits()) {
			UnitType[] unitTypes = repeatType.getUnits().getUnitArray();
			for (UnitType repUnitType : unitTypes) {
				XmlObject[] xmlObjects = repUnitType.selectChildren(Utils
						.getInputsAllQnameSet());
				repeatInputsUnitList.add(Utils.parseInputElement(xmlObjects));
			}
		} else {
			for (int i = 1; i <= deFault; i++) {
				LinkedHashMap<String, Object> map = Utils
						.parseInputElement(defaultXmlObjects);
				repeatInputsUnitList.add(map);
			}
		}
	}

	/**
	 * �������ؼ����õ�list���Ա��ٴε���creatcontents
	 */
	public void clearWidgetArrayList() {
		deleteButtons.clear();
		repeatUnitComposites.clear();
		repeatPlaceInputsUnitComposites.clear();
		repeatUnitTitleLabelWidgets.clear();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Repeat";
	}

	/**
	 * �����ظ����Ͳ�����ȫ���ؼ����ݣ�����title,help���ظ���Ԫ�ڲ��Ŀؼ�
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

		titleWidget = new Label(mainComposite, SWT.NONE);
		titleWidget.setBackground(titleWidget.getDisplay().getSystemColor(
				SWT.COLOR_WHITE));
		String titleText = title + "  --  Repeat Param";
		titleText = WordUtils.wrap(titleText, 100);
		titleWidget.setText(titleText);
		titleWidget.setFont(font);

		if (isHelpExist()) {
			helpWidget = new Label(mainComposite, SWT.NONE);
			helpWidget.setBackground(helpWidget.getDisplay().getSystemColor(
					SWT.COLOR_WHITE));
			String helpText = help;
			helpText = WordUtils.wrap(helpText, 100);
			helpWidget.setText(helpText);
			Color color = Display.getCurrent().getSystemColor(
					SWT.COLOR_DARK_GRAY);
			helpWidget.setForeground(color);
		}

		int i = 1;
		for (LinkedHashMap<String, Object> unitInputs : repeatInputsUnitList) {
			creatUnitContents(unitInputs, i);
			i++;
		}
		font.dispose();

		separatorLine = new Label(mainComposite, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		separatorLine.setLayoutData(gridData);

		creatButtons();

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
				Repeat.this.topComposite.setFocus();
			}
		});

		titleWidget.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				Repeat.this.topComposite.setFocus();
			}
		});

		if (isHelpExist()) {
			helpWidget.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
					// TODO Auto-generated method stub
					Repeat.this.topComposite.setFocus();
				}
			});
		}

		separatorLine.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				Repeat.this.topComposite.setFocus();
			}
		});
	}

	/**
	 * �����ظ���Ԫ�Ŀؼ������������еİ�ť
	 * 
	 * @param unitInputs
	 * @param i
	 */
	private void creatUnitContents(LinkedHashMap<String, Object> unitInputs,
			int i) {
		FontData[] fontDatas = Display.getCurrent().getSystemFont()
				.getFontData();
		for (FontData fontData : fontDatas) {
			fontData.setStyle(SWT.BOLD);
		}
		Font font = new Font(Display.getCurrent(), fontDatas);

		Composite unitComposite = new Composite(mainComposite, SWT.NONE);
		unitComposite.setBackground(unitComposite.getDisplay().getSystemColor(
				SWT.COLOR_WHITE));
		repeatUnitComposites.add(unitComposite);
		unitComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		unitComposite.setLayout(new GridLayout(2, false));

		Label unitTitleLabelWidget = new Label(unitComposite, SWT.NONE);
		unitTitleLabelWidget.setBackground(unitTitleLabelWidget.getDisplay()
				.getSystemColor(SWT.COLOR_WHITE));
		repeatUnitTitleLabelWidgets.add(unitTitleLabelWidget);
		String unitTitleText = title + " " + i + "  --  Repeat Unit " + i
				+ " :";
		unitTitleText = WordUtils.wrap(unitTitleText, 100);
		unitTitleLabelWidget.setText(unitTitleText);
		unitTitleLabelWidget.setFont(font);
		GridData gridData = new GridData();
		gridData.horizontalSpan = 2;
		unitTitleLabelWidget.setLayoutData(gridData);

		Label label = new Label(unitComposite, SWT.SEPARATOR | SWT.VERTICAL);
		label.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		Composite placeInputsUnitComposites = new Composite(unitComposite,
				SWT.NONE);
		placeInputsUnitComposites.setBackground(placeInputsUnitComposites
				.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		repeatPlaceInputsUnitComposites.add(placeInputsUnitComposites);
		placeInputsUnitComposites.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL));
		placeInputsUnitComposites.setLayout(new GridLayout(1, false));

		Utils.creatInputsContents(unitInputs, placeInputsUnitComposites,
				this.topComposite);

		font.dispose();

		// ���������¼�����߲㸸������ȡ������
		unitComposite.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				Repeat.this.topComposite.setFocus();
			}
		});

		unitTitleLabelWidget.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				Repeat.this.topComposite.setFocus();
			}
		});

		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				Repeat.this.topComposite.setFocus();
			}
		});

		placeInputsUnitComposites.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				Repeat.this.topComposite.setFocus();
			}
		});
	}

	private void renameLabelWidget() {
		int i = 1;
		for (Label label : repeatUnitTitleLabelWidgets) {
			String string = title + " " + i + "  --  Repeat Unit " + i + " :";
			label.setText(string);
			i++;
		}
	}

	/**
	 * ɾ���ظ���Ԫ��index��0��ʼ����
	 * 
	 * @param i
	 */
	private void deleteUnit(int index) {
		Composite composite = repeatUnitComposites.get(index);
		composite.dispose();
		repeatUnitComposites.remove(index);

		repeatPlaceInputsUnitComposites.remove(index);
		repeatUnitTitleLabelWidgets.remove(index);

		repeatInputsUnitList.remove(index);
	}

	/**
	 * ɾ�����Ӻ�ɾ����ť�ؼ���dispose���������list�е�����
	 */
	private void clearButtons() {
		if (addButton != null && !addButton.isDisposed()) {
			addButton.dispose();
			addButton = null;
		}

		for (Button button : deleteButtons) {
			if (button != null && !button.isDisposed())
				button.dispose();
		}
		deleteButtons.clear();
	}

	/**
	 * ���Ӱ�ť��ѭ������ɾ����ť����ĩβ������Ӱ�ť�� ����ظ���Ԫ����min���򲻴���ɾ����ť���������max�򲻴�����Ӱ�ť��
	 */
	private void creatButtons() {
		if (repeatInputsUnitList.size() > min) {
			for (int index = 0; index < repeatPlaceInputsUnitComposites.size(); index++) {
				Composite composite = repeatPlaceInputsUnitComposites
						.get(index);
				Button deleteUnitButton = new Button(composite, SWT.PUSH
						| SWT.FLAT);
				deleteButtons.add(deleteUnitButton);
				int num = index + 1;
				String string = "Remove " + title + " " + num;
				deleteUnitButton.setText(string);
				final Integer integer = new Integer(index);
				deleteUnitButton.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						// TODO Auto-generated method stub
						clearButtons();
						deleteUnit(integer.intValue());
						renameLabelWidget();
						creatButtons();
					}
				});
			}

		}
		if (repeatInputsUnitList.size() < max) {
			Composite composite = getMainComposite();
			addButton = new Button(composite, SWT.PUSH | SWT.FLAT);
			String string = "Add new " + title;
			addButton.setText(string);
			addButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					LinkedHashMap<String, Object> addedUnitInputs = Utils
							.parseInputElement(defaultXmlObjects);

					if (workflowModel) {
						Utils.registerLabelDataWidgetForInputs(addedUnitInputs);
					}

					if (isAlwaysValidate) {
						Utils.registerAlwaysValidatingForInputs(addedUnitInputs);
					}

					repeatInputsUnitList.add(addedUnitInputs);

					creatUnitContents(addedUnitInputs,
							repeatInputsUnitList.size());
					clearButtons();
					renameLabelWidget();
					creatButtons();

				}
			});
		}
		// �ƶ��ָ���
		separatorLine.moveBelow(null);

		// ˢ�½���

		Control[] controls = new Control[deleteButtons.size()
				+ repeatPlaceInputsUnitComposites.size()];
		for (int i = 0; i < deleteButtons.size(); i++) {
			controls[i] = deleteButtons.get(i);
		}
		for (int i = 0; i < repeatPlaceInputsUnitComposites.size(); i++) {
			controls[deleteButtons.size() + i] = repeatPlaceInputsUnitComposites
					.get(i);
		}
		topComposite.layout(controls);
		Utils.reSize(topComposite);
	}

	/**
	 * ��֤�û�������
	 * 
	 * @return
	 */
	public boolean startValidating() {
		boolean b = true;
		for (LinkedHashMap<String, Object> inputs : repeatInputsUnitList) {
			if (!Utils.startValidatingForInputs(inputs)) {
				b = false;
			}
		}
		return b;
	}

	/**
	 * ��֤���������ֵ�Ƿ������޶�
	 * 
	 * @return
	 */
	public boolean validateSavedValue() {
		boolean b = true;
		for (LinkedHashMap<String, Object> inputs : repeatInputsUnitList) {
			if (!Utils.validateSavedValueForInputs(inputs)) {
				b = false;
			}
		}
		return b;
	}

	/**
	 * �������DataToolParameter�����ڳ�Ա�����е�����ֵ
	 */
	public void clearDataToolParameterSavedVaule() {
		// TODO Auto-generated method stub
		for (LinkedHashMap<String, Object> inputs : repeatInputsUnitList) {
			Utils.clearDataToolParameterSavedVauleForInputs(inputs);
		}
	}

	/**
	 * ���û����뱣�浽��Ա����
	 */
	public void doSave() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

		for (LinkedHashMap<String, Object> inputs : repeatInputsUnitList) {
			Utils.doSaveForInputs(inputs);
			list.add(Utils.getDataModelFromInputs(inputs));
		}

		map.put(getName(), list);
		dataModelMap = map;
	}

	/**
	 * �����ظ��ؼ�������ģ��
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
	public RepeatType saveAsRepeatType() {
		RepeatType repeatType = RepeatType.Factory.newInstance();
		repeatType.setName(name);
		repeatType.setTitle(title);
		repeatType.setHelp(help);
		repeatType.setDefault(deFault);
		repeatType.setMin(min);
		repeatType.setMax(max);

		for (XmlObject xmlObject : defaultXmlObjects) {
			Utils.addInputsXMLObjectsToParentObject(repeatType, xmlObject);
		}

		UnitsType unitsType = repeatType.addNewUnits();
		for (LinkedHashMap<String, Object> inputs : repeatInputsUnitList) {
			UnitType unitType = unitsType.addNewUnit();
			ArrayList<XmlObject> xmlObjects = Utils
					.getXmlObjectsFromInputs(inputs);
			for (XmlObject child : xmlObjects) {
				Utils.addInputsXMLObjectsToParentObject(unitType, child);
			}
		}

		return repeatType;
	}

	/**
	 * �滻�ļ��������Ͳ�������ʽ�����ڹ�����ģʽ���ڿؼ�����֮ǰ����
	 */
	public void registerLabelDataWidget() {
		workflowModel = true;
		for (LinkedHashMap<String, Object> inputs : repeatInputsUnitList) {
			Utils.registerLabelDataWidgetForInputs(inputs);
		}
	}

	/**
	 * ע��ʼ��ִ�м�飬�ڿؼ�������֮ǰע��
	 * 
	 * @param inputs
	 */
	public void registerAlwaysValidating() {
		isAlwaysValidate = true;
		for (LinkedHashMap<String, Object> inputs : repeatInputsUnitList) {
			Utils.registerAlwaysValidatingForInputs(inputs);
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

		int i = 1;
		for (LinkedHashMap<String, Object> inputs : repeatInputsUnitList) {
			String string = labelPrefix + title + " " + i + " -> ";
			list.addAll(Utils.getDataToolParameterEncapsulationListFromInputs(
					inputs, string));
			i++;
		}
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
	 * ���ذ���title��help�ظ���Ԫ�ؼ��ĸ�����
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getHelp() {
		return help;
	}

	public void setHelp(String help) {
		this.help = help;
	}

	public int getDeFault() {
		return deFault;
	}

	public void setDeFault(int deFault) {
		this.deFault = deFault;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public XmlObject[] getDefaultXmlObjects() {
		return defaultXmlObjects;
	}

	public void setDefaultXmlObjects(XmlObject[] defaultXmlObjects) {
		this.defaultXmlObjects = defaultXmlObjects;
	}

	public ArrayList<LinkedHashMap<String, Object>> getRepeatInputsUnitList() {
		return repeatInputsUnitList;
	}

	public void setRepeatInputsUnitList(
			ArrayList<LinkedHashMap<String, Object>> repeatInputsUnitList) {
		this.repeatInputsUnitList = repeatInputsUnitList;
	}

	public boolean isHelpExist() {
		return HelpExist;
	}

	public void setHelpExist(boolean helpExist) {
		HelpExist = helpExist;
	}

	public Label getTitleWidget() {
		return titleWidget;
	}

	public Label getHelpWidget() {
		return helpWidget;
	}

	public Composite getTopComposite() {
		return topComposite;
	}

	public void setTopComposite(Composite topComposite) {
		this.topComposite = topComposite;
	}

	public Button getAddButton() {
		return addButton;
	}

	public ArrayList<Button> getDeleteButtons() {
		return deleteButtons;
	}

	public ArrayList<Composite> getRepeatUnitComposites() {
		return repeatUnitComposites;
	}

	public ArrayList<Composite> getRepeatPlaceInputsUnitComposites() {
		return repeatPlaceInputsUnitComposites;
	}

	public ArrayList<Label> getRepeatUnitTitleLabelWidgets() {
		return repeatUnitTitleLabelWidgets;
	}

	public Composite getMainComposite() {
		return mainComposite;
	}

	public boolean isWorkflowModel() {
		return workflowModel;
	}

	public void setWorkflowModel(boolean workflowModel) {
		this.workflowModel = workflowModel;
	}

	public boolean isAlwaysValidate() {
		return isAlwaysValidate;
	}

	public void setAlwaysValidate(boolean isAlwaysValidate) {
		this.isAlwaysValidate = isAlwaysValidate;
	}

}
