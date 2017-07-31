package dc.util;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mtcservers.cn.hzcr.monitor.message.SendMessage;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Util {

	private static Logger logger = Logger.getLogger(Util.class);
	
	public static boolean empty(Object obj)
	  {
	    if (obj == null)
	      return true;
	    if (((obj instanceof String)) && ((obj.equals("")) || (obj.equals("0"))))
	      return true;
	    if (((obj instanceof Number)) && (((Number)obj).doubleValue() == 0.0D))
	      return true;
	    if (((obj instanceof Boolean)) && (!((Boolean)obj).booleanValue()))
	      return true;
	    if (((obj instanceof Collection)) && (((Collection)obj).isEmpty()))
	      return true;
	    if (((obj instanceof Map)) && (((Map)obj).isEmpty()))
	      return true;
	    if (((obj instanceof Object[])) && (((Object[])obj).length == 0)) {
	      return true;
	    }
	    return false;
	  }
	
	public static String getUuid() {
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	public static List<HashMap<String,Object>> RSSTest(String url)
		    throws DocumentException
	  {
	    List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
	    
	    try {
			String msg = new SendMessage().doPost(url, null);
			if(msg != null){
				JSONObject obj = JSON.parseObject(msg);
				if(obj.get("rows") != null){
					JSONArray rows = JSONArray.parseArray(String.valueOf(obj.get("rows")));
					for(int i=0; i<7; i++){
						Object row = rows.get(i);
						JSONObject o = JSON.parseObject(String.valueOf(row));
						HashMap<String,Object> map = new HashMap<String,Object>();
						map.put("title",String.valueOf(o.get("title")));
						map.put("pubDate",String.valueOf(o.get("date")));
						map.put("link",ConfigUtil.getValue("healthe.ducation.url")+String.valueOf(o.get("id")));
						list.add(map);
					}
				}
			}
			
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    
	    return list;
	  }
	
	public static String html2Text(String inputString)
	  {
	    String htmlStr = inputString;
	    String textStr = "";
	    try
	    {
	      String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
	      String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
	      String regEx_html = "<[^>]+>";

	      Pattern p_script = Pattern.compile(regEx_script, 2);
	      Matcher m_script = p_script.matcher(htmlStr);
	      htmlStr = m_script.replaceAll("");

	      Pattern p_style = Pattern.compile(regEx_style, 2);
	      Matcher m_style = p_style.matcher(htmlStr);
	      htmlStr = m_style.replaceAll("");

	      Pattern p_html = Pattern.compile(regEx_html, 2);
	      Matcher m_html = p_html.matcher(htmlStr);
	      htmlStr = m_html.replaceAll("");

	      textStr = htmlStr;
	    }
	    catch (Exception e) {
	      logger.error("HTML2TEXT ERROR", e);
	    }
	    htmlStr = null;
	    return textStr;
	  }
	
	public static String dateTostring(java.util.Date date, String typ) {
	    String sdate = new SimpleDateFormat(typ).format(date);
	    return sdate;
	}
	
	public static String getImgStr(String htmlStr){   
		String url = "";
		Pattern p = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");//<img[^<>]*src=[\'\"]([0-9A-Za-z.\\/]*)[\'\"].(.*?)>");
		Matcher m = p.matcher(htmlStr);
		while(m.find()){
			url += m.group(1) + " ";
		}
		return url; 
    } 
}
