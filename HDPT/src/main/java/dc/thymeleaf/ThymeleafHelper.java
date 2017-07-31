package dc.thymeleaf;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.IContext;

import dc.util.EncodingDetect;
import dc.util.XMLHelper;

public class ThymeleafHelper {

	private TemplateEngine htmlTemplateEngine;
	private TemplateEngine xmlTemplateEngine;

	/**
	 * 获得html模版
	 * 
	 * @param tplName
	 *            html文件名
	 * @param data
	 *            数据集
	 * @return
	 */
	public String getHtml(String tplName, Map<String, Object> data) {
		return getTemplate(tplName, data, htmlTemplateEngine);
	}

	/**
	 * 获得html模版。
	 * 
	 * @param tplName
	 *            html模板文件名。
	 * @param xmlData
	 *            数据
	 * @return
	 * @throws DocumentException
	 */
	public String getHtml(String tplName, Element xmlData)
			throws DocumentException {
		return getTemplate(tplName, xmlData, htmlTemplateEngine);
	}

	/**
	 * @param tplName
	 *            html模板文件名
	 * @param data
	 *            数据
	 * @param xmlData
	 *            xml数据
	 * @return
	 * @throws DocumentException
	 */
	public String getHtml(String tplName, Map<String, Object> data,
			byte[] xmlData) throws DocumentException {
		return getTemplate(tplName, data, xmlData, htmlTemplateEngine);
	}

	/**
	 * 获得xml模版
	 * 
	 * @param tplName
	 *            xml模板文件名
	 * @param data
	 *            数据集
	 * @return
	 */
	public String getXml(String tplName, Map<String, Object> data) {
		return getTemplate(tplName, data, xmlTemplateEngine);
	}

	/**
	 * 获得xml模版。
	 * 
	 * @param tplName
	 *            xml模板文件名
	 * @param xmlData
	 *            数据
	 * @return
	 * @throws DocumentException
	 */
	public String getXml(String tplName, byte[] xmlData)
			throws DocumentException {
		return getTemplate(tplName, xmlData, xmlTemplateEngine);
	}

	/**
	 * 获得模版字符串
	 * 
	 * @param tplName
	 * @param data
	 * @param engine
	 * @return
	 */
	private String getTemplate(String tplName, Map<String, Object> data,
			TemplateEngine engine) {
		IContext ctx = new ThymWebContext(data);
		return engine.process(tplName, ctx);
	}

	/**
	 * @param tplName
	 * @param xmlData
	 * @param engine
	 * @return
	 * @throws DocumentException
	 */
	private String getTemplate(String tplName, byte[] xmlData,
			TemplateEngine engine) throws DocumentException {
		ThymWebContext ctx = new ThymWebContext();
		ctx.setVariable("doc", getDocument(xmlData).getRootElement());
		return engine.process(tplName, ctx);
	}

	private String getTemplate(String tplName, Element xmlData,
			TemplateEngine engine) throws DocumentException {
		ThymWebContext ctx = new ThymWebContext();
		ctx.setVariable("doc", xmlData);
		return engine.process(tplName, ctx);
	}

	private String getTemplate(String tplName, Map<String, Object> data,
			byte[] xmlData, TemplateEngine engine) throws DocumentException {
		ThymWebContext ctx = new ThymWebContext();
		if (data != null && !data.isEmpty()) {
			ctx.setVariables(data);
		}
		if (xmlData != null && xmlData.length > 0) {
			ctx.setVariable("doc", getDocument(xmlData).getRootElement());
		}
		return engine.process(tplName, ctx);
	}

	/**
	 * @param xml
	 * @return
	 * @throws DocumentException
	 */
	private Document getDocument(byte[] xml) throws DocumentException {
		InputStream ins = new ByteArrayInputStream(xml);
		return XMLHelper.getDocument(ins, EncodingDetect.getJavaEncode(xml));
	}

