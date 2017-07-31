package dc.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.xml.sax.SAXException;

import com.alibaba.fastjson.JSONArray;
import com.bsoft.mpi.Constants;
import com.bsoft.mpi.server.rpc.IMPIProvider;
import com.bsoft.xds.ILabReportRetrieveService;
import com.bsoft.xds.support.dao.IDAO;

import ctd.net.rpc.Client;
import ctd.util.MD5StringUtil;
import ctd.util.context.Context;
import ctd.util.exp.ExpException;
import ctd.util.exp.ExpRunner;
import dc.ServerCode;
import dc.domain.adminuser.JmhdSysAdminuser;
import dc.domain.main.JmhdSysModule;
import dc.domain.main.VjmhdSysAdminrole;
import dc.domain.main.YyUserAccount;
import dc.util.ConfigUtil;
import dc.util.DateUtil;
import dc.util.HttpHelper;
import dc.util.IdCardUtil;
import dc.util.RandomWord;
import dc.util.code.Base64Decode;
import mtcservers.cn.hzcr.monitor.message.SendMessage;
import net.coobird.thumbnailator.Thumbnails;

/**
 * @author Administrator
 *
 */
@Controller
public class MtcController {
	@Autowired
	private ILabReportRetrieveService PtLabReportService;
	
//	@Autowired
//	private IEHRViewService ehrViewService;
	
	@Autowired
	private IMPIProvider mpiService;

	@Autowired
	private SendMessage sendMessage;
	
	@Autowired
	private IDAO serviceDao;
	
	protected final static String CODE = "x-response-code";
	protected final static String MSG = "x-response-msg";
	protected final static String BODY = "body";
	protected final static String NotLogon = "NotLogon";
	
	protected String str = null;

	
	// 验证码图片的宽度。  
    private int width = 80;  
    // 验证码图片的高度。  
    private int height = 20;  
    // 验证码字符个数  
    private int codeCount = 4;  
  
    private int x = 0;  
    // 字体高度  
    private int fontHeight;  
    private int codeY;  
  
    char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',  
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',  
            'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' }; 
    
    private static final Logger LOGGER = LoggerFactory.getLogger(MtcController.class);
    
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
    SimpleDateFormat sdf_full = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    private static String baseDir = "/article_file/";//上传文件存储目录  
    private static Long maxSize = 1024 *1024*1024l;  
  
    // 0:不建目录 1:按天存入目录 2:按月存入目录 3:按扩展名存目录 建议使用按天存  
    private static String dirType = "1";  
	
	/**
	 * 获得MPI信息
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/getMpiInfo")
	public void getMpiInfo(HttpServletRequest request,
			HttpServletResponse response)
			throws IOException {
		try {
			Map<String, Object> mpi = mpiService.getMPI((String) request.getSession().getAttribute("mpiId"));
			//返回正常
			HttpHelper.writeJSON( 200, "success",mpi,response);
//			HttpHelper.writeHtml(str,response);
		} catch (Exception e) {
			//返回异常
			HttpHelper.writeJSON(500, e.getMessage(), null, response);
		}

	}
	
	/**
	 * 修改密码
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/edtpwd")
	public void edtpwd(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "origpwd", required = false) String origpwd,
			@RequestParam(value = "pwd", required = false) String pwd,
			@RequestParam(value = "repwd", required = false) String repwd)
			throws IOException {
		
		Map<String, Object> data = new HashMap<String, Object>();
		String phoneno = (String) request.getSession().getAttribute("phoneno");
		try {
			if(phoneno==null){
				HttpHelper.writeJSON(404, "用户未登陆！", null, response);
				return;
			}
			else if(!pwd.equals(repwd)){
				HttpHelper.writeJSON(401, "重复的密码不正确！", null, response);
				return;
			}
			else{
				data = edtpwd(phoneno, origpwd, repwd);
				HttpHelper.writeJSON((Integer) data.get("code"), (String)data.get("msg"), data.get("body"), response);
			}

		} catch (Exception e) {
			//返回异常
			HttpHelper.writeJSON(500, e.getMessage(), null, response);
		}

	}
	
	@RequestMapping(value = "/passwordUpdate")
	public void passwordUpdate(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "passwordOld", required = true) String passwordOld,
			@RequestParam(value = "passwordNew", required = true) String passwordNew)
			throws IOException {
		
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			JmhdSysAdminuser user =  (JmhdSysAdminuser)request.getSession().getAttribute("data");
			
			if(user.getPassword().equalsIgnoreCase(MD5StringUtil.MD5Encode( passwordOld ))){
				JmhdSysAdminuser adminuser = serviceDao.queryT("JmhdSysAdminuser", "adminId=?", new Object[]{user.getAdminId()});
				if(adminuser != null){
					adminuser.setPassword(MD5StringUtil.MD5Encode( passwordNew ));
					serviceDao.update("JmhdSysAdminuser", adminuser);
				}
				map.put("success", true);
				map.put("message", "密码修改成功，重新登陆后生效。");
			}else{
				map.put("message", "原密码错误！");
			}
		}catch(Exception e){
			map.put("message", "密码修改失败，联系管理员处理。");
		}
		HttpHelper.writeHdptJSON(map, response,true);
	}
	
	
	
//	/**
//	 * 社区开通
//	 * 
//	 * @param request
//	 * @param response
//	 * @param UserName
//	 * @param cardType
//	 * @param IDCard
//	 * @param verify
//	 * @param RealName
//	 * @param PassWord
//	 */
//	@RequestMapping(value = "/cross")
//	public void cross(HttpServletRequest request,
//			HttpServletResponse response,
//			@RequestParam(value = "idcard", required = true) String IDCard,
//			@RequestParam(value = "regdocid", required = true) String regdocid,
//			@RequestParam(value = "manadocid", required = false) String manadocid,
//			@RequestParam(value = "manaunitid", required = false) String manaunitid){
//		
//		Map<String, Object> data = new HashMap<String, Object>();
//		Map<String, Object> member = new HashMap<String, Object>();
//		try {
//			//获得mpiid
//			String mpiid = 	mpiService.getMPIID(IDCard); 
//			
//			if(mpiid== null){
//				HttpHelper.writeJSON( 404, "该身份证中心不存在！无法开通！",null,response);
//				return;
//			}
//			//判断是否开通
//			member = ehrViewService.findOne("JMHD_MEMBER"," MPIID=? or CERTIFICATENO=?", mpiid,IDCard);
//			if(member!=null){
//				HttpHelper.writeJSON( 500, "该用户已经开通！",null,response);
//				return;
//			}
//			else{
//				Map<String, Object> info  = mpiService.getMPI(mpiid);
//				//注册开通
//				if(info!=null){
//					Map<String, Object> JMHD_MEMBER = new HashMap<String, Object>();
//					JMHD_MEMBER.put("ID", "0");
//					JMHD_MEMBER.put("MPIID", mpiid);
//					JMHD_MEMBER.put("PASSWORD", MD5StringUtil.MD5Encode(MD5StringUtil.MD5Encode("123abc")));
//					JMHD_MEMBER.put("NAME",info.get("personName"));
//					JMHD_MEMBER.put("CERTIFICATETYPECODE","1");
//					JMHD_MEMBER.put("CERTIFICATENO",IDCard);
//					JMHD_MEMBER.put("STATUS", "0");
//					JMHD_MEMBER.put("PHONENUMBER",info.get("contactNo"));
//					JMHD_MEMBER.put("INPUTUSER",regdocid);
//					JMHD_MEMBER.put("SOURCE","1");
//					JMHD_MEMBER.put("MANADOCTORID", manadocid);
//					JMHD_MEMBER.put("MANAUNITID", manaunitid);
//
//					ehrViewService.save("JMHD_MEMBER",JMHD_MEMBER);
//					
//					HttpHelper.writeJSON( 200, "成功开通！",null,response);
//				}
//				else{
//					HttpHelper.writeJSON( 500, "用户信息不足开通失败！",null,response);
//				}
//				
//			}
//
//		}
//		catch(MPIServiceException e){
//			HttpHelper.writeJSON( 500, e.getMessage(),null,response);
//		}
//		catch(Exception e){
//			HttpHelper.writeJSON( 500, e.getMessage(),null,response);
//		}
//	}
	
