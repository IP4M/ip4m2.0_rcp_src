package com.sixthhosp.gcmpa.tools.template;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class TemplateEngine {

	/**
	 * �������ģ�ͺ�ģ���ַ������õ���ֵ�滻֮��Ľ��,ģ���������Velocity����.
	 * 
	 * @param dataModel
	 * @param template
	 * @return
	 */
	public static String combineDataModelAndTemplate(
			HashMap<String, Object> dataModel, String template) {
		// ��ʼ������
		VelocityEngine velocityEngine = new VelocityEngine();
		velocityEngine.init();

		// ��ʼ������������
		VelocityContext context = new VelocityContext();
		if (dataModel != null) {
			Iterator<Entry<String, Object>> iterator = dataModel.entrySet()
					.iterator();
			while (iterator.hasNext()) {
				Entry<String, Object> entry = iterator.next();
				String key = entry.getKey();
				Object object = entry.getValue();
				context.put(key, object);
			}
		}
		// ��Ӿ�̬������������Ҫ������չ
		context.put("Math", Math.class);
		context.put("StringUtils", StringUtils.class);
		context.put("FilenameUtils", FilenameUtils.class);

		// �½��ַ��������
		StringWriter stringWriter = new StringWriter();

		// ��ֵ�滻
		velocityEngine.evaluate(context, stringWriter, "", template);

		// ����滻֮����ַ�
		return stringWriter.toString();
	}
}
