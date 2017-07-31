package dc.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bsoft.mpi.Constants;
import com.bsoft.mpi.server.rpc.IMPIProvider;
import com.bsoft.xds.ILabReportRetrieveService;
import com.bsoft.xds.exception.DocumentEntryException;
import com.bsoft.xds.support.dao.IDAO;

import dc.domain.main.YyUserAccount;
import dc.util.HttpHelper;
import dc.util.SecurityUtil;

@Controller
public class UrlLoginController {
	
	@Autowired
	private ILabReportRetrieveService PtLabReportService;
	
	@Autowired
	private IMPIProvider mpiService;
	
	@Autowired
	private IDAO serviceDao;

	@RequestMapping(value = "/urlLogin")
	public void fetchTopManaunit(HttpServletRequest request,HttpServletResponse response)
			throws IOException {
		try {
		YyUserAccount account = new YyUserAccount();
		String userinfo = checkEmpty(request.getParameter("userinfo"),"userinfo");
		userinfo = userinfo.replaceAll(" ", "+");
		String key = "bsoftlzjkw";
		byte[] keyBytes = SecurityUtil.getKeyBytes(key);
		byte[] srcBytes = SecurityUtil.decryptMode(keyBytes, SecurityUtil.Base642byte(userinfo));
		userinfo = new String(srcBytes,"UTF-8");
		JSONObject resJson = JSON.parseObject(userinfo);
		String logintype  = checkEmpty(resJson.get("logintype").toString(),"logintype");
		String password = checkEmpty(resJson.get("password").toString(),"password");
		String telphone  = checkEmpty(resJson.get("telphone").toString(),"telphone");
		// 需要注册时先注册用户
		if("1".equals(logintype)){
		String name = checkEmpty(resJson.get("name").toString(),"name");
		String sex = checkEmpty(resJson.get("sex").toString(),"sex");
		String cardtype = checkEmpty(resJson.get("cardtype").toString(),"cardtype");
		String cardno = checkEmpty(resJson.get("cardno").toString(),"cardno");
		String birthday = checkEmpty(resJson.get("birthday").toString(),"birthday");
		String nationality = checkEmpty(resJson.get("nationality").toString(),"nationality");
		
		//注册
		account = new YyUserAccount();
		account.setPhoneno(telphone);
		account.setUsername(name);
		account.setSex(sex);
		account.setCardtype(cardtype);
		account.setIdcard(cardno);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		account.setBirthday(sdf.parse(birthday));
		account.setPassword(password);
		account.setNationality(nationality);
		
		// 判断手机号码是否已经存在信息
		List<Map<String, Object>> userlist = serviceDao.queryForList("YyUserAccount", "phoneno=?",
				new Object[] { account.getPhoneno() });
		if (userlist != null && userlist.size() > 0) {
			throw new DocumentEntryException("手机号码已经存在，不能注册");
		}
		
		serviceDao.save("YyUserAccount", account);
		
		//获取mpi数据并插入jmhd_member表
		saveMpidInfo(account);
		
		}else{// 不需要注册时直接登录
			account = serviceDao.queryT("YyUserAccount", " phoneno=? and password=?", new Object[]{telphone,password});
			if(account==null){
				throw new DocumentEntryException("用户名密码错误！");
			}
		}
		
		//设置seesion信息用来在首页被调用
		setLoginSeesion(account,request);
		
		}catch (DocumentEntryException de) {
			request.getSession().setAttribute("urlloginerr", de.getMessage());
			response.sendRedirect("hdpt/error.html");
			return;
		}
		catch (Exception e) {
			request.getSession().setAttribute("urlloginerr", e.getMessage());
			response.sendRedirect("hdpt/error.html");
			return;
		}
		
		response.sendRedirect("hdpt/index.html");
	}
	
	public String checkEmpty(String value, String valuename) throws DocumentEntryException {
		if (value == null || "".equals(value)) {
			throw new DocumentEntryException(valuename + "不能为空");
		}
		return value;
	}
	
