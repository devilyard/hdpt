package dc.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.dom4j.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import dc.auth.HttpSessionHolder;
import dc.util.uuid.UUID;

public class HttpHelper {

	private static final Logger logger = LoggerFactory
			.getLogger(HttpHelper.class);

	public static final String SESSIONID_KEY_NAME = "SSSID";

	private final static String CODE = "code";
	private final static String MSG = "x-response-msg";
	private final static String BODY = "body";
	public final static int SUCCESS_CODE = 200;
	public final static String SUCCESS_MSG = "success";
	private static ObjectMapper jsonMapper = new ObjectMapper();

	static {
		jsonMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
				false);
		jsonMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	}

	public static void writeXML(Document doc, HttpServletResponse res) {
		writeXML(doc.asXML(), res);
	}

	public static void writeXML(String xml, HttpServletResponse res) {
		res.setContentType("text/xml;charset=UTF-8");
		try {
			res.getWriter().print(xml);
		} catch (IOException e) {
			logger.error("Failed to write xml response data.", e);
		}
	}

	public static void writeHtml(String html, HttpServletResponse res) {
		res.setContentType("text/html;charset=UTF-8");
		try {
			res.getWriter().print(html);
		} catch (IOException e) {
			logger.error("Failed to write html response data.", e);
		}
	}

	public static void writeJSON(Object body, HttpServletResponse res) {
		writeJSON(SUCCESS_CODE, SUCCESS_MSG, body, res);
	}

	public static void writeJSON(int code, String msg, Object body,
			HttpServletResponse res) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(CODE, code);
		map.put(MSG, msg);
		map.put(BODY, body);
		writeJSONOnly(map, res);
	}

	/**
	 * 把制定map写入response
	 * @param map
	 * @param res
     */
	public static void writeJSONOnly(Map<String, Object> map, HttpServletResponse res){
		res.setContentType("text/javascript");
		OutputStreamWriter out;
		try {
			out = new OutputStreamWriter(res.getOutputStream(), "UTF-8");
			jsonMapper.writeValue(out, map);
		} catch (Exception e) {
			logger.error("Failed to write json response data.", e);
		}
	}
	
	
	public static void writeHdptJSON(Object body,HttpServletResponse res,boolean flag) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(SUCCESS_MSG, flag);
		map.put("data", body);
		res.setContentType("text/javascript");
		OutputStreamWriter out;
		try {
			out = new OutputStreamWriter(res.getOutputStream(), "UTF-8");
			jsonMapper.writeValue(out, map);
		} catch (Exception e) {
			logger.error("Failed to write json response data.", e);
		}
	}

	@SuppressWarnings("unchecked")
	public static HashMap<String, Object> parseRequest(
			HttpServletRequest request) {
		try {
			String encoding = request.getCharacterEncoding();
			if (encoding == null) {
				encoding = "UTF-8";
			}
			InputStream ins = request.getInputStream();
			return jsonMapper.readValue(ins, HashMap.class);
		} catch (Exception e) {
			logger.error("parseRequest failed:", e);
		}
		return new HashMap<String, Object>();
	}

	/**
	 * @param args
	 * @param uuid
	 * @return
	 */
	public static String getSessionID(HttpServletRequest request, UUID uuid) {
		String sessionID = (uuid == null ? null : uuid.getSessionID());
		if (sessionID != null) {
			return sessionID;
		}
		sessionID = (String) request
				.getAttribute(HttpHelper.SESSIONID_KEY_NAME);
		if (sessionID != null) {
			return sessionID;
		}
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie c : cookies) {
				if (SESSIONID_KEY_NAME.equals(c.getName())) {
					sessionID = c.getValue();
					if (sessionID.equals("")){
						sessionID = null;
					}
					break;
				}
			}
		}
		if (sessionID == null) {
			sessionID = request.getSession().getId();
		}
		return sessionID;
	}

	/**
	 * @param request
	 * @param uuid
	 * @return
	 */
	public static HttpSession getSession(HttpServletRequest request, UUID uuid) {
		return HttpSessionHolder.get(getSessionID(request, uuid));
	}
}
