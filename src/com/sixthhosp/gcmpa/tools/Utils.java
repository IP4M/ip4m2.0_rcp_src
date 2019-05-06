package com.sixthhosp.gcmpa.tools;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.QNameSetBuilder;
import org.apache.xmlbeans.XmlObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.sixthhosp.gcmpa.xmlbeans.tool.ConditionalType;
import com.sixthhosp.gcmpa.xmlbeans.tool.DataType;
import com.sixthhosp.gcmpa.xmlbeans.tool.HtmlType;
import com.sixthhosp.gcmpa.xmlbeans.tool.InputsType;
import com.sixthhosp.gcmpa.xmlbeans.tool.OutputsType;
import com.sixthhosp.gcmpa.xmlbeans.tool.ParamType;
import com.sixthhosp.gcmpa.xmlbeans.tool.RepeatType;
import com.sixthhosp.gcmpa.xmlbeans.tool.UnitType;
import com.sixthhosp.gcmpa.xmlbeans.tool.WhenType;
import com.sixthhosp.gcmpa.configs.ConfigFile;
import com.sixthhosp.gcmpa.tools.parameters.Conditional;
import com.sixthhosp.gcmpa.tools.parameters.DataToolOutput;
import com.sixthhosp.gcmpa.tools.parameters.DataToolParameter;
import com.sixthhosp.gcmpa.tools.parameters.DataToolParameterEncapsulation;
import com.sixthhosp.gcmpa.tools.parameters.HtmlToolOutput;
import com.sixthhosp.gcmpa.tools.parameters.Repeat;
import com.sixthhosp.gcmpa.tools.parameters.ToolOutput;
import com.sixthhosp.gcmpa.tools.parameters.ToolParameter;

/**
 * 操作XMLBeans的常用函数，和其他一些函数的集合
 * 
 * @author zhengzequn
 * 
 */
public class Utils {

	private static QNameSet sectionAllQnameSet;
	private static QNameSet inputsAllQnameSet;

	private Utils() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 获取Section下tool和label的QnameSet
	 * 
	 * @return
	 */
	public static QNameSet getSectionAllQnameSet() {
		if (sectionAllQnameSet == null) {
			QNameSetBuilder qnsb = new QNameSetBuilder();
			qnsb.add(new QName("tool"));
			qnsb.add(new QName("label"));
			qnsb.add(new QName("section"));
			sectionAllQnameSet = qnsb.toQNameSet();
		}
		return sectionAllQnameSet;
	}

	/**
	 * param,conditional,repeat的QnameSet
	 * 
	 * @return
	 */
	public static QNameSet getInputsAllQnameSet() {
		if (inputsAllQnameSet == null) {
			QNameSetBuilder qnsb = new QNameSetBuilder();
			qnsb.add(new QName("param"));
			qnsb.add(new QName("conditional"));
			qnsb.add(new QName("repeat"));
			inputsAllQnameSet = qnsb.toQNameSet();
		}
		return inputsAllQnameSet;
	}

