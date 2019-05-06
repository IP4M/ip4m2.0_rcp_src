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
	 * 结合数据模型和模板字符串，得到插值替换之后的结果,模板引擎采用Velocity引擎.
	 * 
	 * @param dataModel
	 * @param template
	 * @return
	 */
	public static String combineDataModelAndTemplate(
			HashMap<String, Object> dataModel, String template) {
		// 初始化引擎
		VelocityEngine velocityEngine = new VelocityEngine();
		velocityEngine.init();

		// 初始化数据上下文
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
		// 添加静态方法，根据需要进行扩展
		context.put("Math", Math.class);
		context.put("StringUtils", StringUtils.class);
		context.put("FilenameUtils", FilenameUtils.class);

		// 新建字符串输出流
		StringWriter stringWriter = new StringWriter();

		// 插值替换
		velocityEngine.evaluate(context, stringWriter, "", template);

		// 输出替换之后的字符
		return stringWriter.toString();
	}
}
