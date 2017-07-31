package dc.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class proxy extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Logger logger =  LoggerFactory
			.getLogger(this.getClass().getName());
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
			doPost(request,response);	
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("GBK");
		String url = request.getParameter("url");
		if(url!=null){
			url= url.replaceAll("UUID", request.getSession().getAttribute("sessionId")+"-"+request.getSession().getAttribute("mpiId"));
		}
		System.out.println(url);
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			if (false == load(url, out)) {
				out.write("0".getBytes());
//				response.sendError(404);
			}
		} catch (IOException e) {
			out.write("0".getBytes());
		}
		if (out != null) {
			try {
				out.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public boolean load(String Url, OutputStream outputStream)
			throws IOException {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			URL url = new URL(Url);
			InputStream imageIn = url.openStream();// 文件流
			bis = new BufferedInputStream(imageIn);
			byte data[] = new byte[4096];
			int size = 0;
			bos = new BufferedOutputStream(outputStream);
			size = bis.read(data);
			while (size != -1) {
				bos.write(data, 0, size);
				size = bis.read(data);
			}
			bos.flush();// 清空输出缓冲流
			return true;
		} finally {
			if (bis != null) {
				bis.close();
			}
			if (bos != null) {
				bos.close();
			}
		}
	}

	/**
	 * 读取url返回数据
	 * @param url
	 * @return
	 */
	public static  String readURL( String url){
	  try{
		  URL url_obj=new URL(url);
		   InputStreamReader isr=new InputStreamReader(url_obj.openStream(),"utf-8");
		   BufferedReader br=new BufferedReader(isr);
		   String str;
		   StringBuffer all_content = new StringBuffer();
		   while((str=br.readLine())!=null)
		   {
			   all_content.append(str);
		   }
			   br.close();
			   isr.close();
			   return all_content.toString();
	 }
	   catch(Exception e)
	   {
			return e.getMessage();
	   }

	 }
}
