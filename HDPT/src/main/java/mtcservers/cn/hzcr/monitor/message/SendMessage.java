package mtcservers.cn.hzcr.monitor.message;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * 发送数据核心调用方法
 * 
 * @author Administrator
 * 
 */
public class SendMessage {
	
	
	public static String sms_url = "http://106.ihuyi.cn/webservice/sms.php?method=Submit";
	public static String sms_account = "cf_rickywrx";
	public static String sms_psd = "sms.zgjkw.cn";
	public static String sms_template_code = "您的确认码是：【变量】。请不要把确认码泄露给其他人。如非本人操作，可不用理会！";
	public static String sms_template_psd = "您的新密码为【变量】。请不要泄露给其他人。请立即更改您的密码！";

	
	
	/**
	 * 
	 * @param mobileNumber传入对应的手机号
	 * @return 返回生成的随机数
	 */
	public int sendMessage(String mobileNumber) {
		final MessageOperation mo = new MessageOperation();
		System.out.println("------------------------init-------------");
		int connectRe = mo.jBtnInitActionPerformed();// 初始化服务
		System.out.println("------------------------sssss-------------");

		StringBuffer msg = new StringBuffer();
		int key = 0;
		try {
			if (connectRe == 0) {
				key = (int) ((Math.random() * 9 + 1) * 100000);// 返回的随机数值
				msg.append("尊敬的用户,您注册的中山市市属医院检验、体检报告查询系统验证码： ").append(key)
						.append("。请勿将验证码告知他人，并在5分钟内完成注册。");
				if (msg.length() > 0 && !"".equals(mobileNumber)) {
					int status = mo.sendActionPerformed(mobileNumber, null,
							msg.toString(), 10); // 用户注册短信
					if (status == 0) {
						System.out.println("发送成功");
					} else {
						System.out.println("发送失败");
						key = 0;
					}
				}
			}
		} catch (Exception e) {
			System.out.println("短信平台服务启用出错");
			key = 0;
			return key;
		} finally {
			mo.jBtnReleaseActionPerformed();// 释放服务
		}
		return key;
	}
	
	
	
	/**
	 * 发送短信
	 * 
	 * @param numb
	 * @param code
	 * @param kinds
	 * @return
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public String sendSmsCode(String numb, String code, int kinds)
			throws IOException, ParserConfigurationException, SAXException {
		Map<String, String> params = new HashMap<String, String>();
		String msg = "";
		params.put("account", sms_account);
		params.put("password", sms_psd);
		params.put("mobile", numb);
		switch (kinds) {
		case 1:
			params.put("content", sms_template_code.replace("【变量】", code));
			break;// 验证码
		case 2:
			params.put("content", sms_template_psd.replace("【变量】", code));
			break;// 密码
		default:
			break;
		}

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		StringReader sr = new StringReader(doPost(sms_url, params));
		InputSource is = new InputSource(sr);
		Document document = db.parse(is);
		Element rootElement = document.getDocumentElement();

		NodeList codeList = rootElement.getElementsByTagName("code");
		NodeList msgList = rootElement.getElementsByTagName("msg");
		if (codeList.getLength() > 0 && msgList.getLength() > 0) {
			Node codeNode = codeList.item(0);
			if ("2".equals(codeNode.getFirstChild().getTextContent())) {
				msg = "success";
			} else {
				msg = "error";
			}
		} else {
			msg = "error";
		}

		return msg;
	}
	
	
	public String doPost(String url, Map<String, String> params) throws UnsupportedEncodingException {
		HttpPost post = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		HttpClient hc = new DefaultHttpClient();
		if(params != null){
			Set<String> keySet = params.keySet();
			for (String key : keySet) {
				nvps.add(new BasicNameValuePair(key, params.get(key)));
			}
		}
		try {
			post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			HttpResponse response = hc.execute(post);
			try {
				HttpEntity entity = response.getEntity();
				try {
					if (entity != null) {
						String str = EntityUtils.toString(entity, "UTF-8");
						return str;
					}
				} finally {
					if (entity != null) {
						entity.getContent().close();
					}
				}
			} finally {
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			post.releaseConnection();
		}
		return "";
	}
}