//	/**
//	 * 密码重置
//	 * 
//	 * @param request
//	 * @param response
//	 * @param cardType
//	 * @param IDCard
//	 * @param verify
//	 */
//	@RequestMapping(value = "/admin/RestorePwd")
//	public void RestorePwd(HttpServletRequest request,
//			HttpServletResponse response,
//			@RequestParam(value = "IDCard", required = false) String IDCard){
//		Map<String, Object> data = new HashMap<String, Object>();
//		if(IDCard==null){
//			HttpHelper.writeJSON( 404, "身份证不能为空！",null,response);
//		}
//		if(request.getSession().getAttribute("LOGONAME")!=null || request.getSession().getAttribute("ADMIN_NAME")!=null){
//			//获得mpiid
//			String mpiid;
//			try {
//				mpiid = mpiService.getMPIID(IDCard);
//				if(mpiid== null){
//					HttpHelper.writeJSON( 404, "该身份证中心不存在！无法开通！",null,response);
//					return;
//				}
//				String  random_pwd = RandomWord.randomString(6);
//				String  random_salt = null;
//				
//				data.put("PASSWORD", MD5StringUtil.MD5Encode(MD5StringUtil.MD5Encode(random_pwd)));
//				data.put("LASTMODIFYUSER", request.getSession().getAttribute("LOGONAME") != null?request.getSession().getAttribute("LOGONAME"):request.getSession().getAttribute("ADMIN_NAME"));
//				data.put("LASTMODIFYDATE", new Date());
//				data.put("SALT", random_salt);
//				ehrViewService.update("JMHD_MEMBER","CERTIFICATENO='"+IDCard+"'",data);
//				//返回随机密码
//				Map<String, Object> pwd = new HashMap<String, Object>();
//				pwd.put("pwd", random_pwd);
//				HttpHelper.writeJSON( 200, "更新成功！",pwd,response);
//				
//			} catch (MPIServiceException e) {
//				HttpHelper.writeJSON( 500, e.getMessage(),null,response);
//			} 
//			catch(Exception e){
//				HttpHelper.writeJSON( 500, e.getMessage(),null,response);
//			}
//		}
//	 else{
//			HttpHelper.writeJSON( 404, "医生未登录！",null,response);
//		}
//	}
//	/**
//	 * 注销用户
//	 * 
//	 * @param request
//	 * @param response
//	 * @param cardType
//	 * @param IDCard
//	 * @param verify
//	 */
//	@RequestMapping(value = "/admin/offmember")
//	public void offmember(HttpServletRequest request,
//			HttpServletResponse response,
//			@RequestParam(value = "IDCard", required = false) String IDCard){
//		Map<String, Object> data = new HashMap<String, Object>();
//		if(IDCard==null){
//			HttpHelper.writeJSON( 404, "身份证不能为空！",null,response);
//		}
//		if(request.getSession().getAttribute("LOGONAME")!=null || request.getSession().getAttribute("ADMIN_NAME")!=null){
//			//获得mpiid
//			String mpiid;
//			try {
//				mpiid = mpiService.getMPIID(IDCard);
//				if(mpiid== null){
//					HttpHelper.writeJSON( 404, "该身份证中心不存在！无法开通！",null,response);
//					return;
//				}
//				String  random_pwd = RandomWord.randomString(6);
//				data.put("STATUS", "1");
//				data.put("OFFDOC", request.getSession().getAttribute("LOGONAME") != null?request.getSession().getAttribute("LOGONAME"):request.getSession().getAttribute("ADMIN_NAME"));
//				data.put("OFFDATE", new Date());
//				ehrViewService.update("JMHD_MEMBER","CERTIFICATENO='"+IDCard+"'",data);
//		
//				HttpHelper.writeJSON( 200, "注销成功！",null,response);
//				
//			} catch (MPIServiceException e) {
//				HttpHelper.writeJSON( 500, e.getMessage(),null,response);
//			} 
//			catch(Exception e){
//				HttpHelper.writeJSON( 500, e.getMessage(),null,response);
//			}
//		}
//	 else{
//			HttpHelper.writeJSON( 404, "医生未登录！",null,response);
//		}
//	}
	
	
	//注册
	@RequestMapping(value="/register",method=RequestMethod.POST)
	public void register(YyUserAccount account , HttpServletRequest request, HttpServletResponse response){
		
		try{
			
//			if(!account.getVerify().equals(request.getSession().getAttribute("verify"))){
//				HttpHelper.writeJSON(201, "验证码错误!", null, response);
//				return ;
//			}
			
			
			account.setPassword(MD5StringUtil.MD5Encode(account.getPassword()));
			account.setRegdate(new Date());
			account.setLastmodifytime(new Date());
			account.setLoginid(MD5StringUtil.MD5Encode(account.getUsername()));
			account.setAge((short)DateUtil.getAgeByBirthday(account.getBirthday()));

			serviceDao.save("YyUserAccount", account);
			
			String includempi = ConfigUtil.getValue("register.include.mpi");
			// 不通过社区授权直接插入MPIID
			if("1".equals(includempi)){
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
			}

			
			HttpHelper.writeJSON(200, "success", null, response);
			
		} catch (Exception e) {
			//返回异常
			HttpHelper.writeJSON(500, e.getMessage(), null, response);
		}
		
	}
	
	@RequestMapping(value = "/getAdminLoginInfo")
	public void getAdminLoginInfo(HttpServletRequest request, HttpServletResponse response) {
		try {
			HashMap<String, Object> map = new HashMap<String, Object>();
			JmhdSysAdminuser user = (JmhdSysAdminuser) request.getSession().getAttribute("adminlogininfo");
			map.put("userinfo", user);
			// 角色类型是否包含系统管理员
			if (user.getRoles() != null && !user.getRoles().isEmpty()) {
				for (VjmhdSysAdminrole role : user.getRoles()) {
					// 角色类型包含管理员
					if ("2".equals(role.getRoleType())) {
						map.put("roletype", "2");
						break;
					}
				}
			}
			if("admin".equals(user.getLoginName())){
				map.put("roletype", "2");
			}
			HttpHelper.writeHdptJSON(map, response, true);
		} catch (Exception e) {
			HttpHelper.writeHdptJSON(null, response, false);
		}
	}
	
	/**
	 * 检查用户
	 * 
	 * @param request
	 * @param response
	 * @param UserName
	 * @throws IOException
	 */
	@RequestMapping(value = "/checkuser")
	public void checkUser(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "phoneno", required = false) String phoneno)
			throws IOException {
		
		try {
				YyUserAccount account = serviceDao.queryT("YyUserAccount", " phoneno=? ", new Object[]{phoneno});
				if(account == null){
					HttpHelper.writeJSON(200, "success", null, response);
				}else{
					HttpHelper.writeJSON(201, "false", null, response);
				}
				
		} catch (Exception e) {
			//返回异常
			HttpHelper.writeJSON(500, e.getMessage(), null, response);
		}

	}
	
	/**
	 * 检查手机号
	 * 
	 * @param request
	 * @param response
	 * @param UserName
	 * @throws IOException
	 */
	@RequestMapping(value = "/checkph")
	public void checkph(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "phoneno", required = false) String phoneno)
			throws IOException {
		
		try {
				YyUserAccount account = serviceDao.queryT("YyUserAccount", " phoneno=? ", new Object[]{phoneno});
				String oldphoneno = (String)request.getSession().getAttribute("phoneno");
				//System.out.println(phoneno+"hhhhhh"+oldphoneno);
				if(account == null || oldphoneno.equals(phoneno)){
					HttpHelper.writeJSON(200, "success", null, response);
				}else{
					HttpHelper.writeJSON(201, "false", null, response);
				}
				
		} catch (Exception e) {
			//返回异常
			HttpHelper.writeJSON(500, e.getMessage(), null, response);
		}

	}
	
	/**
	 * 用户登录（手机号码登录）
	 * 
	 * @param request
	 * @param response
	 * @param cardId
	 * @param password
	 * @param verifyCode
	 * @throws IOException
	 */
	@RequestMapping(value = "/logon")
	public void logon(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "loginname", required = false) String phoneno,
			@RequestParam(value = "password", required = false) String password,
			@RequestParam(value = "verifyCode", required = false) String verifyCode)
			throws IOException {
		try {
			//判断登录者身份
			boolean doc_logon =false;
			if(request.getSession().getAttribute("LOGONAME")!=null || request.getSession().getAttribute("ADMIN_NAME")!=null){
				doc_logon =true;
			}
			//市民登录需要验证码
			if(!doc_logon&&!(verifyCode.toUpperCase()).equals(request.getSession().getAttribute("validateCodeRecruit").toString())){
				HttpHelper.writeJSON(404, "验证码错误", null, response);
 				return;
			}
			YyUserAccount account = serviceDao.queryT("YyUserAccount", " phoneno=? and password=?", new Object[]{phoneno,MD5StringUtil.MD5Encode(password)});
			if(account != null){
				try{
					String sessionId = PtLabReportService.getSession();
					request.getSession().setAttribute("sessionId",sessionId);
				}catch (Exception e) {
					e.printStackTrace();
				}
				
				//身份证登录
				Map<String, Object>	member = serviceDao.queryForMap("JMHD_MEMBER","PHONENUMBER=? and STATUS ='0'",
						new Object[]{phoneno});
				
				if(member != null && !member.isEmpty()){
					//吴江项目该功能需要注释掉
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



				
				request.getSession().setAttribute("phoneno",phoneno);//手机号码
				request.getSession().setAttribute("map",account);
				//返回正常
				HttpHelper.writeJSON( 200, "success",account,response);
			}
			else{
				HttpHelper.writeJSON(500, null, null, response);
			}
		
			//		HttpHelper.writeHtml(str,response);
		} catch (Exception e) {
			//返回异常
			e.printStackTrace();
			HttpHelper.writeJSON(500, e.getMessage(), null, response);
		}

	}
	
	 //用户通过预约挂号登录
	 @RequestMapping(value = "/logonforyygh")
	 public void logonforyygh(HttpServletRequest request, HttpServletResponse response,
			 @RequestParam(value = "phoneno") String phoneno,
			 @RequestParam(value = "password") String password){
		 try {
			 phoneno = Base64Decode.getDecodeData(phoneno);
			 password = Base64Decode.getDecodeData(password);
			 YyUserAccount account = serviceDao.queryT("YyUserAccount", " phoneno=? and password=?", new Object[]{phoneno, password});
			 if(account != null){
					try{
						String sessionId = PtLabReportService.getSession();
						request.getSession().setAttribute("sessionId",sessionId);
					}catch (Exception e) {
						e.printStackTrace();
					}
					
					//身份证登录
					Map<String, Object>	member = serviceDao.queryForMap("JMHD_MEMBER","PHONENUMBER=? and STATUS ='0'",
							new Object[]{phoneno});
					
					if(member != null && !member.isEmpty()){
						//吴江项目该功能需要注释掉
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
					
					request.getSession().setAttribute("phoneno",phoneno);//手机号码
					request.getSession().setAttribute("map",account);
					
			        response.sendRedirect("hdpt/index.html");
				}
		} catch (Exception e) {
			// 出现异常
			e.printStackTrace();
		}
		 
	 }
	
	@RequestMapping(value = "/logonforwj")
	public void logonforwj(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "idnumber", required = false) String idnumber
			)
			throws IOException {
		try {
			//判断登录者身份
			boolean doc_logon =false;
			if(request.getSession().getAttribute("LOGONAME")!=null || request.getSession().getAttribute("ADMIN_NAME")!=null){
				doc_logon =true;
			}
			
			Map<String, Object> mpiArgs = new HashMap<String, Object>();
			mpiArgs.put(Constants.F_IDCARD, idnumber);
			List<Map<String, Object>> list = mpiService.getMPIDetail(mpiArgs);
			
			if(list != null){
				
				request.getSession().setAttribute("mpiId",list.get(0).get("mpiId"));
															
				response.sendRedirect("hdpt/index.html");
			}		
		} catch (Exception e) {
			//返回异常
			e.printStackTrace();
			HttpHelper.writeJSON(500, e.getMessage(), null, response);
		}

	}
	
	@RequestMapping(value = "/getSessionValue")
	public void getSessionValue(HttpServletRequest request, HttpServletResponse response) {
		try{
			HashMap<String,Object> map = new HashMap<String,Object>();
			if(request.getSession().getAttribute("map") != null){
				YyUserAccount account = (YyUserAccount) request.getSession().getAttribute("map");
				map.put("phoneno", account.getPhoneno());
				map.put("password", account.getPassword());
				map.put("personName", account.getUsername());
				if("1".equals(account.getSex())){
					map.put("sexCode_text", "男");
				}else{
					map.put("sexCode_text", "女");
				}
				
				map.put("birthday", account.getBirthday());
				if("1".equals(account.getNationality())){
					map.put("nationalityCode_text","中国大陆");
				}else if("2".equals(account.getNationality())){
					map.put("nationalityCode_text", "中国港澳");
				}else if("3".equals(account.getNationality())){
					map.put("nationalityCode_text", "中国台湾");
				}else{
					map.put("nationalityCode_text", "海外同胞");
				}
				
				if("01".equals(account.getCardtype())){//身份证
					map.put("idCard", account.getIdcard());
				}
			}
			
			if( request.getSession().getAttribute("mpiId") != null){
				map.put("mpiId", request.getSession().getAttribute("mpiId"));
				map.put("manadoctorid", request.getSession().getAttribute("manadoctorid"));
				map.put("manaunitid", request.getSession().getAttribute("manaunitid"));
				map.put("manaunit_text", request.getSession().getAttribute("manaunit_text"));
			}
			if( request.getSession().getAttribute("familys") != null){
				map.put("familys", request.getSession().getAttribute("familys"));
			}
			HttpHelper.writeHdptJSON(map, response,true);
		} catch (Exception e) {
			HttpHelper.writeHdptJSON(null, response,false);
		}
	}
	
	
	@RequestMapping(value = "/getDocByRpc")
	public void getDocByRpc(HttpServletRequest request, HttpServletResponse response,
			@RequestParam String domain,
			@RequestParam String beanName,
			@RequestParam String dcid) {
		
		Object result = null;
		try {
			result = Client.rpcInvoke(domain+"."+beanName,
					"getDefaultDocumentByRecordId", new Object[] { dcid});
			if(result != null){
				result = String.valueOf(result);
			}
			HttpHelper.writeJSON(200, "success", result, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			HttpHelper.writeJSON(500, e.getMessage(), null, response);
		}
		
	}
	
	
	/**
	 * 用户退出
	 * 
	 * @param request
	 * @param response
	 * @param UserName
	 * @param password
	 * @throws IOException
	 */
	@RequestMapping(value = "/logout")
	public void logout(HttpServletRequest request,
			HttpServletResponse response)
			throws IOException {
		request.getSession().setAttribute("map",null);
		request.getSession().setAttribute("mpiId",null);
		//返回正常
		response.sendRedirect("logon.html");
	}

	/**
	 * 只做退出操作不跳转页面
	 * @param request
	 * @param response
	 * @throws IOException
     */
	@RequestMapping(value = "/logoutOnly")
	public void logoutOnly(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.getSession().setAttribute("map",null);
		request.getSession().setAttribute("mpiId",null);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		HttpHelper.writeHdptJSON(map, response,true);
	}
	
	/**
	 * 管理员退出
	 * 
	 * @param request
	 * @param response
	 * @param UserName
	 * @param password
	 * @throws IOException
	 */
	@RequestMapping(value = "/adminlogout")
	public void adminlogout(HttpServletRequest request,
			HttpServletResponse response)
			throws IOException {
		request.getSession().setAttribute("adminName",null);
		request.getSession().setAttribute("data",null);
		request.getSession().setAttribute("roles",null);
		//返回正常
		response.sendRedirect("admin/login.html");
	}
	/**
	 * 获得服务器日期
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/now")
	public void now(HttpServletRequest request,
			HttpServletResponse response)
			throws IOException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 E");//设置日期df2
		//System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
		Map<String, Object> now = new HashMap<String, Object>();
		now.put("now", df.format(new Date()));
		HttpHelper.writeJSON( 200, "success",now,response);
	}
	
	/**
	 * 检查联系方式
	 * 
	 * @param request
	 * @param response
	 * @param cardType
	 * @param IDCard
	 * @throws IOException
	 */
	@RequestMapping(value = "/getPhonenum")
	public void getPhonenum(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "cardType", required = false) String cardType,
			@RequestParam(value = "IDCard", required = false) String IDCard)
			throws IOException {
		List<Map<String, Object>>   ContactWays = null;
		List<String>   RetuenContactWays =new  ArrayList<String>() ;
		List<String>   RetuenMaskContactWays =new  ArrayList<String>() ;
		Map<String, Object> data = new HashMap<String, Object>();
		String mpiid =null;
		try {
			data = PtLabReportService.getPhonenum( cardType, IDCard);
			mpiid = (String) data.get("body");
			if(mpiid!=null){
				ContactWays = mpiService.getContactWays(mpiid);
				
				for(int i=0;i<ContactWays.size();i++){
					Map<String, Object> contact = ContactWays.get(i);
//					if("01".equals(contact.get("contactTypeCode"))&&contact.get("contactNo")!=null&& ((String)contact.get("contactNo")).length()==11){
					if(contact.get("contactNo")!=null&& ((String)contact.get("contactNo")).length()==11){
						RetuenContactWays.add((String)contact.get("contactNo"));
						RetuenMaskContactWays.add(((String)contact.get("contactNo")).substring(0,3)+"****"+((String)contact.get("contactNo")).substring(7,11));
					}
				}
				request.getSession().setAttribute("phonenum_ls", RetuenContactWays);
				request.getSession().setAttribute("zcmpiId", mpiid);
				//返回正常
				HttpHelper.writeJSON( 200, "success",RetuenMaskContactWays,response);
			}
			else{
				//返回正常
				HttpHelper.writeJSON((Integer)data.get("code"), (String)data.get("msg"), data.get("body"), response);
			}
		}
		
		catch (Exception e) {
			// TODO Auto-generated catch block
			HttpHelper.writeJSON(500, e.getMessage(), null, response);
		}
		
	}
	
	/**
	 * 重置密码检查联系方式
	 * 
	 * @param request
	 * @param response
	 * @param cardType
	 * @param IDCard
	 * @throws IOException
	 */
	@RequestMapping(value = "/restoregetPhonenum")
	public void restoregetPhonenum(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "cardType", required = false) String cardType,
			@RequestParam(value = "IDCard", required = false) String IDCard)
			throws IOException {
		List<Map<String, Object>>   ContactWays = null;
		List<String>   RetuenContactWays =new  ArrayList<String>() ;
		List<String>   RetuenMaskContactWays =new  ArrayList<String>() ;
		Map<String, Object> data = new HashMap<String, Object>();
		String mpiid =null;
		try {
			data = PtLabReportService.getPhonenum( cardType, IDCard);
			mpiid = (String) data.get("body");
			if(mpiid==null){
				//身份证
				if("1".equals(cardType)){
						mpiid = 	mpiService.getMPIID(IDCard); 
					} 
				//市民卡
				else{
						Map<String, Object>   Cardnum =  new HashMap<String, Object>();
						Map<String, Object>   Cards =  new HashMap<String, Object>();
						List<Object> Cardslist= new ArrayList<Object>();
						List<String> result= new ArrayList<String>();
						Cardnum.put("cardTypeCode", "02");
						Cardnum.put("cardNo", IDCard);
						Cardslist.add(Cardnum);
						Cards.put("cards", Cardslist);
						result = 	mpiService.getMPIID(Cards);
						if(result.size()==1){
							mpiid = result.get(0);
						}
						else{
							mpiid = null;
						}
					}
				ContactWays = mpiService.getContactWays(mpiid);
				
				for(int i=0;i<ContactWays.size();i++){
					Map<String, Object> contact = ContactWays.get(i);
					if("01".equals(contact.get("contactTypeCode"))&&contact.get("contactNo")!=null&& ((String)contact.get("contactNo")).length()==11){
						RetuenContactWays.add((String)contact.get("contactNo"));
						RetuenMaskContactWays.add(((String)contact.get("contactNo")).substring(0,3)+"****"+((String)contact.get("contactNo")).substring(7,11));
					}
				}
				request.getSession().setAttribute("phonenum_ls", RetuenContactWays);
				request.getSession().setAttribute("mpiId", mpiid);
				//返回正常
				HttpHelper.writeJSON( 300, "success",RetuenMaskContactWays,response);
			}
			else{
				//返回正常
				HttpHelper.writeJSON((Integer)data.get("code"), (String)data.get("msg"), data.get("body"), response);
			}
		}
		
		catch (Exception e) {
			// TODO Auto-generated catch block
			HttpHelper.writeJSON(500, e.getMessage(), null, response);
		}
		
	}
	
	/**   登录获取验证码
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/validateCode")
	public void validateCode(HttpServletRequest request,
			HttpServletResponse response)
			throws IOException {
		HttpSession session = request.getSession();  
        
		  x = width / (codeCount + 1);  
	      fontHeight = height - 2;  
	      codeY = height - 4;  
	        
        // 定义图像buffer  
        BufferedImage buffImg = new BufferedImage(width, height,  
                BufferedImage.TYPE_INT_RGB);  
        Graphics2D g = buffImg.createGraphics();  
  
        // 创建一个随机数生成器类  
        Random random = new Random();  
  
        // 将图像填充为白色  
        g.setColor(Color.WHITE);  
        g.fillRect(0, 0, width, height);  
  
        // 创建字体，字体的大小应该根据图片的高度来定。  
        Font font = new Font("Fixedsys", Font.PLAIN, fontHeight);  
        // 设置字体。  
        g.setFont(font);  
  
        // 画边框。  
        g.setColor(Color.BLACK);  
        g.drawRect(0, 0, width - 1, height - 1);

		// 随机产生20条干扰线，使图象中的认证码不易被其它程序探测到。
		g.setColor(Color.BLACK);
		for (int i = 0; i < 6; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}


		// randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
        StringBuffer randomCode = new StringBuffer();  
        int red = 0, green = 0, blue = 0;  
  
        // 随机产生codeCount数字的验证码。  
        for (int i = 0; i < codeCount; i++) {  
            // 得到随机产生的验证码数字。  
            String strRand = String.valueOf(codeSequence[random.nextInt(36)]);  
            // 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。  
            red = random.nextInt(128);  
            green = random.nextInt(128);  
            blue = random.nextInt(128);  
  
            // 用随机产生的颜色将验证码绘制到图像中。  
            g.setColor(new Color(red, green, blue));  
            g.drawString(strRand, (i ) * x+8+2*i, codeY);  
  
            // 将产生的四个随机数组合在一起。  
            randomCode.append(strRand);  
        }  
        // 将四位数字的验证码保存到Session中。  
        session.setAttribute("validateCodeRecruit", randomCode.toString().toUpperCase());  
  
        // 禁止图像缓存。  
        response.setHeader("Pragma", "no-cache");  
        response.setHeader("Cache-Control", "no-cache");  
        response.setDateHeader("Expires", 0);  
  
        response.setContentType("image/jpeg");  
  
        //清空缓存  
        g.dispose();  
          
        // 将图像输出到Servlet输出流中。  
        ServletOutputStream sos = response.getOutputStream();  
        ImageIO.write(buffImg, "jpeg", sos);  
        sos.close();  
	}
	
//	/**
//	 * 用户登录
//	 * @param cardId
//	 * @param password
//	 * @param doc_logon 
//	 * @return
//	 */
//	public Map<String, Object> logon( String cardId,String password, boolean doc_logon){
//
//		List<Map<String, Object>> list = null;
//		List<String> fields = new ArrayList<String>();
//		Map<String, Object> response = new HashMap<String, Object>();
//		String SALT ="";
//		if(!doc_logon){
//			fields.add("SALT");
//			list = ehrViewService.find("JMHD_MEMBER", fields,
//					"  CERTIFICATENO=? and STATUS ='0'", cardId);
//			if(list==null||list.size() !=1){
//				response.put("code", ServerCode.OK);
//				response.put("msg", "success");
//				response.put("body", list);
//				return response;
//			}
//			else{
//				if(list.get(0).get("SALT")!=null){
//					SALT = (String) list.get(0).get("SALT");
//				}
//				password =MD5StringUtil.MD5Encode( MD5StringUtil.MD5Encode(password)+SALT);
//			}
//		}
//		
//		fields = new ArrayList<String>();
//		fields.add("MPIID");
//		fields.add("EMPIID");
//		fields.add("NAME");
//		//身份证登录
//		if(!doc_logon){
//			list = ehrViewService.find("JMHD_MEMBER", fields,
//					" PASSWORD=? and CERTIFICATENO=? and STATUS ='0'", password,cardId);
//		}
//		else{
//			list = ehrViewService.find("JMHD_MEMBER", fields,
//					" CERTIFICATENO=? and STATUS ='0'",cardId);
//		}
//
//		response.put("code", ServerCode.OK);
//		response.put("msg", "success");
//		response.put("body", list);
//		return response;
//	}
	
	/**
	 * 更多责任医生信息
	 * 
	 * @param request
	 * @param response
	 * @param MANADOCTORID
	 * @param MANAUNITID
	 * @throws IOException
	 */
	@RequestMapping(value = "/moreDoCInfo")
	public void moreDoCInfo(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "MANADOCTORID", required = false) String MANADOCTORID,
			@RequestParam(value = "MANAUNITID", required = false) String MANAUNITID)
			throws IOException {
		List<String> fields = new ArrayList<String>();
		fields.add("GENDER");
		fields.add("MOBILEPHONE");
		fields.add("USERNAME");
		//根据登录名称查询
		Map<String, Object> map = serviceDao.queryForMap("JMHD_SQ_SYSUSER"," LOGONNAME=? ", new Object[]{MANADOCTORID});
		if(map != null){
			HttpHelper.writeJSON( 200, "success",map,response);
			//保存用户管辖机构
			request.getSession().setAttribute("manaunitid", MANAUNITID);
			request.getSession().setAttribute("manadoctorid", MANADOCTORID);
			request.getSession().setAttribute("manadoctorname", map.get("USERNAME"));
		}
		else{
			HttpHelper.writeJSON( 404, "success",null,response);
		}
	}
	