	/**
	 * 解析xml文件
	 * 
	 * @param xml
	 * @return Map<String,Object>
	 * @throws DocumentException
	 */
	public Map<String, Object> xmlParseMap(String xml) throws DocumentException {
		Document doc = DocumentHelper.parseText(xml);
		Element e = doc.getRootElement();
		return elementToMap(e);
	}

	/**
	 * 解析xml节点
	 * 
	 * @param e
	 * @return Map<String,Object>
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> elementToMap(Element e) {
		Map<String, Object> body = new HashMap<String, Object>();
		Iterator<Element> it = e.elementIterator();
		while (it.hasNext()) {

			Element el = it.next();
			List<Element> subs = el.elements();
			if ("Group".equals(el.attributeValue("type"))) {
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				for (Element subEl : subs) {
					list.add(elementToMap(subEl));
				}
				body.put(el.getName().toUpperCase(), list);
			} else if (subs.size() > 0) {
				List<Element> ls = e.selectNodes(el.getName());
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				int i = 0;
				for (Element El : ls) {
					i++;
					list.add(elementToMap(El));
					if (i < ls.size()) {
						it.next();
					}
				}
				body.put(el.getName(), list);
				body.put(el.getName() + "_SIZE", i);
			} else {
				body.put(el.getName(), el.getText());
				if (el.attribute("display") != null) {
					body.put(el.getName() + "_TEXT", el.attribute("display")
							.getValue());
				}
			}
		}
		return body;
	}

	public TemplateEngine getHtmlTemplateEngine() {
		return htmlTemplateEngine;
	}

	public void setHtmlTemplateEngine(TemplateEngine htmlTemplateEngine) {
		this.htmlTemplateEngine = htmlTemplateEngine;
	}

	public TemplateEngine getXmlTemplateEngine() {
		return xmlTemplateEngine;
	}

	public void setXmlTemplateEngine(TemplateEngine xmlTemplateEngine) {
		this.xmlTemplateEngine = xmlTemplateEngine;
	}

	/**
	 * 个人基本信息
	 * 
	 * @param templateName
	 * @param detailInfo
	 * @param xml
	 * @return
	 * @throws DocumentException
	 */
	public String getAnalysisedHtml(String templateName,
			Map<String, Object> detailInfo, String xml)
			throws DocumentException {
		Document doc = DocumentHelper.parseText(xml);
		Element e = doc.getRootElement();
		Map<String, Object> newMap = new HashMap<String, Object>();
		Map<String, Object> effectiveMap = new HashMap<String, Object>();
		newMap = elementToMap(e);
		effectiveMap = getEffectValue(newMap);
		Set<Entry<String, Object>> set = effectiveMap.entrySet();
		for (Iterator<Entry<String, Object>> it = set.iterator(); it.hasNext();) {
			Entry<String, Object> entry = (Entry<String, Object>) it.next();
			detailInfo.put(entry.getKey(), entry.getValue());
		}
		return getTemplate(templateName, detailInfo, xml.getBytes(),
				htmlTemplateEngine);
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> getEffectValue(Map<String, Object> newMap) {
		Map<String, Object> rMap = new HashMap<String, Object>();
		String key = "";
		Object value = null;
		Set<Entry<String, Object>> set = ((Map<String, Object>) ((HashMap<String, Object>) newMap)
				.clone()).entrySet();
		for (Iterator<Entry<String, Object>> it = set.iterator(); it.hasNext();) {
			Entry<String, Object> entry = (Entry<String, Object>) it.next();
			if (entry.getKey().equals("EHR_PASTHISTORY")) {
				ArrayList<HashMap<String, String>> past = new ArrayList<HashMap<String, String>>();
				past = (ArrayList<HashMap<String, String>>) entry.getValue();
				for (int i = 0; i < past.size(); i++) {
					key = "PastHistoryCode"
							+ past.get(i).get("PastHistoryCode");
					value = past.get(i).get("DiseaseText");
					if (rMap.containsKey(key)) {
						value = (String) value + "、" + rMap.get(key);
					}
					rMap.put(key, value);
				}
			}
		}
		return rMap;
	}

	public String getDiseaseCourseHtml(String tplName,
			Map<String, Object> data, String xml) throws DocumentException {
		Map<String, Object> DCMap = new HashMap<String, Object>();
		DCMap = toMap(xml);
		data.put("DiseaseCourse", DCMap.get("DiseaseCourse"));
		return getTemplate(tplName, data, xml.getBytes(), htmlTemplateEngine);
	}

	public static Map<String, Object> toMap(String xml) {
		Map<String, Object> map = new HashMap<String, Object>();
		String DiseaseCourse = "";
		Document doc = null;
		String str = null;
		try {
			doc = DocumentHelper.parseText(xml);
			Element el = doc.getRootElement();
			Element el1 = (Element) el.selectSingleNode("DiagnosisDate");
			if (el1 == null) {
				el1 = (Element) el.selectSingleNode("ConfirmDate");
			}
			//取得确诊时间
			str = el1.getStringValue();
			if (str.equals("")) {
				DiseaseCourse = "未获得确诊时间";
			} else {
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				// 取得现在的时间
				String end = sf.format(new Date());
				DiseaseCourse = remainDateToString(str, end);
			}
			if (DiseaseCourse.equals("")) {
				DiseaseCourse = "未满一个月";
			}
			map.put("DiseaseCourse", DiseaseCourse);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return map;
	}

	private static String remainDateToString(String str, String end) {
		Calendar calS = Calendar.getInstance();
		java.util.Date startDate = null;
		java.util.Date endDate = null;
		StringBuilder sBuilder = new StringBuilder();
		try {
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(str);
			endDate = new SimpleDateFormat("yyyy-MM-dd").parse(end);
			if(startDate.after(endDate)){
				return "数据有误";
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
		calS.setTime(startDate);
		int startY = calS.get(Calendar.YEAR);
		int startM = calS.get(Calendar.MONTH);
		int startD = calS.get(Calendar.DATE);
		int startDayOfMonth = calS.getActualMaximum(Calendar.DAY_OF_MONTH);

		calS.setTime(endDate);
		int endY = calS.get(Calendar.YEAR);
		int endM = calS.get(Calendar.MONTH);
		// 处理2011-01-10到2011-01-10，认为一天
		int endD = calS.get(Calendar.DATE) + 1;
		int endDayOfMonth = calS.getActualMaximum(Calendar.DAY_OF_MONTH);

		if (endDate.compareTo(startDate) < 0) {
			return "";
		}
		int lday = endD - startD;
		if (lday < 0) {
			endM = endM - 1;
			lday = startDayOfMonth + lday;
		}
		// 处理天数问题，如：2011-01-01 到 2013-12-31 2年11个月31天 实际上就是3年
		if (lday == endDayOfMonth) {
			endM = endM + 1;
			lday = 0;
		}
		int mos = (endY - startY) * 12 + (endM - startM);
		int lyear = mos / 12;
		int lmonth = mos % 12;
		if (lyear > 0) {
			sBuilder.append(lyear + "年");
		}
		if (lmonth > 0) {
			sBuilder.append(lmonth + "个月");
		}
		return sBuilder.toString();
	}
	
	public String getComplicatedHtml(String tplName,
			Map<String, Object> data, byte[] xmlData1, byte[] xmlData2) throws DocumentException {
		return getComplicatedTemplate(tplName, data, xmlData1, xmlData2, htmlTemplateEngine);
	}

	private String getComplicatedTemplate(String tplName,
			Map<String, Object> data, byte[] xmlData1, byte[] xmlData2,
			TemplateEngine engine) throws DocumentException {
		ThymWebContext ctx = new ThymWebContext();
		if (data != null && !data.isEmpty()) {
			ctx.setVariables(data);
		}
		if (xmlData1 != null && xmlData1.length > 0) {
			ctx.setVariable("doc1", getDocument(xmlData1).getRootElement());
		}
		if (xmlData2 != null && xmlData2.length > 0) {
			ctx.setVariable("doc2", getDocument(xmlData2).getRootElement());
		}
		return engine.process(tplName, ctx);
	}
}
