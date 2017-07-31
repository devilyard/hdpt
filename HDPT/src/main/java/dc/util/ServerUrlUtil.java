package dc.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

/**
 * 服务容器工具类
 * @author WYI
 *
 */
public class ServerUrlUtil {
	
	public static final String IMG_REG = "<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";
	/**
	 * 文章多图片时分隔符
	 */
	public static final String SEPARATOR = " ";
	/**
	 * 转换微信图片时的后缀
	 */
	public static final String WX_SUFFIX = "_wx";
	
	/**
	 * 获取服务地址
	 * @param request	如http://192.168.1.1:8080
	 * @return
	 */
	public static String getServerUrl(HttpServletRequest request){
		if(request == null){
			return null;
		}
		return request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
	}
	/**
	 * 识别并补全内容中的img标签src
	 * @param request
	 * @param content
	 * @return
	 */
	public static String addImgServerUrl(HttpServletRequest request, String content){
		String serUrl = ServerUrlUtil.getServerUrl(request);
		if(serUrl == null || content == null){
			return content;
		}
		Pattern p = Pattern.compile(IMG_REG);
		Matcher m = p.matcher(content);
		StringBuilder sb = new StringBuilder(content);
		String tempStr = null;
		while(m.find()){
			tempStr = m.group(1);
			int i = sb.indexOf(tempStr);
			sb.insert(i, serUrl);
		}
		
		return sb.toString();
	}

	/**
	 * 处理并拼装微信用图片地址
	 * 暂用于文章中url字段
	 * @param urls
	 * @param request
	 * @return
     */
	public static String convImgPath4Wx(String urls, HttpServletRequest request){
		if(urls == null || urls.isEmpty()){
			return null;
		}
		StringBuilder urlArray = new StringBuilder();
		for(String urlStr: urls.split(SEPARATOR)){
			StringBuilder urlTemp = new StringBuilder(urlStr);
			urlTemp.insert(0, ServerUrlUtil.getServerUrl(request));
			int pointIdx = urlTemp.lastIndexOf(".");
			urlTemp.insert(pointIdx, WX_SUFFIX).append(SEPARATOR);
			urlArray.append(urlTemp);
		}
		urlArray.deleteCharAt(urlArray.length()-1);
		return urlArray.toString();
	}

	/**
	 * 处理文章内容中的图片地址
	 * 暂用于文章中的内容字段
	 * @param content
	 * @param request
     * @return
     */
	public static String convContentImg4Wx(String content, HttpServletRequest request){
		String serUrl = ServerUrlUtil.getServerUrl(request);
		if(serUrl == null || content == null){
			return content;
		}
		Pattern p = Pattern.compile(IMG_REG);
		Matcher m = p.matcher(content);
		StringBuilder sb = new StringBuilder(content);
		StringBuilder tempStr = null;
		while(m.find()){
			tempStr = new StringBuilder(m.group(1));
			int serverIdx = sb.indexOf(tempStr.toString());
			int pointIdx = tempStr.lastIndexOf(".");
			sb.insert(serverIdx, serUrl);
			sb.insert(serverIdx + serUrl.length() + pointIdx, WX_SUFFIX);
		}
		return sb.toString();
	}
}
