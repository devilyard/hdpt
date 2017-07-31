package dc.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bsoft.adapter.ws.WebServiceEntryServiceStub;
import com.bsoft.xds.support.dao.IDAO;

import ctd.dictionary.Dictionaries;
import ctd.dictionary.Dictionary;
import ctd.dictionary.DictionaryItem;
import ctd.util.JSONUtils;
import dc.domain.main.JmhdSysModule;
import dc.domain.ws.WsServiceAddress;
import dc.util.HttpHelper;


@Controller
public class JmhdOndemandCheckReport {
	
	@Autowired
	private IDAO serviceDao;

	/**
	 * 调用ondemand接口实时获取检查数据
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping(value = "/jmhdondemand/getCheckReport")
	public void creatView(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
			
			long wstype = 3;
			String wsname = "ondemand";
			WsServiceAddress wsa = serviceDao.queryT("WsServiceAddress", " enabled=1 and wstype=? and wsname=? ", new Object[]{wstype, wsname});
			
			String appid = wsa.getAppid();
			String pwd = wsa.getSectrekey();
			String service = "adapter.rpcCallService";
			String method = "invocationDSDetailCapture";
			String effectiveTime = "2016-12-16";//测试数据
			effectiveTime =request.getParameter("checkDate");
			String authororganization = "47011661433010211A1001";//测试数据
			authororganization =request.getParameter("organizationcode");
			String reqxml = "<Request><Header>"
					+ "<DocInfo><RecordClassifying>Pt_ExamReport</RecordClassifying><EffectiveTime>"+effectiveTime+"</EffectiveTime>"
					+ "<Authororganization>"+authororganization+"</Authororganization></DocInfo><Patient><CardNo>"
					+ "</CardNo><CardType></CardType> <IdCard>442000197203294623</IdCard> <IdType>1</IdType>"
					+ "</Patient></Header>"
					+ "</Request>";
			
			String xml = "";
			try {
			String url = wsa.getUrl();
			WebServiceEntryServiceStub stub = new WebServiceEntryServiceStub(url);
			WebServiceEntryServiceStub.Invoke iv = new WebServiceEntryServiceStub.Invoke();
			iv.setArg0(appid);
			iv.setArg1(pwd);
			iv.setArg2(service);
			iv.setArg3(method);
			WebServiceEntryServiceStub.StringArray sa = new WebServiceEntryServiceStub.StringArray();
			sa.addItem(reqxml);
			iv.setArg4(sa);
			WebServiceEntryServiceStub.InvokeResponse res = stub.invoke(iv);
			xml = res.get_return();
			System.out.println(xml);
			} catch (Exception e) {
				xml = "";
			}
			
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print(xml);
			
		

	}
	
	@RequestMapping(value = "/jmhdondemand/getOrganizationList")
	public void getOrganizationList(HttpServletRequest request,HttpServletResponse response)
			throws IOException {
		try{
			Dictionary organization = Dictionaries.instance().getDic("organization");
			// 获取XML文件
			Document doc = organization.getDefineDoc();
			HashMap<String, Object> body = new HashMap<String, Object>();
			List<DictionaryItem> organizationList = organization.itemsList();
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("organizationList", organizationList);
			map.put("success", true);
			HttpHelper.writeHdptJSON(map, response,true);
		}catch (Exception e) {
			HttpHelper.writeHdptJSON(null, response,false);
		}
	}
	
}