	/**
	 * 刷新滚动容器大小，以显示其下所有的容器
	 * 
	 * @param topComposite
	 */
	public static void reSize(Composite topComposite) {
		ScrolledComposite scrolledComposite = (ScrolledComposite) topComposite
				.getParent();
		scrolledComposite.setMinSize(topComposite.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));
	}

	/**
	 * 根据param,repeat,conditional标签获得对应实例的有序字典，其中的界面控件并没有被初始化
	 * 即creatContents方法并没有被调用
	 * 
	 * @param xmlObjects
	 * @return
	 */
	public static LinkedHashMap<String, Object> parseInputElement(
			XmlObject[] xmlObjects) {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		for (XmlObject xmlObject : xmlObjects) {
			if (xmlObject instanceof ParamType) {
				ParamType paramType = (ParamType) xmlObject;
				ToolParameter toolParameter = ToolParameter.init(paramType);
				String name = toolParameter.getName();
				map.put(name, toolParameter);
			} else if (xmlObject instanceof ConditionalType) {
				ConditionalType conditionalType = (ConditionalType) xmlObject;
				Conditional conditionalParameter = new Conditional(
						conditionalType);
				String name = conditionalParameter.getName();
				map.put(name, conditionalParameter);
			} else if (xmlObject instanceof RepeatType) {
				RepeatType repeatType = (RepeatType) xmlObject;
				Repeat repeatParameter = new Repeat(repeatType);
				String name = repeatParameter.getName();
				map.put(name, repeatParameter);
			} else {
				throw new RuntimeException("unknown inputs");
			}
		}
		return map;
	}

	/**
	 * 根据inputs XML标签，获得输入参数类型对应实例的有序字典，其中的界面控件并没有被初始化
	 * 
	 * @param inputsType
	 * @return
	 */
	public static LinkedHashMap<String, Object> parseInputsType(
			InputsType inputsType) {
		XmlObject[] xmlObjects = inputsType.selectChildren(Utils
				.getInputsAllQnameSet());
		return Utils.parseInputElement(xmlObjects);
	}

	/**
	 * 根据outputs XML标签，获得输出参数类型对应实例的有序字典
	 */
	public static LinkedHashMap<String, ToolOutput> parseOutputsType(
			OutputsType outputsType) {
		LinkedHashMap<String, ToolOutput> map = new LinkedHashMap<String, ToolOutput>();
		DataType[] dataTypes = outputsType.getDataArray();
		for (DataType dataType : dataTypes) {
			DataToolOutput dataToolOutput = new DataToolOutput(dataType);
			map.put(dataToolOutput.getName(), dataToolOutput);
		}

		HtmlType htmlType = outputsType.getHtml();
		HtmlToolOutput htmlToolOutput = new HtmlToolOutput(htmlType);
		map.put(htmlToolOutput.getName(), htmlToolOutput);
		return map;
	}

	/**
	 * 根据输入参数的有序字典，创建参数图形化控件
	 * 
	 * @param inputs
	 * @param parent
	 * @param topComposite
	 */
	public static void creatInputsContents(
			LinkedHashMap<String, Object> inputs, Composite parent,
			Composite topComposite) {
		Iterator<Entry<String, Object>> iterator = inputs.entrySet().iterator();
		while (iterator.hasNext()) {
			Object object = iterator.next().getValue();
			if (object instanceof ToolParameter) {
				ToolParameter toolParameter = (ToolParameter) object;
				toolParameter.creatContents(parent, topComposite);
			} else if (object instanceof Repeat) {
				Repeat repeat = (Repeat) object;
				repeat.clearWidgetArrayList();
				repeat.creatContents(parent, topComposite);
			} else if (object instanceof Conditional) {
				Conditional conditional = (Conditional) object;
				conditional.creatContents(parent, topComposite);
			} else {
				throw new RuntimeException("unknown inputs");
			}
		}
	}

	/**
	 * 根据参数的有序字典，保存用户的控件输入到相应类的成员变量
	 * 
	 * @param inputs
	 */
	public static void doSaveForInputs(LinkedHashMap<String, Object> inputs) {
		Iterator<Entry<String, Object>> iterator = inputs.entrySet().iterator();
		while (iterator.hasNext()) {
			Object object = iterator.next().getValue();
			if (object instanceof ToolParameter) {
				ToolParameter toolParameter = (ToolParameter) object;
				toolParameter.doSave();
			} else if (object instanceof Repeat) {
				Repeat repeat = (Repeat) object;
				repeat.doSave();
			} else if (object instanceof Conditional) {
				Conditional conditional = (Conditional) object;
				conditional.doSave();
			} else {
				throw new RuntimeException("unknown inputs");
			}
		}
	}

	/**
	 * 根据输出参数的有序字典，设置输出参数的路径信息
	 * 
	 * @param outputs
	 * @param outDirectory
	 */
	public static void setOutDirectoryPathForOutputs(
			LinkedHashMap<String, ToolOutput> outputs, String outDirectory) {
		Iterator<Entry<String, ToolOutput>> iterator = outputs.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			ToolOutput toolOutput = iterator.next().getValue();
			toolOutput.setOutDirectory(outDirectory);
		}
	}

	/**
	 * 根据输出参数的有序字典，保存用户的控件输入到ToolOutput类的成员变量
	 * 
	 * @param outputs
	 */
	public static void doSaveForOutputs(
			LinkedHashMap<String, ToolOutput> outputs) {
		Iterator<Entry<String, ToolOutput>> iterator = outputs.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			ToolOutput toolOutput = iterator.next().getValue();
			toolOutput.doSave();
		}
	}

	/**
	 * 从参数输出有序字典中，获得html参数输出结果
	 * 
	 * @param outputs
	 * @return
	 */
	public static HtmlToolOutput getHtmlToolOutput(
			LinkedHashMap<String, ToolOutput> outputs) {
		Iterator<Entry<String, ToolOutput>> iterator = outputs.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			ToolOutput toolOutput = iterator.next().getValue();
			if (toolOutput instanceof HtmlToolOutput) {
				return (HtmlToolOutput) toolOutput;
			}
		}
		return null;
	}

	/**
	 * 从参数输出有序字典中，获得数据参数输出结果
	 * 
	 * @param outputs
	 * @return
	 */
	public static ArrayList<DataToolOutput> getDataToolOutputs(
			LinkedHashMap<String, ToolOutput> outputs) {
		Iterator<Entry<String, ToolOutput>> iterator = outputs.entrySet()
				.iterator();
		ArrayList<DataToolOutput> dataToolOutputs = new ArrayList<DataToolOutput>();
		while (iterator.hasNext()) {
			ToolOutput toolOutput = iterator.next().getValue();
			if (toolOutput instanceof DataToolOutput) {
				dataToolOutputs.add((DataToolOutput) toolOutput);
			}
		}
		return dataToolOutputs;
	}

	/**
	 * 根据参数的有序字典，返回参数成员变量对应的数据模型，在dosave之后调用
	 * 
	 * @param inputs
	 */
	public static HashMap<String, Object> getDataModelFromInputs(
			LinkedHashMap<String, Object> inputs) {
		HashMap<String, Object> map = new HashMap<String, Object>();

		Iterator<Entry<String, Object>> iterator = inputs.entrySet().iterator();
		while (iterator.hasNext()) {
			Object object = iterator.next().getValue();
			if (object instanceof ToolParameter) {
				ToolParameter toolParameter = (ToolParameter) object;
				map.putAll(toolParameter.getDataModelMap());
			} else if (object instanceof Repeat) {
				Repeat repeat = (Repeat) object;
				map.putAll(repeat.getDataModelMap());
			} else if (object instanceof Conditional) {
				Conditional conditional = (Conditional) object;
				map.putAll(conditional.getDataModelMap());
			} else {
				throw new RuntimeException("unknown inputs");
			}
		}
		return map;
	}

	/**
	 * 根据输出参数的有序字典，返回输出参数成员变量对应的数据模型，在dosave之后调用
	 * 
	 * @param outputs
	 * @return
	 */
	public static HashMap<String, String> getDataModelFromOutputs(
			LinkedHashMap<String, ToolOutput> outputs) {
		HashMap<String, String> map = new HashMap<String, String>();

		Iterator<Entry<String, ToolOutput>> iterator = outputs.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			ToolOutput toolOutput = iterator.next().getValue();
			map.putAll(toolOutput.getDataModelMap());
		}
		return map;
	}

	/**
	 * 将XmlObject对象child添加到parent中，允许child类型为ParamType,RepeatType,
	 * ConditionalType 允许的parent类型为InputsType,RepeatType,WhenType,UnitType
	 * 
	 * @param parent
	 * @param child
	 */
	public static void addInputsXMLObjectsToParentObject(XmlObject parent,
			XmlObject child) {
		if (parent instanceof RepeatType) {
			RepeatType repeatType = (RepeatType) parent;

			if (child instanceof ParamType) {
				repeatType.addNewParam().set(child);
			} else if (child instanceof RepeatType) {
				repeatType.addNewRepeat().set(child);
			} else if (child instanceof ConditionalType) {
				repeatType.addNewConditional().set(child);
			} else {
				throw new RuntimeException("child type error");
			}

		} else if (parent instanceof InputsType) {
			InputsType inputsType = (InputsType) parent;

			if (child instanceof ParamType) {
				inputsType.addNewParam().set(child);
			} else if (child instanceof RepeatType) {
				inputsType.addNewRepeat().set(child);
			} else if (child instanceof ConditionalType) {
				inputsType.addNewConditional().set(child);
			} else {
				throw new RuntimeException("child type error");
			}

		} else if (parent instanceof WhenType) {
			WhenType whenType = (WhenType) parent;

			if (child instanceof ParamType) {
				whenType.addNewParam().set(child);
			} else if (child instanceof RepeatType) {
				whenType.addNewRepeat().set(child);
			} else if (child instanceof ConditionalType) {
				whenType.addNewConditional().set(child);
			} else {
				throw new RuntimeException("child type error");
			}

		} else if (parent instanceof UnitType) {
			UnitType unitType = (UnitType) parent;

			if (child instanceof ParamType) {
				unitType.addNewParam().set(child);
			} else if (child instanceof RepeatType) {
				unitType.addNewRepeat().set(child);
			} else if (child instanceof ConditionalType) {
				unitType.addNewConditional().set(child);
			} else {
				throw new RuntimeException("child type error");
			}
		} else {
			throw new RuntimeException("parent type error");
		}
	}

	/**
	 * 从参数的有序字典中返回XMLBeans对象List
	 * 
	 * @param inputs
	 * @return
	 */
	public static ArrayList<XmlObject> getXmlObjectsFromInputs(
			LinkedHashMap<String, Object> inputs) {
		ArrayList<XmlObject> list = new ArrayList<XmlObject>();

		Iterator<Entry<String, Object>> iterator = inputs.entrySet().iterator();
		while (iterator.hasNext()) {
			Object object = iterator.next().getValue();
			if (object instanceof ToolParameter) {
				ToolParameter toolParameter = (ToolParameter) object;
				list.add(toolParameter.saveAsParamType());
			} else if (object instanceof Repeat) {
				Repeat repeat = (Repeat) object;
				list.add(repeat.saveAsRepeatType());
			} else if (object instanceof Conditional) {
				Conditional conditional = (Conditional) object;
				list.add(conditional.saveAsConditionalType());
			} else {
				throw new RuntimeException("unknown inputs");
			}
		}

		return list;
	}

	/**
	 * 遍历inputs，验证用户的输入，输入值从控件获取
	 * 
	 * @param inputs
	 * @return
	 */
	public static boolean startValidatingForInputs(
			LinkedHashMap<String, Object> inputs) {
		boolean b = true;
		Iterator<Entry<String, Object>> iterator = inputs.entrySet().iterator();
		while (iterator.hasNext()) {
			Object object = iterator.next().getValue();
			if (object instanceof ToolParameter) {
				ToolParameter toolParameter = (ToolParameter) object;
				if (!toolParameter.startValidating()) {
					b = false;
				}
			} else if (object instanceof Repeat) {
				Repeat repeat = (Repeat) object;
				if (!repeat.startValidating()) {
					b = false;
				}
			} else if (object instanceof Conditional) {
				Conditional conditional = (Conditional) object;
				if (!conditional.startValidating()) {
					b = false;
				}
			} else {
				throw new RuntimeException("unknown inputs");
			}
		}
		return b;
	}

	/**
	 * 遍历inputs，验证 保存在成员变量中的输入值是否满足限定
	 * 
	 * @param inputs
	 * @return
	 */
	public static boolean validateSavedValueForInputs(
			LinkedHashMap<String, Object> inputs) {
		boolean b = true;
		Iterator<Entry<String, Object>> iterator = inputs.entrySet().iterator();
		while (iterator.hasNext()) {
			Object object = iterator.next().getValue();
			if (object instanceof ToolParameter) {
				ToolParameter toolParameter = (ToolParameter) object;
				if (!toolParameter.validateSavedValue()) {
					b = false;
				}
			} else if (object instanceof Repeat) {
				Repeat repeat = (Repeat) object;
				if (!repeat.validateSavedValue()) {
					b = false;
				}
			} else if (object instanceof Conditional) {
				Conditional conditional = (Conditional) object;
				if (!conditional.validateSavedValue()) {
					b = false;
				}
			} else {
				throw new RuntimeException("unknown inputs");
			}
		}
		return b;
	}

	/**
	 * 替换文件输入文件类型参数DataToolParameter的样式，用于工作流模式，在控件被创建之前注册
	 */
	public static void registerLabelDataWidgetForInputs(
			LinkedHashMap<String, Object> inputs) {
		Iterator<Entry<String, Object>> iterator = inputs.entrySet().iterator();
		while (iterator.hasNext()) {
			Object object = iterator.next().getValue();
			if (object instanceof ToolParameter) {
				if (object instanceof DataToolParameter) {
					DataToolParameter dataToolParameter = (DataToolParameter) object;
					dataToolParameter.registerLabelDataWidget();
				}
			} else if (object instanceof Repeat) {
				Repeat repeat = (Repeat) object;
				repeat.registerLabelDataWidget();
			} else if (object instanceof Conditional) {
				Conditional conditional = (Conditional) object;
				conditional.registerLabelDataWidget();
			} else {
				throw new RuntimeException("unknown inputs");
			}
		}
	}

	/**
	 * 对inputs注册始终执行检查，在控件被创建之前注册或者创建之后注册，只能调用一次。
	 * 
	 * @param inputs
	 */
	public static void registerAlwaysValidatingForInputs(
			LinkedHashMap<String, Object> inputs) {
		Iterator<Entry<String, Object>> iterator = inputs.entrySet().iterator();
		while (iterator.hasNext()) {
			Object object = iterator.next().getValue();
			if (object instanceof ToolParameter) {
				ToolParameter toolParameter = (ToolParameter) object;
				toolParameter.registerAlwaysValidating();
			} else if (object instanceof Repeat) {
				Repeat repeat = (Repeat) object;
				repeat.registerAlwaysValidating();
			} else if (object instanceof Conditional) {
				Conditional conditional = (Conditional) object;
				conditional.registerAlwaysValidating();
			} else {
				throw new RuntimeException("unknown inputs");
			}
		}
	}

	/**
	 * 清除输入文件类型参数DataToolParameter保存在成员变量中的输入值，
	 * 用于工作流模式下conditional切换时清空输入文件类型实例的状态。
	 * 
	 * @param inputs
	 */
	public static void clearDataToolParameterSavedVauleForInputs(
			LinkedHashMap<String, Object> inputs) {
		Iterator<Entry<String, Object>> iterator = inputs.entrySet().iterator();
		while (iterator.hasNext()) {
			Object object = iterator.next().getValue();
			if (object instanceof ToolParameter) {
				if (object instanceof DataToolParameter) {
					DataToolParameter dataToolParameter = (DataToolParameter) object;
					dataToolParameter.clearSavedVaule();
				}
			} else if (object instanceof Repeat) {
				Repeat repeat = (Repeat) object;
				repeat.clearDataToolParameterSavedVaule();
			} else if (object instanceof Conditional) {
				Conditional conditional = (Conditional) object;
				conditional.clearDataToolParameterSavedVaule();
			} else {
				throw new RuntimeException("unknown inputs");
			}
		}
	}

	/**
	 * 根据输入部分的有序字典inputs，获取到对应的XMLBeans对象
	 * 
	 * @param inputs
	 * @return
	 */
	public static InputsType getInputsTypeFromInputs(
			LinkedHashMap<String, Object> inputs) {
		InputsType inputsType = InputsType.Factory.newInstance();
		ArrayList<XmlObject> xmlObjects = getXmlObjectsFromInputs(inputs);
		for (XmlObject child : xmlObjects) {
			Utils.addInputsXMLObjectsToParentObject(inputsType, child);
		}
		return inputsType;
	}

	/**
	 * 根据输入部分的有序字典inputs，获取到DataToolParameter对象封装类list，保存了对应的引用和标签信息，
	 * 在doSave之后调用
	 * 
	 * @param inputs
	 * @param labelPrefix
	 * @return
	 */
	public static ArrayList<DataToolParameterEncapsulation> getDataToolParameterEncapsulationListFromInputs(
			LinkedHashMap<String, Object> inputs, String labelPrefix) {
		ArrayList<DataToolParameterEncapsulation> list = new ArrayList<DataToolParameterEncapsulation>();

		Iterator<Entry<String, Object>> iterator = inputs.entrySet().iterator();
		while (iterator.hasNext()) {
			Object object = iterator.next().getValue();
			if (object instanceof ToolParameter) {
				if (object instanceof DataToolParameter) {
					DataToolParameter dataToolParameter = (DataToolParameter) object;
					String label = labelPrefix + dataToolParameter.getLabel();
					list.add(new DataToolParameterEncapsulation(label,
							dataToolParameter));
				}
			} else if (object instanceof Repeat) {
				Repeat repeat = (Repeat) object;
				list.addAll(repeat
						.getDataToolParameterEncapsulationList(labelPrefix));
			} else if (object instanceof Conditional) {
				Conditional conditional = (Conditional) object;
				list.addAll(conditional
						.getDataToolParameterEncapsulationList(labelPrefix));
			} else {
				throw new RuntimeException("unknown inputs");
			}
		}
		return list;
	}

	/**
	 * 验证模块XML
	 * 
	 * @param toolXml
	 * @throws Exception
	 */
	public static void validateToolXML(String toolXml) throws Exception {
		String xsdPath = ConfigFile.getProjectFolderPath()
				+ "/configs/tool.xsd";
		File documentFile = new File(toolXml);
		File schemaFile = new File(xsdPath);

		SchemaFactory factory = SchemaFactory
				.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = null;
		try {
			schema = factory.newSchema(schemaFile);
		} catch (SAXException e) {
			fail(e);
		}
		Validator validator = schema.newValidator();
		SAXSource source = new SAXSource(new InputSource(new FileReader(
				documentFile)));

		try {
			validator.validate(source);
		} catch (SAXException e) {
			fail(e);
		}
	}

	private static void fail(SAXException e) throws Exception {
		if (e instanceof SAXParseException) {
			SAXParseException spe = (SAXParseException) e;
			throw new Exception("LineNumber: " + spe.getLineNumber()
					+ "  ColumnNumber: " + spe.getColumnNumber() + "\n"
					+ spe.getMessage() + "\n"

			);
		} else {
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * 根据输出部分的有序字典outputs，获取到对应的XMLBeans对象
	 * 
	 * @param outputs
	 * @return
	 */
	public static OutputsType getOutputsTypeFromOutputs(
			LinkedHashMap<String, ToolOutput> outputs) {
		OutputsType outputsType = OutputsType.Factory.newInstance();
		Iterator<Entry<String, ToolOutput>> iterator = outputs.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			ToolOutput toolOutput = iterator.next().getValue();
			if (toolOutput instanceof DataToolOutput) {
				DataType dataType = ((DataToolOutput) toolOutput)
						.saveAsDataType();
				outputsType.addNewData().set(dataType);
			} else if (toolOutput instanceof HtmlToolOutput) {
				HtmlType htmlType = ((HtmlToolOutput) toolOutput)
						.saveAsHtmlType();
				outputsType.addNewHtml().set(htmlType);
			} else {
				throw new RuntimeException("unknown output");
			}
		}
		return outputsType;
	}
}