	@RequestMapping(value = "/getSeesionError")
	public void toError(HttpServletRequest request,HttpServletResponse response) throws IOException{
		System.out.println("toerr");
		HashMap<String,Object> map = new HashMap<String,Object>();
		String errmsg = (String)request.getSession().getAttribute("urlloginerr");
		map.put("errmsg", errmsg);
		HttpHelper.writeHdptJSON(map, response,true);
	}
	
	public void saveMpidInfo(YyUserAccount account){
		try{
		Map<String, Object> JMHD_MEMBER = new HashMap<String, Object>();
		
		// mpi查询结果
		List<Map<String, Object>> list = null;
		
		// mpi 查询参数
		Map<String, Object> mpiArgs = new HashMap<String, Object>();
		
		// 证件类型为身份证
		if("01".equals(String.valueOf(account.getCardtype()))){
			mpiArgs.put(Constants.F_IDCARD, String.valueOf(account.getIdcard()));
			list = mpiService.getMPIDetail(mpiArgs); 
		}else{
			// 证件类型为其他类型
			Map<String, Object> certificatesmap = new HashMap<String, Object>();
			certificatesmap.put("cardNo", String.valueOf(account.getIdcard()));
			certificatesmap.put("cardTypeCode", String.valueOf(account.getCardtype()));
			List<Map<String, Object>> certificateslist = new ArrayList<Map<String, Object>>();
			certificateslist.add(certificatesmap);
			mpiArgs.put("cards", certificateslist);
			list = mpiService.getMPIDetail(mpiArgs); 
		}
		
		if(list!= null && list.size()>0){
			
			Map<String,Object> m = list.get(0);
			 
			JMHD_MEMBER.put("MPIID", m.get("mpiId"));
			JMHD_MEMBER.put("USERNAME", account.getUsername());
			JMHD_MEMBER.put("NAME", account.getUsername());
			JMHD_MEMBER.put("STATUS", "0");
			JMHD_MEMBER.put("HOMEADDRESS", m.get("address"));
			JMHD_MEMBER.put("CONTACT", m.get("contactName"));
			JMHD_MEMBER.put("CONTACTPHONE", m.get("contactPhone"));
			JMHD_MEMBER.put("REGDATE", new Date());
			JMHD_MEMBER.put("CERTIFICATETYPECODE", String.valueOf(account.getCardtype()));
			JMHD_MEMBER.put("CERTIFICATENO", String.valueOf(account.getIdcard()));
			JMHD_MEMBER.put("PHONENUMBER", account.getPhoneno());
			
			serviceDao.save("JMHD_MEMBER", JMHD_MEMBER);
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void setLoginSeesion(YyUserAccount account,HttpServletRequest request){
		try{
			String sessionId = PtLabReportService.getSession();
			request.getSession().setAttribute("sessionId",sessionId);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		//身份证登录
		Map<String, Object>	member = serviceDao.queryForMap("JMHD_MEMBER","PHONENUMBER=? and STATUS ='0'",
				new Object[]{account.getPhoneno()});
		
		if(member != null && !member.isEmpty()){
			List<Map<String, Object>> list = serviceDao.queryForListSQL("select a.id,a.empiid,a.auth_empiid from JMHD_USER_FAMILY a " +
					"inner join JMHD_MEMBER b on a.empiid = b.mpiid " +
					"inner join JMHD_MEMBER c on a.auth_empiid = c.mpiid " +
					"where (a.empiid=? or a.auth_empiid=?) and a.del_Status=? and a.status=?",new Object[]{member.get("MPIID"),member.get("MPIID"),0,2});
			if(list !=null && !list.isEmpty()){
				request.getSession().setAttribute("familys",list);
			}
			//结束
			request.getSession().setAttribute("name",member.get("NAME"));
			request.getSession().setAttribute("mpiId",member.get("MPIID"));
			request.getSession().setAttribute("manadoctorid", member.get("MANADOCTORID"));
			request.getSession().setAttribute("manaunitid", member.get("MANAUNITID"));
			request.getSession().setAttribute("manaunit_text", member.get("MANAUNIT_TEXT"));
			request.getSession().setAttribute("manadoctorname", member.get("MANADOCTOR_TEXT"));
		}

		request.getSession().setAttribute("phoneno",account.getPhoneno());//手机号码
		request.getSession().setAttribute("map",account);
	}
	
}