//	/**
//	 * 获得管辖机构代码
//	 * 
//	 * @param request
//	 * @param response
//	 * @throws IOException
//	 */
//	@RequestMapping(value = "/getManunit")
//	public void getManunit(HttpServletRequest request,
//			HttpServletResponse response)
//			throws IOException {
//		List<Map<String, Object>> list = null;
//		Map<String, Object> result = new HashMap<String, Object>();
//		//获得所有的机构
//		list = ehrViewService.find("EHR_MANAUNIT"," 1=? ", 1);
//		if(list.size()!=1){
//			HttpHelper.writeJSON( 200, "success",list,response);
//		}
//		else{
//			HttpHelper.writeJSON( 404, "success",null,response);
//		}
//	}
	
	/**
	 * 个人信息修改
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/check_Userinfo")
	public void check_Userinfo(HttpServletRequest request,
			HttpServletResponse response )
			throws IOException {
		YyUserAccount account = serviceDao.queryT("YyUserAccount", " phoneno=? ", new Object[]{request.getSession().getAttribute("phoneno")});
		HttpHelper.writeJSON( 200, "success",account,response);
	}
	
//	/**
//	 *账户信息查询
//	 * 
//	 * @param request
//	 * @param response
//	 * @throws IOException
//	 */
//	@RequestMapping(value = "/admin/get_account")
//	public void get_account(HttpServletRequest request,
//			HttpServletResponse response ,
//			@RequestParam(value = "idcard", required = true) String IDCard,
//			@RequestParam(value = "url", required = true) String url)
//			throws IOException {
//		if(request.getSession().getAttribute("LOGONAME")!=null || request.getSession().getAttribute("ADMIN_NAME")!=null){
//			try {
//				//获得mpiid
//				String mpiid = 	mpiService.getMPIID(IDCard); 
//				
//				if(mpiid== null){
//					HttpHelper.writeJSON( 404, "该身份证中心不存在！无法开通！",null,response);
//					return;
//				}
//				//远程获得数据
//				url =  url+request.getSession().getId()+"-"+mpiid+".ehr";
//				String data = proxy.readURL(url);
//				//组装数据
//				JSONObject jsonObj = JSONObject.parseObject(data);
//				Map<String, Object> result = new HashMap<String, Object>();
//				Map<String, Object> member = new HashMap<String, Object>();
//				//保存ehr信息
//				result.put("ehr", jsonObj);
//				member = ehrViewService.findOne("JMHD_MEMBER"," MPIID=? ", mpiid);
//				//保存账户信息
//				if(member!=null){
//					result.put("member", member);
//				}
//				else{
//					result.put("member", new HashMap<String, Object>());
//				}
//				HttpHelper.writeJSON( 200, "获得详细信息！",result,response);
//				//System.out.println(jsonObj);
//			}
//			catch(MPIServiceException e){
//				HttpHelper.writeJSON( 500, e.getMessage(),null,response);
//			}
//		}
//		else{
//			HttpHelper.writeJSON( 404, "医生未登录！",null,response);
//		}
//	}
	
	/**
	 * 更新个人信息
	 * 
	 * @param request
	 * @param response
	 * @param dataExist
	 * @param body
	 * @throws IOException
	 */
	@RequestMapping(value = "/updat_Userinfo")
	public void updat_Userinfo(HttpServletRequest request,
			HttpServletResponse response ,YyUserAccount account)
			throws IOException {
		try {
			String phoneno = (String)request.getSession().getAttribute("phoneno");
			YyUserAccount user = serviceDao.queryT("YyUserAccount", " phoneno=? ", new Object[]{phoneno});
			if(user != null){
				user.setUsername(account.getUsername());
				if(user.getCardtype()== null){
					user.setCardtype(account.getCardtype());
					user.setIdcard(account.getIdcard());
				}
				user.setSex(account.getSex());
				user.setBirthday(account.getBirthday());
				user.setPhoneno(account.getPhoneno());
				user.setEmail(account.getEmail());
				user.setAddress(account.getAddress());
				user.setNationality(account.getNationality());
				user.setLastmodifytime(new Date());
				
				serviceDao.update("YyUserAccount",user);
				request.getSession().setAttribute("phoneno", user.getPhoneno());
				request.getSession().setAttribute("map",user);
				//返回正常
				HttpHelper.writeJSON( 200, "修改内容成功！",null,response);
			}else{
				HttpHelper.writeJSON(201, "修改失败", null, response);
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
			HttpHelper.writeJSON( 500, e.getMessage(),null,response);
		}
	}
	
//	/**
//	 * 自助建档
//	 * 
//	 * @param request
//	 * @param response
//	 * @param body
//	 * @throws IOException
//	 */
//	@RequestMapping(value = "/create")
//	public void create(HttpServletRequest request,
//			HttpServletResponse response,@RequestBody  String  body 
//			)
//			throws IOException {
//		List<Map<String, Object>> list = null;
//		Map<String, Object> Params = new HashMap<String, Object>();
//		Params = Url_Map.getUrlParams(body);
//		if (Params.size()==0){
//			HttpHelper.writeJSON( 500, "提交内容不全！",null,response);
//			return;
//		}
//		//身份证检查
//		list = ehrViewService.find("JMHD_APPLY_EHR",
//				" IDCARD=? ", 1,1,Params.get("IDCARD"));
//		if(list.size()>0){
//			HttpHelper.writeJSON( 501, " 不能重复自助建档！",null,response);
//			return;
//		}
//		//是否注册
//		list = ehrViewService.find("JMHD_MEMBER",
//				"CERTIFICATETYPECODE =1 and STATUS=1 and  CERTIFICATENO=? ", 1,1,Params.get("IDCARD"));
//		if(list.size()>0){
//			HttpHelper.writeJSON( 502, "该身份证已经注册！",null,response);
//			return;
//		}
//		try {
//			//获得mpiid
//			String mpiid = 	mpiService.getMPIID((String)Params.get("IDCARD")); 
//			Params.put("MPIID", mpiid);
//			Params.put("ID", "0");
//			Params.put("CREAREDATE", new Date());
//			
//			Date BIRTHDAY = sdf.parse((String)Params.get("BIRTHDAY"));
//			Params.put("BIRTHDAY", BIRTHDAY);
//			
//			if(Params.get("STARTWORKDATE")!=null){
//				Date STARTWORKDATE = sdf.parse((String)Params.get("STARTWORKDATE"));
//				Params.put("STARTWORKDATE", STARTWORKDATE);
//			}
//			
//			Params.put("STATUS", "1");
//			
//			ehrViewService.save("JMHD_APPLY_EHR",Params);
//			//返回正常
//			HttpHelper.writeJSON( 200, "自助建档成功！",null,response);
//		}
//		catch(Exception e){
//			HttpHelper.writeJSON( 500, e.getMessage(),null,response);
//		}
//	}
	
	/**
	 *  根据cnd的条件查询相应的数据
	 *
	 * @param request
	 * @param response
	 * @param uuid
	 * @param entryName
	 * @param cnd
	 */
	@RequestMapping(value = "/entryName/{entryName}")
	public void findContent(HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable("entryName") String entryName,
			@RequestParam(value = "cnd", required = false) String cnd,
			@RequestParam(value = "pageNo", required = false) String pageNo,
			@RequestParam(value = "pageSize", required = false) String pageSize,
			@RequestParam(value = "orderField", required = false) String orderField,
			@RequestParam(value = "isDesc", required = false) String isDesc) {

//		//未登录处理
//		if(request.getSession().getAttribute("LOGONAME")==null
//				&&request.getSession().getAttribute("ADMIN_NAME")==null
//				&&request.getSession().getAttribute("mpiId")==null){
//			HttpHelper.writeJSON( 500, "请登录",null,response);
//		}
		String where;
		Integer no = StringUtils.isEmpty(pageNo) ? 1 : Integer.valueOf(pageNo);
		Integer size = StringUtils.isEmpty(pageSize) ? 9999999 : Integer.valueOf(pageSize);
		boolean Desc = "1".equals(isDesc) ? true : false;
		Integer allcount = 0;

		Map<String, Object> result =new HashMap<String, Object>();
		try {
			if ("[]".equals(cnd)||"[\"and\"]".equals(cnd)){
				where = "1=1";
			}
			else{
				where = ExpRunner.toString(cnd, new Context());
			}
			if(no==1){
				allcount = serviceDao.queryForCount(entryName, where, new HashMap<String, Object>());
			}
		} catch (ExpException e) {
			throw new HibernateException("Make where crouse failed.", e);
		}
		Object[] obj = null;
		List<Map<String, Object>> list = serviceDao.queryForPage(entryName,where, no, size,obj);
		result.put("list", list);
		result.put("cnt", allcount);
		result.put("pageNo", no);
		result.put("pageSize", size);
		HttpHelper.writeJSON( 200, "查询成功！",result,response);

	}
	
	/**
	 * 获得远程资源
	 * 
	 * @param res_url
	 * @return
	 */
	public Object  getRemoteResource( String res_url){
		URL url;
		try {
			url = new URL(res_url);
			  //获得此 URL 的内容。 
		    Object obj = url.getContent(); 
		    return obj; 
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			return e; 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return e; 
		} 
	    
	}

	/**
	 * 市民修改密码
	 * @param mpiid
	 * @param origpwd
	 * @param pwd
	 * @return
	 */
	public Map<String, Object> edtpwd(String phoneno,String origpwd,String pwd) {
		Map<String, Object> response = new HashMap<String, Object>();
		try{
			//核对密码
			YyUserAccount account = serviceDao.queryT("YyUserAccount", "phoneno=? and password=?",new Object[]{ phoneno,MD5StringUtil.MD5Encode(origpwd)});
			if(account != null){
				account.setPassword(MD5StringUtil.MD5Encode(pwd));
				account.setLastmodifytime(new Date());
				serviceDao.update("YyUserAccount", account);
	//			HttpHelper.writeJSON(200, "修改完成！！", null, response);
				response.put("code", ServerCode.OK);
				response.put("msg", "修改完成！！");
				response.put("body", null);
				return response;
		}
		else{
//			HttpHelper.writeJSON(500, "原始密码不正确！！", null, response);
			response.put("code", 500);
			response.put("msg", "原始密码不正确！！");
			response.put("body", null);
			return response;
		}
		}
		catch(Exception e){
//			HttpHelper.writeJSON(500, "原始密码不正确！！", null, response);
			response.put("code", 500);
			response.put("msg", e.getMessage());
			response.put("body", null);
			return response;
		}
	}
	
	
	
	@RequestMapping(value="/sendmsg",method=RequestMethod.POST)
	public void sendmsg(HttpServletRequest request, HttpServletResponse response,@RequestParam(value="phoneno")String phoneno){
		String msg="";
		try {
			String verify = RandomWord.genRandomStr(6);
			msg = sendMessage.sendSmsCode(phoneno,verify, 1);
			request.getSession().setAttribute("verify", verify);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpHelper.writeJSON( 200, msg,null,response);
	}
//	/**
//	 * 社区医生修改密码
//	 * @param mpiid
//	 * @param origpwd
//	 * @param pwd
//	 * @return
//	 */
//	public Map<String, Object> doc_edtpwd(String mpiid,String origpwd,String pwd) {
//		List<Object> list = null;
//		Map<String, Object> data = new HashMap<String, Object>();
//		Map<String, Object> response = new HashMap<String, Object>();
//		//System.out.print(MD5StringUtil.MD5Encode(origpwd));
//		list = ehrViewService.find("JMHD_MEMBER", "ID",
//				"MPIID=? and PASSWORD=?   ",mpiid,MD5StringUtil.MD5Encode(origpwd));
//		if(list.size()==1){
//			data.put("PASSWORD", MD5StringUtil.MD5Encode(pwd));
//			ehrViewService.update("JMHD_MEMBER","ID="+list.get(0),data);
////			HttpHelper.writeJSON(200, "修改完成！！", null, response);
//			response.put("code", ServerCode.OK);
//			response.put("msg", "修改完成！！");
//			response.put("body", null);
//			return response;
//		}
//		else{
////			HttpHelper.writeJSON(500, "原始密码不正确！！", null, response);
//			response.put("code", 500);
//			response.put("msg", "原始密码不正确！！");
//			response.put("body", null);
//			return response;
//		}
//	}
	/**
	 * 管理员登录
	 * 
	 * @param request
	 * @param response
	 * @param loginName
	 * @param password
	 * @throws IOException
	 */
	/**
	 * @param request
	 * @param response
	 * @param loginName
	 * @param password
	 * @throws IOException
	 */
	@RequestMapping(value = "/adminlogon")
	public void adminlogon(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "loginName", required = false) String loginName,
			@RequestParam(value = "password", required = false) String password)
			throws IOException {
		JmhdSysAdminuser data = null;
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			
			password = MD5StringUtil.MD5Encode(password);
			//身份证登录
			data = serviceDao.queryT("JmhdSysAdminuser", "loginName=? and password=?", new Object[]{loginName,password});
			
			if(data != null){
				
				List<Integer> roleArray = new ArrayList<Integer>();
				if (!"admin".equals(data.getLoginName())) {
					
					List<VjmhdSysAdminrole> roles = serviceDao.queryList("VjmhdSysAdminrole", "adminId=? and id is not null", new Object[]{data.getAdminId()});

					List<Integer> tableList = new ArrayList<Integer>();

					for (VjmhdSysAdminrole roleItem : roles) {

						roleArray.add(roleItem.getId());

					}
					HashSet<Integer> hs = new HashSet<Integer>(tableList); // set会去掉重复值
					Iterator<Integer> i = hs.iterator();
					while (i.hasNext()) {
						Integer temp = i.next();
						roleArray.add(temp);
					}
				} else {
					List<JmhdSysModule> jmhdSysModules = serviceDao.queryList("JmhdSysModule", "isleaf=0",  null);
					for (JmhdSysModule module : jmhdSysModules) {
						roleArray.add(module.getId());
					}

				}
				request.getSession().setAttribute("adminName",data.getAdminName());
				request.getSession().setAttribute("data",data);
				request.getSession().setAttribute("adminlogininfo",data);
				request.getSession().setAttribute("roles",roleArray);
				map.put("success", true);
			}
			else{
				map.put("success", false);
			}
			HttpHelper.writeHdptJSON(map, response,true);
			//		HttpHelper.writeHtml(str,response);
		} catch (Exception e) {
			//返回异常
			HttpHelper.writeHdptJSON(map, response,true);
		}

	}
	
	@RequestMapping(value = "/fetchModules")
	public void fetchModules(HttpServletRequest request,HttpServletResponse response)
			throws IOException {
		try{
			List<Integer> params = (List<Integer>)request.getSession().getAttribute("roles");
			List<JmhdSysModule> jmhdSysModules = serviceDao.queryListIn("from JmhdSysModule where id in (:id) ", "id", params);
			String treeJsonData = "[";
			for (JmhdSysModule sysModule : jmhdSysModules) {
				if (sysModule.getModuleLev() == 1) {
					treeJsonData += "{\"id\":"
							+ sysModule.getId()
							+ ",\"text\":\""
							+ sysModule.getModuleName()
							+ "\",\"attributes\":{\"actionname\":\""
							+ sysModule.getActionName()
							+ "\",\"actionmethod\":\""
							+ sysModule.getActionMethod()
							+ "\",\"modulecode\":\""
							+ sysModule.getModuleCode()
							+ "\"},\"children\":["
							+ getTreeSub(jmhdSysModules, sysModule.getModuleCode(),
									sysModule.getModuleLev() + 1)
							+ "],\"checked\":true},";
				}
			}

			if (!treeJsonData.equals("[")) {
				treeJsonData = treeJsonData.substring(0, treeJsonData.length() - 1);
			}
			treeJsonData += "]";
			JSONArray k = JSONArray.parseArray(treeJsonData);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("treeJsonData", k);
			map.put("adminName", request.getSession().getAttribute("adminName"));
			HttpHelper.writeHdptJSON(map, response,true);
		} catch (Exception e) {
			//返回异常
			e.printStackTrace();
			HttpHelper.writeJSON(500, e.getMessage(), null, response);
		}
	}
	
	
	@RequestMapping(value = "/fetchTasks")
	public void fetchTasks(HttpServletRequest request,HttpServletResponse response)
			throws IOException {
		Map<String,Object> map = new HashMap<String,Object>();
		JmhdSysAdminuser user =  (JmhdSysAdminuser)request.getSession().getAttribute("data");
		List<Map<String, Object>> adminWorks = serviceDao.queryForListSQL("select DISTINCT a.wtype,a.numb,b.wtypename,b.wurl from (select wtype,count(wtype) as numb from JMHD_ADMIN_WORK where status='0' and doctor_id= ? group by wtype) a " +
				"left join JMHD_ADMIN_WORK b  on b.wtype=a.wtype ", new Object[]{user.getLoginName()});
		if(adminWorks!=null&&adminWorks.size()>0){
			map.put("adminWorks",adminWorks);
		}
		HttpHelper.writeHdptJSON(map, response,true);
	}
	
	
	/**
	 * 旧地址跳转
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/user/welcome.do")
	public void welcome(HttpServletRequest request,
			HttpServletResponse response)
			throws IOException {
		response.sendRedirect("../");
	}
	
//	/**
//	 * 社区医生登录
//	 * 
//	 * @param request
//	 * @param response
//	 * @param UserName
//	 * @param password
//	 * @throws IOException
//	 */
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "/admin/auto-login")
//	public void doclogon(HttpServletRequest request,
//			HttpServletResponse response,
//			@RequestParam(value = "loginName", required = true) String loginName,
//			@RequestParam(value = "password", required = true) String password)
//			throws IOException {
//		Map<String, Object> data = new HashMap<String, Object>();
//		List<Map<String, Object>> list = null;
//		try {
//			
//			List<String> fields = new ArrayList<String>();
//			fields.add("USERNAME");
//			fields.add("PASSWORD");
//			fields.add("MANAUNITID");
//			fields.add("LOGONNAME");
//			//登录
//			list = ehrViewService.find("JMHD_SQ_SYSUSER",fields,"LOGONNAME=? and STATUS=0 ", loginName);
//			
//			if(list.size()==1){
//				data = list.get(0);
//				if(data.get("PASSWORD")!=null &&  password.equals((String) data.get("PASSWORD"))){
////				if(data.get("PASSWORD")!=null &&  password.equals(MD5StringUtil.MD5Encode((String) data.get("PASSWORD")))){
//					
//					request.getSession().setAttribute("LOGO_DOC_NAME",data.get("USERNAME"));
//					request.getSession().setAttribute("MANAUNITID",data.get("MANAUNITID"));
//					request.getSession().setAttribute("LOGONAME",loginName);
//					//将session共享到远程服务
//					String sessionId = PtLabReportService.getSession();
//					request.getSession().setAttribute("sessionId",sessionId);
//					response.sendRedirect("home-mini.html?DOCTOR_ID="+loginName+"&&DOC_NAME="+java.net.URLEncoder.encode((String) data.get("USERNAME"), "UTF-8"));
//					//返回正常
//					HttpHelper.writeJSON( 200, "success",null,response);
//				}
//				else{
//					//异常正常
//					response.sendRedirect("user_err.html");
//					HttpHelper.writeJSON(404, "新建用户或者修改密码需要隔天才能进入!", null, response);
//				}
//				
//			}
//			else{
//				//异常正常
//				response.sendRedirect("user_err.html");
//				HttpHelper.writeJSON(404, "新建用户或者修改密码需要隔天才能进入!", null, response);
//			}
//		
//			//		HttpHelper.writeHtml(str,response);
//		} catch (Exception e) {
//			//返回异常
//			HttpHelper.writeJSON(500, e.getMessage(), null, response);
//		}
//
//	}
	
	/**
	 * 得到树的子节点json字符串
	 * @param jkwSysModules
	 * @param moduleCode
	 * @param step
	 * @return
	 */
	private String getTreeSub(List<JmhdSysModule> jmhdSysModules,
			String moduleCode, long step) {
		String subJsonData = "";
		for (JmhdSysModule sysModule : jmhdSysModules) {
			// System.out.println(sysModule.getModuleCode().indexOf(moduleCode));
			if (sysModule.getModuleLev() == step
					&& sysModule.getModuleCode().indexOf(moduleCode) == 0) {
				subJsonData += "{\"id\":"
						+ sysModule.getId()
						+ ",\"text\":\""
						+ sysModule.getModuleName()
						+ "\",\"attributes\":{\"actionname\":\""
						+ sysModule.getActionName()
						+ "\",\"actionmethod\":\""
						+ sysModule.getActionMethod()
						+ "\",\"modulecode\":\""
						+ sysModule.getModuleCode()
						+ "\"},\"children\":["
						+ getTreeSub(jmhdSysModules, sysModule.getModuleCode(),
								sysModule.getModuleLev() + 1) + "]},";
			}
		}
		if (!subJsonData.equals("")) {
			subJsonData = subJsonData.substring(0, subJsonData.length() - 1);
		}
		return subJsonData;
	}
	
	
	@RequestMapping(value = "/upload")
	public void upload(HttpServletRequest request,HttpServletResponse response)
			throws IOException {
		response.setContentType("text/html; charset=UTF-8");  
        response.setHeader("Cache-Control", "no-cache");  
  
        String err = "";  
        String newFileName = "";  
        String realBaseDir = request.getSession().getServletContext().getRealPath(baseDir);  
        File baseFile = new File(realBaseDir);  
        if (!baseFile.exists()) {  
            baseFile.mkdir();  
        }
        
        if ("application/octet-stream".equals(request.getContentType())) { //HTML 5 上传

            try {
                String dispoString = request.getHeader("Content-Disposition");
                int iFindStart = dispoString.indexOf("name=\"")+6;
                int iFindEnd = dispoString.indexOf("\"", iFindStart);
                iFindStart = dispoString.indexOf("filename=\"")+10;
                iFindEnd = dispoString.indexOf("\"", iFindStart);
                String sFileName = dispoString.substring(iFindStart, iFindEnd);
                int i = request.getContentLength();
                byte buffer[] = new byte[i];
                int j = 0;
                while(j < i) { //获取表单的上传文件
                    int k = request.getInputStream().read(buffer, j, i-j);
                    j += k;
                }
                if (buffer.length == 0) { //文件是否为空
                    printInfo(response, "上传文件不能为空", "");
                    return;
                }
                if (maxSize > 0 && buffer.length > maxSize) { //检查文件大小
                    printInfo(response, "上传文件的大小超出限制", "");
                    return;
                }
                String filepathString = getSaveFilePath(sFileName, response,request);
				String filepathWx = filepathString.substring(0,filepathString.lastIndexOf("."))+"_wx"+filepathString.substring(filepathString.lastIndexOf("."));
                OutputStream out=new BufferedOutputStream(new FileOutputStream(request.getSession().getServletContext().getRealPath("") + filepathString,true));
                out.write(buffer);
                out.close();
                
                Thumbnails.of(request.getSession().getServletContext()  
                        .getRealPath("")+filepathString)
                .size(180, 120).keepAspectRatio(false).outputQuality(0.9).toFile(request.getSession().getServletContext()
                        .getRealPath("")+filepathWx);

                newFileName = request.getContextPath() + File.separator + filepathString;
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                newFileName = "";
                err = "错误: " + ex.getMessage();
            }

        } else {
        	DiskFileUpload upload = new DiskFileUpload();  
            try {  
                List<FileItem> items = upload.parseRequest(request);  
                Map<String, Serializable> fields = new HashMap<String, Serializable>();  
                Iterator<FileItem> iter = items.iterator();  
                  
                while (iter.hasNext()) {  
                    FileItem item = (FileItem) iter.next();  
                    if (item.isFormField())  
                        fields.put(item.getFieldName(), item.getString());  
                    else  
                        fields.put(item.getFieldName(), item);  
                }  
                  
                /*获取表单的上传文件*/  
                FileItem uploadFile = (FileItem)fields.get("filedata");  
                  
                /*获取文件上传路径名称*/  
                String fileNameLong = uploadFile.getName();  
                //System.out.println("fileNameLong:" + fileNameLong);  
                  
                /*获取文件扩展名*/  
                String extensionName = fileNameLong.substring(fileNameLong.lastIndexOf(".") + 1);  
                //System.out.println("extensionName:" + extensionName);  
                  
//                /*检查文件类型*/  
//                if (("," + fileExt.toLowerCase() + ",").indexOf("," + extensionName.toLowerCase() + ",") < 0){  
//                    printInfo(response, "不允许上传此类型的文件", "");  
//                    return;  
//                }  
                /*文件是否为空*/  
                if (uploadFile.getSize() == 0){  
                    printInfo(response, "上传文件不能为空", "");  
                    return;  
                }  
                /*检查文件大小*/  
                if (maxSize > 0 && uploadFile.getSize() > maxSize){  
                    printInfo(response, "上传文件的大小超出限制", "");  
                    return;  
                }  
                  
                //0:不建目录, 1:按天存入目录, 2:按月存入目录, 3:按扩展名存目录.建议使用按天存.  
                String fileFolder = "";  
                if (dirType.equalsIgnoreCase("1"))  
                    fileFolder = new SimpleDateFormat("yyyyMMdd").format(new Date());;  
                if (dirType.equalsIgnoreCase("2"))  
                    fileFolder = new SimpleDateFormat("yyyyMM").format(new Date());  
                if (dirType.equalsIgnoreCase("3"))  
                    fileFolder = extensionName.toLowerCase();  
                  
                /*文件存储的相对路径*/  
                String saveDirPath = baseDir + fileFolder + "/";  
                //System.out.println("saveDirPath:" + saveDirPath);  
                  
                /*文件存储在容器中的绝对路径*/  
                String saveFilePath = request.getSession().getServletContext().getRealPath("") + saveDirPath;  
                //System.out.println("saveFilePath:" + saveFilePath);  
                  
                /*构建文件目录以及目录文件*/  
                File fileDir = new File(saveFilePath);  
                if (!fileDir.exists()) {fileDir.mkdirs();}  
                  
                /*重命名文件*/  
                String filename = RandomWord.randomString(10);  
                File savefile = new File(saveFilePath + filename + "." + extensionName);  
                  
                /*存储上传文件*/  
                //System.out.println(upload == null);  
                uploadFile.write(savefile);  
                  
                //这个地方根据项目的不一样，需要做一些特别的定制。  
//                newFileName = "/xheditor" + saveDirPath + filename + "." + extensionName;         
                newFileName = request.getContextPath() + File.separator +  saveDirPath + filename + "." + extensionName;
                //System.out.println("newFileName:" + newFileName);  
            } catch (Exception ex) {  
                System.out.println(ex.getMessage());  
                newFileName = "";  
                err = "错误: " + ex.getMessage();  
            }  
        }
        printInfo(response, err, newFileName);
	}
	
	public String getSaveFilePath(String sFileName, HttpServletResponse response,HttpServletRequest request)  
            throws IOException {  
  
        String extensionName = sFileName  
                .substring(sFileName.lastIndexOf(".") + 1); // 获取文件扩展名  
//        if (("," + fileExt.toLowerCase() + ",").indexOf(","  
//                + extensionName.toLowerCase() + ",") < 0) { // 检查文件类型  
//            printInfo(response, "不允许上传此类型的文件", "");  
//            return "不允许上传此类型的文件";  
//        }  
  
        String fileFolder = "";   
        // 0:不建目录, 1:按天存入目录, 2:按月存入目录,3:按扩展名存目录.建议使用按天存  
  
        if (dirType.equalsIgnoreCase("1"))  
            fileFolder = new SimpleDateFormat("yyyyMMdd").format(new Date());  
  
        if (dirType.equalsIgnoreCase("2"))  
            fileFolder = new SimpleDateFormat("yyyyMM").format(new Date());  
  
        if (dirType.equalsIgnoreCase("3"))  
            fileFolder = extensionName.toLowerCase();  
  
        String saveDirPath = baseDir + fileFolder + "/"; // 文件存储的相对路径  
        String saveFilePath = request.getSession().getServletContext()  
                .getRealPath("")+"/"+ saveDirPath; // 文件存储在容器中的绝对路径  
  
        File fileDir = new File(saveFilePath); // 构建文件目录以及目录文件  
        if (!fileDir.exists()) {  
            fileDir.mkdirs();  
        }  
        String filename = RandomWord.randomString(10); // 重命名文件  
        return saveDirPath + filename + "." + extensionName;  
    }
	
	
	 public void printInfo(HttpServletResponse response, String err, String newFileName) throws IOException {  
        PrintWriter out = response.getWriter();  
        //String filename = newFileName.substring(newFileName.lastIndexOf("/") + 1);  
        out.println("{\"err\":\"" + err + "\",\"msg\":\"" + newFileName + "\"}");  
        out.flush();  
        out.close();  
    } 
	
}
