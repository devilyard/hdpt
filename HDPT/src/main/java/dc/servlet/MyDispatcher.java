package dc.servlet;

import java.util.LinkedHashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.DispatcherServlet;

import ctd.config.ConfigController;
import ctd.servlet.SystemInitialize;
import dc.config.layout.LayoutConfigController;
import dc.config.module.ModuleConfigController;
import dc.util.ServletContextHolder;
import dc.util.SystemArgsHolder;

public class MyDispatcher extends DispatcherServlet {

	private static final long serialVersionUID = 9197524004386412326L;
	private String encoding;

	@Override
	public void init(ServletConfig config) throws ServletException {
		encoding = config.getInitParameter("encoding");
		String wEBROOT = config.getServletContext().getRealPath("/");
		SystemArgsHolder.setWEB_ROOT(wEBROOT);
		ServletContext servletContext = config.getServletContext();
		ServletContextHolder.setServletContext(servletContext);

		super.init(config);
		LinkedHashMap<String, ConfigController> cs = new LinkedHashMap<String, ConfigController>();
		cs.put("server.moduleConfigHome", ModuleConfigController.instance());
		cs.put("server.layoutConfigHome", LayoutConfigController.instance());
		SystemInitialize.setCustomcontrollers(cs);
		SystemInitialize.init(config);
	}
	
	protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {  
		request.setCharacterEncoding(encoding);  
		super.doService(request, response);  
	} 

}
