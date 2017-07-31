package dc.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.db2.jcc.b.re;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bsoft.xds.support.dao.IDAO;

import dc.domain.adminuser.JmhdSysAdminuser;
import dc.domain.diary.JmhdUserDiary;
import dc.domain.document.JmhdCmsDocument;
import dc.domain.document.JmhdCmsSubject;
import dc.domain.document.JmhdJkzx;
import dc.domain.document.JmhdWszw;
import dc.domain.main.JmhdInfo;
import dc.util.HttpHelper;
import dc.util.PageBuilder;
import dc.util.PageCount;
import dc.util.PageModel;
import dc.util.ServerUrlUtil;
import dc.util.Util;

@Controller
public class JmhdDocumentController{
	
	
	@Autowired
	private IDAO serviceDao;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JmhdDocumentController.class);
	
	/**
	 * 文章详细信息
	 * @param request
	 * @param response
	 * @param id
	 */
	@RequestMapping(value = "/jmhddocument/ducumentinfo")
	public void ducumentinfo(HttpServletRequest request,
			HttpServletResponse response,@RequestParam(value="docid") Long id){
		
			HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			JmhdCmsDocument info = serviceDao.queryT("JmhdCmsDocument", "id = ?", new Object[]{id});
			map.put("success", Boolean.valueOf(true));
			map.put("info", info);
			HttpHelper.writeHdptJSON(map, response,true);
		} catch (Exception e) {
			map.put("success", Boolean.valueOf(false));
			HttpHelper.writeHdptJSON(null, response,false);
		}

	}
	
	/**
	 * 文章列表
	 * @param request
	 * @param response
	 * @param info
	 */
	@RequestMapping(value = "/jmhddocument/pagelist")
	public void pagelist(HttpServletRequest request,
			HttpServletResponse response,
			@ModelAttribute("info") JmhdInfo info){
		
		 HashMap<String,Object> map = new HashMap<String,Object>();
		try {

			JmhdSysAdminuser  adminuser = (JmhdSysAdminuser) request.getSession().getAttribute("data");
			String manaunitid = "";
			String datafield = "";
			if(adminuser != null){
				manaunitid = adminuser.getOrgId();
				datafield = adminuser.getDatafield();
			}

			String title = info.getTitle();
			String begindate = info.getBeginDate();
			String enddate = info.getEndDate();
			String cardtype = info.getCardtype();
			String status = info.getStatus();
			Long orderNum = info.getOrderNum();
			String extendtitlefir = info.getExtendtitlefir();
			int page = info.getPage();
			
			StringBuffer sql = new StringBuffer("1=1 ");
			
			if(!Util.empty(title)){
				sql = sql.append("and title like '%"+ title +"%' ");
			}
			
			if(!Util.empty(begindate)){
				sql = sql.append("and filldate >= to_date('"+begindate+"','yyyy-MM-dd') ");
			}

			if(!Util.empty(enddate)){
				sql = sql.append("and filldate <= to_date('"+enddate+" 23:59:59','yyyy-MM-dd HH24:mi:ss') ");
			}
			
			if(!Util.empty(cardtype)){
				JmhdCmsSubject jcs = (JmhdCmsSubject)serviceDao.queryT("JmhdCmsSubject", "description = ? ", new Object[]{cardtype});
				if(jcs!=null){
					sql = sql.append("and category = '"+jcs.getId()+"' ");
				}else{
					sql = sql.append("and category = '"+cardtype+"' ");
				}
			}
			
			if(!Util.empty(orderNum)){
				sql = sql.append("and orderNum = '"+orderNum+"' ");
			}
			
			if (status != null && !"".equals(status)) {
				// 查询未审核数据
				if("2".equals(status)){
					sql = sql.append(" and status is null ");
				}else{
					sql = sql.append("and status = '" + status + "' ");
				}
			}
			
			if(!Util.empty(extendtitlefir)){
				sql = sql.append("and extendtitlefir = '"+extendtitlefir+"' ");
			}
			
			if(!Util.empty(manaunitid) && !"3".equals(datafield)){ //所属机构不空，权限不是卫生局

				//manaunitid = manaunitid.substring(0, 9);
				//sql = sql.append("and orgId in ('"+manaunitid +"','"+ manaunitid.subSequence(0, 6) +"','"+ manaunitid.subSequence(0, 4)+"')");
				sql = sql.append("and org_Id = '"+manaunitid+"'");
			}
			
			sql.append("order by filldate desc");
			
			List<JmhdUserDiary> diaryList = serviceDao.queryList("JmhdCmsDocument", sql.toString(), null);
			int pagesize = 15;
			page = page==0 ? 1 : page;
		    PageBuilder pb = new PageBuilder(pagesize);
		    pb.items(diaryList.size());
		    pb.page(page);
		    Object[] params = null;
//		    List<Map<String, Object>> list = serviceDao.queryForPage("JmhdCmsDocument", sql.toString(),(page-1) * pagesize,pagesize, params);
		    String hql = "select jm.title as title,jm.filldate as filldate,jm.id as id,jm.status as status,jm.ORDERNUM as ORDERNUM,jms.subjectname as subjectname from JMHD_CMS_DOCUMENT jm "
		    		+ "left join JMHD_CMS_SUBJECT jms on jm.category = jms.id ";
		    hql += " where "+sql.toString();
		    List<Map<String, Object>> list = serviceDao.queryForPageSql(hql,(page-1) * pagesize,pagesize, params);
		    map.put("success", Boolean.valueOf(true));
		    map.put("list", list);
		    map.put("pb", new PageModel(pb));
		    HttpHelper.writeHdptJSON(map, response,true);
		} catch (Exception e) {
			map.put("success", false);
			map.put("message", "查询失败");
			HttpHelper.writeHdptJSON(map, response,false);
		}
		
	}
	
	
	
	@RequestMapping(value = "/subList")
	public void subList(HttpServletRequest request,
			HttpServletResponse response,
			@ModelAttribute("info") JmhdCmsSubject info){
		
		 HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			String moduleName = info.getModuleName();
			String subjectname = info.getSubjectname();
			Date begindate = info.getBeginDate();
			Date enddate = info.getEndDate();
			int page = info.getPage();
			
			StringBuffer sql = new StringBuffer("1=1 ");
			List<Object> paramsArray = new ArrayList<Object>();
			
			if(!Util.empty(moduleName)){
				sql = sql.append("and moduleName like '%"+ moduleName +"%' ");
			}
			
			if(!Util.empty(subjectname)){
				sql = sql.append("and subjectname like '%"+ subjectname +"%' ");
			}
			
			if(!Util.empty(begindate)){
				sql = sql.append(" and createdate >= ?" );
				paramsArray.add(begindate);
			}

			if(!Util.empty(enddate)){
				sql = sql.append(" and createdate <= ?" );
				paramsArray.add(enddate);
			}
			
			Object[] obj = new Object[paramsArray.size()];
			obj = paramsArray.toArray(obj);
			
			int count = serviceDao.queryForCount("JmhdCmsSubject", sql.toString(), obj);
			int pagesize = 15;
			page = page==0 ? 1 : page;
		    PageBuilder pb = new PageBuilder(pagesize);
		    pb.items(count);
		    pb.page(page);
		    List<Map<String, Object>> list = serviceDao.queryForPage("JmhdCmsSubject", sql.toString(),(page-1) * pagesize,pagesize, obj);
		    map.put("success", Boolean.valueOf(true));
		    map.put("list", list);
		    map.put("pb", new PageModel(pb));
		    HttpHelper.writeHdptJSON(map, response,true);
		} catch (Exception e) {
			map.put("success", false);
			map.put("message", "查询失败");
			HttpHelper.writeHdptJSON(map, response,false);
		}
	}
	
	@RequestMapping(value = "/subFind")
	public void subFind(HttpServletRequest request,
			HttpServletResponse response,@RequestParam(value="id") Long id){
		JmhdCmsSubject cmssub = serviceDao.queryT("JmhdCmsSubject", "id=?", new Object[]{id});
		HashMap<String,Object> map = new HashMap<String,Object>();
		if(cmssub != null){
			map.put("cmssub", cmssub);
			map.put("success", true);
		}else{
			map.put("success", false);
			map.put("message", "此数据已经不存在!");
		}
		HttpHelper.writeHdptJSON(map, response,true);
	}
	
	
	@RequestMapping(value = "/subUpdate")
	public void subUpdate(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("info") JmhdCmsSubject info){
		HashMap<String,Object> map = new HashMap<String,Object>();
		try{
			info.setCreatedate(new Date());
			serviceDao.update("JmhdCmsSubject", info);
			map.put("success", true);
			map.put("message", "更新成功!");
		}catch(Exception e){
			map.put("success", false);
			map.put("message", "更新失败!");
		}
		HttpHelper.writeHdptJSON(map, response,true);
	}
	
	@RequestMapping(value = "/subAdd")
	public void subAdd(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("info") JmhdCmsSubject info){
		HashMap<String,Object> map = new HashMap<String,Object>();
		try{
			info.setCreator(request.getSession().getAttribute("adminName") == null ?  ""  : String.valueOf(request.getSession().getAttribute("adminName")));
			info.setCreatedate(new Date());
			serviceDao.save("JmhdCmsSubject", info);
			map.put("success", true);
			map.put("message", "添加成功!");
		}catch(Exception e){
			map.put("success", false);
			map.put("message", "添加失败!");
		}
		HttpHelper.writeHdptJSON(map, response,true);
	}
	
	@RequestMapping(value = "/subDel")
	public void subDel(HttpServletRequest request,
			HttpServletResponse response,@RequestParam(value="id") Long id){
		HashMap<String,Object> map = new HashMap<String,Object>();
		try{
			serviceDao.delete("JmhdCmsSubject", " id=? ", new Object[]{id});
			map.put("success", true);
			map.put("message", "删除成功!");
		}catch(Exception e){
			map.put("success", false);
			map.put("message", "删除失败!");
		}
		HttpHelper.writeHdptJSON(map, response,true);
	}
	
	@RequestMapping(value = "/documentToAdd")
	public void documentToAdd(HttpServletRequest request, HttpServletResponse response,@RequestParam(value="moduleCode") String moduleCode){
		List<JmhdCmsSubject> list =serviceDao.queryList("JmhdCmsSubject", "moduleCode=?", new Object[]{moduleCode});
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("sublist", list);
		HttpHelper.writeHdptJSON(map, response,true);
	}
	
	
	@RequestMapping(value = "/documentAdd")
	public void documentAdd(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("info") JmhdCmsDocument info){
		
		HashMap<String,Object> map = new HashMap<String,Object>();
		try{
			info.setCreator(request.getSession().getAttribute("adminName") == null ?  ""  : String.valueOf(request.getSession().getAttribute("adminName")));
			info.setFilldate(new Date());
			JmhdSysAdminuser user = (JmhdSysAdminuser)request.getSession().getAttribute("data");
			if(user != null){
				if("3".equals(user.getDatafield())){//卫生局权限新增文章默认审核通过
					info.setStatus("0");//审核通过
				}
				info.setOrgId(user.getOrgId());
			}
			String url = Util.getImgStr(info.getContent());
			if(!"".equals(url)){
				info.setUrl(url);
			}
			serviceDao.save("JmhdCmsDocument", info);
			map.put("success", true);
			map.put("message", "添加成功!");
		}catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("message", "添加失败!");
		}
		HttpHelper.writeHdptJSON(map, response,true);
	}
	
	@RequestMapping(value = "/documentFind")
	public void documentFind(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam(value="id") Long id,@RequestParam(value="moduleCode") String moduleCode){
		
		JmhdCmsDocument doc = serviceDao.queryT("JmhdCmsDocument", "id=?", new Object[]{id});
		HashMap<String,Object> map = new HashMap<String,Object>();
		if(doc != null){
			List<JmhdCmsSubject> list =serviceDao.queryList("JmhdCmsSubject", "moduleCode=?", new Object[]{moduleCode});
			map.put("sublist", list);
			map.put("jkwdoc", doc);
			map.put("success", true);
		}else{
			map.put("false", true);
			map.put("message", "此数据已经不存在");
		}
		HttpHelper.writeHdptJSON(map, response,true);
	}
	
	@RequestMapping(value = "/documentUpdate")
	public void documentUpdate(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("info") JmhdCmsDocument info){
		HashMap<String,Object> map = new HashMap<String,Object>();
		try{
			//审核状态与修改前一致
			JmhdCmsDocument jcd = serviceDao.queryT("JmhdCmsDocument", "id = ? ", new Object[]{info.getId()});
			info.setStatus(jcd.getStatus());
			info.setFilldate(new Date());
			JmhdSysAdminuser user = (JmhdSysAdminuser)request.getSession().getAttribute("data");
			if(user != null){
//				if("3".equals(user.getDatafield())){//卫生局权限新增文章默认审核通过
//					info.setStatus("0");//审核通过
//				}
				info.setOrgId(user.getOrgId());
			}
			String url = Util.getImgStr(info.getContent());
			if(!"".equals(url)){
				info.setUrl(url);
			}
			serviceDao.update("JmhdCmsDocument", info);
			map.put("success", true);
			map.put("message", "更新成功!");
		}catch(Exception e){
			e.printStackTrace();
			map.put("success", false);
			map.put("message", "更新失败!");
		}
		HttpHelper.writeHdptJSON(map, response,true);
	}
	
	
	@RequestMapping(value = "/documentDel")
	public void documentDel(HttpServletRequest request, HttpServletResponse response, @RequestParam(value="id") Long id){
		HashMap<String,Object> map = new HashMap<String,Object>();
		try{
			serviceDao.delete("JmhdCmsDocument", " id=? ", new Object[]{id});
			map.put("success", true);
			map.put("message", "删除成功!");
		}catch(Exception e){
			map.put("success", false);
			map.put("message", "删除失败!");
		}
		HttpHelper.writeHdptJSON(map, response,true);
	}
	
	@RequestMapping(value = "/documentReview")
	public void documentReview(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("info") JmhdCmsDocument info){
		HashMap<String,Object> map = new HashMap<String,Object>();
		try{
			JmhdCmsDocument cms = serviceDao.queryT("JmhdCmsDocument", "id=?", new Object[]{info.getId()});
			cms.setStatus(info.getStatus());//0：审核通过  1 审核不通过
			serviceDao.update("JmhdCmsDocument", cms);
			map.put("success", true);
			map.put("message", "审核成功!");
		}catch(Exception e){
			e.printStackTrace();
			map.put("success", false);
			map.put("message", "审核失败!");
		}
		HttpHelper.writeHdptJSON(map, response,true);
	}
	
	
	@RequestMapping(value = "/jkzxlist")
	public void jkzxlist(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "type", required = false) String type,
			@ModelAttribute("info") JmhdJkzx info){
		
		 HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			JmhdSysAdminuser  adminuser = (JmhdSysAdminuser) request.getSession().getAttribute("data");
			String manaunitid = "";
			String datafield = "";
			if(adminuser != null){
				manaunitid = adminuser.getOrgId();
				datafield = adminuser.getDatafield();
			}
			String titlefirst = info.getTitlefirst();
			Date begindate = info.getBeginDate();
			Date enddate = info.getEndDate();
			int page = info.getPage();
			StringBuffer sql;
			if(type == null){
				sql = new StringBuffer("1=1 ");
			}else{
				sql = new StringBuffer("type="+type+" ");
			}
			
			List<Object> paramsArray = new ArrayList<Object>();
			
			if(!Util.empty(titlefirst)){
				sql = sql.append("and titlefirst like '%"+ titlefirst +"%' ");
			}
			
			if(!Util.empty(begindate)){
				sql = sql.append(" and trunc(createdate) >= ?" );
				paramsArray.add(begindate);
			}

			if(!Util.empty(enddate)){
				sql = sql.append(" and trunc(createdate) <= ?" );
				paramsArray.add(enddate);
			}

			if(!Util.empty(manaunitid) && !"3".equals(datafield)){ //所属机构不空，权限不是卫生局
				sql = sql.append("and orgId ="+manaunitid);
			}
			sql.append("order by createdate desc");
			
			Object[] obj = new Object[paramsArray.size()];
			obj = paramsArray.toArray(obj);
			
			int count = serviceDao.queryForCount("JmhdJkzx", sql.toString(), obj);
			int pagesize = 15;
			page = page==0 ? 1 : page;
		    PageBuilder pb = new PageBuilder(pagesize);
		    pb.items(count);
		    pb.page(page);
		    List<Map<String, Object>> list = serviceDao.queryForPage("JmhdJkzx", sql.toString(),(page-1) * pagesize,pagesize, obj);
		    map.put("success", Boolean.valueOf(true));
		    map.put("list", list);
		    map.put("pb", new PageModel(pb));
		    HttpHelper.writeHdptJSON(map, response,true);
		} catch (Exception e) {
			map.put("success", false);
			map.put("message", "查询失败");
			HttpHelper.writeHdptJSON(map, response,false);
		}
	}
	
	@RequestMapping(value = "/jkzxAdd")
	public void jkzxAdd(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("info") JmhdJkzx info){
		
		HashMap<String,Object> map = new HashMap<String,Object>();
		try{
			info.setCreatedate(new Date());
			JmhdSysAdminuser user = (JmhdSysAdminuser)request.getSession().getAttribute("data");
			if(user != null){
				if("3".equals(user.getDatafield())){//卫生局权限新增文章默认审核通过
					info.setStatus("0");//审核通过
				}
				info.setOrgId(user.getOrgId());
			}
			String url = Util.getImgStr(info.getSubject());
			if(!"".equals(url)){
				info.setUrl(url);
			}
			serviceDao.save("JmhdJkzx", info);
			map.put("success", true);
			map.put("message", "添加成功!");
		}catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("message", "添加失败!");
		}
		HttpHelper.writeHdptJSON(map, response,true);
	}
	
	@RequestMapping(value = "/jkzxFind")
	public void jkzxFind(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam(value="id") Long id,@RequestParam(value="moduleCode") String moduleCode){
		
		JmhdJkzx doc = serviceDao.queryT("JmhdJkzx", "zxid=?", new Object[]{id});
		HashMap<String,Object> map = new HashMap<String,Object>();
		if(doc != null){
			List<JmhdCmsSubject> list =serviceDao.queryList("JmhdCmsSubject", "moduleCode=?", new Object[]{moduleCode});
			map.put("sublist", list);
			map.put("jkzx", doc);
			map.put("success", true);
		}else{
			map.put("false", true);
			map.put("message", "此数据已经不存在");
		}
		HttpHelper.writeHdptJSON(map, response,true);
	}
	
	@RequestMapping(value = "/jkzxQuerybyid")
	public void jkzxQuerybyid(HttpServletRequest request, HttpServletResponse response, @RequestParam(value="id") Long id){
				
		HashMap<String,Object> map = new HashMap<String,Object>();
		try{
			List<JmhdCmsSubject> list =serviceDao.queryList("JmhdJkzx", "zxid="+id, null);
			map.put("sublist", list);			
			map.put("success", true);
		}catch (Exception e) {
			e.printStackTrace();
			map.put("false", true);
			map.put("message", "此数据已经不存在");
		}					
		HttpHelper.writeHdptJSON(map, response,true);
	}
	
	@RequestMapping(value = "/jkzxUpdate")
	public void jkzxUpdate(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("info") JmhdJkzx info){
		HashMap<String,Object> map = new HashMap<String,Object>();
		try{
			info.setCreatedate(new Date());
			JmhdSysAdminuser user = (JmhdSysAdminuser)request.getSession().getAttribute("data");
			if(user != null){
				if("3".equals(user.getDatafield())){//卫生局权限新增文章默认审核通过
					info.setStatus("0");//审核通过
				}
				info.setOrgId(user.getOrgId());
			}
			String url = Util.getImgStr(info.getSubject());
			if(!"".equals(url)){
				info.setUrl(url);
			}
			serviceDao.update("JmhdJkzx", info);
			map.put("success", true);
			map.put("message", "更新成功!");
		}catch(Exception e){
			e.printStackTrace();
			map.put("success", false);
			map.put("message", "更新失败!");
		}
		HttpHelper.writeHdptJSON(map, response,true);
	}
	
	
	@RequestMapping(value = "/jkzxDel")
	public void jkzxDel(HttpServletRequest request, HttpServletResponse response, @RequestParam(value="id") Long id){
		HashMap<String,Object> map = new HashMap<String,Object>();
		try{
			serviceDao.delete("JmhdJkzx", "zxid=?", new Object[]{id});
			map.put("success", true);
			map.put("message", "删除成功!");
		}catch(Exception e){
			map.put("success", false);
			map.put("message", "删除失败!");
		}
		HttpHelper.writeHdptJSON(map, response,true);
	}
	
	@RequestMapping(value = "/jkzxReview")
	public void jkzxReview(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("info") JmhdJkzx info){
		HashMap<String,Object> map = new HashMap<String,Object>();
		try{
			JmhdJkzx jkzx = serviceDao.queryT("JmhdJkzx", "zxid=?", new Object[]{info.getZxid()});
			jkzx.setStatus(info.getStatus());//0：审核通过  1 审核不通过
			serviceDao.update("JmhdJkzx", jkzx);
			map.put("success", true);
			map.put("message", "审核成功!");
		}catch(Exception e){
			e.printStackTrace();
			map.put("success", false);
			map.put("message", "审核失败!");
		}
		HttpHelper.writeHdptJSON(map, response,true);
	}
	
	
	@RequestMapping(value = "/wszwlist")
	public void wszwlist(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "type", required = false) String type,
			@ModelAttribute("info") JmhdJkzx info){
		
		 HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			
			String titlefirst = info.getTitlefirst();
			Date begindate = info.getBeginDate();
			Date enddate = info.getEndDate();
			int page = info.getPage();
			
			StringBuffer sql;
			if(type == null){
				sql = new StringBuffer("1=1 ");
			}else{
				sql = new StringBuffer("type="+type+" ");
			}
			
			List<Object> paramsArray = new ArrayList<Object>();
			
			if(!Util.empty(titlefirst)){
				sql = sql.append("and titlefirst like '%"+ titlefirst +"%' ");
			}
			
			if(!Util.empty(begindate)){
				sql = sql.append(" and trunc(createdate) >= ?" );
				paramsArray.add(begindate);
			}

			if(!Util.empty(enddate)){
				sql = sql.append(" and trunc(createdate) <= ?" );
				paramsArray.add(enddate);
			}
			
			sql.append("order by createdate desc");
			
			Object[] obj = new Object[paramsArray.size()];
			obj = paramsArray.toArray(obj);
			
			int count = serviceDao.queryForCount("JmhdWszw", sql.toString(), obj);
			int pagesize = 15;
			page = page==0 ? 1 : page;
		    PageBuilder pb = new PageBuilder(pagesize);
		    pb.items(count);
		    pb.page(page);
		    List<Map<String, Object>> list = serviceDao.queryForPage("JmhdWszw", sql.toString(),(page-1) * pagesize,pagesize, obj);
		    map.put("success", Boolean.valueOf(true));
		    map.put("list", list);
		    map.put("pb", new PageModel(pb));
		    HttpHelper.writeHdptJSON(map, response,true);
		} catch (Exception e) {
			map.put("success", false);
			map.put("message", "查询失败");
			HttpHelper.writeHdptJSON(map, response,false);
		}
	}
	
	@RequestMapping(value = "/wszwQuerybyid")
	public void wszwQuerybyid(HttpServletRequest request, HttpServletResponse response, @RequestParam(value="id") Long id){
				
		HashMap<String,Object> map = new HashMap<String,Object>();
		try{
			List<JmhdCmsSubject> list =serviceDao.queryList("JmhdWszw", "zwid="+id, null);
			map.put("sublist", list);			
			map.put("success", true);
		}catch (Exception e) {
			e.printStackTrace();
			map.put("false", true);
			map.put("message", "此数据已经不存在");
		}					
		HttpHelper.writeHdptJSON(map, response,true);
	}
	
	@RequestMapping(value = "/wszwAdd")
	public void wszwAdd(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("info") JmhdWszw info){
		
		HashMap<String,Object> map = new HashMap<String,Object>();
		try{
			info.setCreatedate(new Date());
			JmhdSysAdminuser user = (JmhdSysAdminuser)request.getSession().getAttribute("data");
			if("3".equals(user.getDatafield())){//卫生局权限新增文章默认审核通过
				info.setStatus("0");//审核通过
			}
			String url = Util.getImgStr(info.getSubject());
			if(!"".equals(url)){
				info.setUrl(url);
			}
			serviceDao.save("JmhdWszw", info);
			map.put("success", true);
			map.put("message", "添加成功!");
		}catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("message", "添加失败!");
		}
		HttpHelper.writeHdptJSON(map, response,true);
	}
	
	@RequestMapping(value = "/wszwFind")
	public void wszwFind(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam(value="id") Long id,@RequestParam(value="moduleCode") String moduleCode){
		
		JmhdWszw doc = serviceDao.queryT("JmhdWszw", "zwid=?", new Object[]{id});
		HashMap<String,Object> map = new HashMap<String,Object>();
		if(doc != null){
			List<JmhdCmsSubject> list =serviceDao.queryList("JmhdCmsSubject", "moduleCode=?", new Object[]{moduleCode});
			map.put("sublist", list);
			map.put("wszw", doc);
			map.put("success", true);
		}else{
			map.put("false", true);
			map.put("message", "此数据已经不存在");
		}
		HttpHelper.writeHdptJSON(map, response,true);
	}
	
	@RequestMapping(value = "/wszwUpdate")
	public void wszwUpdate(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("info") JmhdWszw info){
		HashMap<String,Object> map = new HashMap<String,Object>();
		try{
			info.setCreatedate(new Date());
			JmhdSysAdminuser user = (JmhdSysAdminuser)request.getSession().getAttribute("data");
			if("3".equals(user.getDatafield())){//卫生局权限新增文章默认审核通过
				info.setStatus("0");//审核通过
			}
			String url = Util.getImgStr(info.getSubject());
			if(!"".equals(url)){
				info.setUrl(url);
			}
			serviceDao.update("JmhdWszw", info);
			map.put("success", true);
			map.put("message", "更新成功!");
		}catch(Exception e){
			e.printStackTrace();
			map.put("success", false);
			map.put("message", "更新失败!");
		}
		HttpHelper.writeHdptJSON(map, response,true);
	}
	
	
	@RequestMapping(value = "/wszwDel")
	public void wszwDel(HttpServletRequest request, HttpServletResponse response, @RequestParam(value="id") Long id){
		HashMap<String,Object> map = new HashMap<String,Object>();
		try{
			serviceDao.delete("JmhdWszw", "zwid=? ", new Object[]{id});
			map.put("success", true);
			map.put("message", "删除成功!");
		}catch(Exception e){
			map.put("success", false);
			map.put("message", "删除失败!");
		}
		HttpHelper.writeHdptJSON(map, response,true);
	}
	
	@RequestMapping(value = "/wszwReview")
	public void wszwReview(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("info") JmhdWszw info){
		HashMap<String,Object> map = new HashMap<String,Object>();
		try{
			JmhdWszw wszw = serviceDao.queryT("JmhdWszw", "zwid=?", new Object[]{info.getZwid()});
			wszw.setStatus(info.getStatus());//0：审核通过  1 审核不通过
			serviceDao.update("JmhdWszw", wszw);
			map.put("success", true);
			map.put("message", "审核成功!");
		}catch(Exception e){
			e.printStackTrace();
			map.put("success", false);
			map.put("message", "审核失败!");
		}
		HttpHelper.writeHdptJSON(map, response,true);
	}
	
	/**
	 * 获取文章详细
	 * @param request
	 * @param response
	 * @param id
	 * @throws IOException
	 */
	@RequestMapping(value = "/docInfo")
	public void tzggInfo(HttpServletRequest request, HttpServletResponse response
			, @RequestParam(value="id") Long id) throws IOException {
		try {
			HashMap<String,Object> map = new HashMap<String,Object>();

			JmhdCmsDocument document = serviceDao.queryT("JmhdCmsDocument", "id=?", new Object[]{id});

			if(document != null){
				String content = document.getContent();
				content = ServerUrlUtil.addImgServerUrl(request, content);
				document.setContent(content);

				String url = document.getUrl();
				if(url!=null && !"".equals(url)){
					document.setUrl(ServerUrlUtil.getServerUrl(request) + url);
				}

				map.put("docInfo", document);
				map.put("success", true);
			}else{
				map.put("success", false);
				map.put("message", "文章信息不存在，请联系管理员！");
			}

			HttpHelper.writeHdptJSON(map, response, true);
		} catch (Exception e) {
			LOGGER.error("tzggInfo", e);
			HttpHelper.writeHdptJSON(null, response,false);
		}
	}
	/**
	 * 文章列表获取，暂用于门户
	 * @param request
	 * @param response
	 * @param perPageSize	每页显示条目数
	 * @param pageNum		当前页码
	 * @param keyword		标题模糊查询的关键字
	 * @param categoryName	文章所属栏目中文名
	 * @throws IOException
	 */
	@RequestMapping(value = "/documentList")
	public void documentList(HttpServletRequest request, HttpServletResponse response
			, @RequestParam(value="perPageSize", required=false) Integer perPageSize
			, @RequestParam(value="pageNum", required=false) Integer pageNum
			, @RequestParam(value="keyword", required=false) String keyword
			, @RequestParam(value="categoryName") String categoryName) throws IOException {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			JmhdCmsSubject noticeSub = serviceDao.queryT("JmhdCmsSubject", "description=?", new Object[]{categoryName});
			String subjectID = String.valueOf(noticeSub.getId());
			String clazz = "JmhdCmsDocument";
			String condition = "category=? and status='0' order by filldate desc";
			Object[] params = new Object[]{subjectID};
			
			if(keyword!=null && !"".equals(keyword)){
				//中文解码
				try {
					keyword =URLDecoder.decode(keyword,"UTF-8");
				} catch (Exception e) {
					e.printStackTrace();
				}
				condition = "category=? and status='0' and title like '%"+keyword+"%' order by filldate desc";
			}
			
			int total = serviceDao.queryForCount(clazz, condition, params);
			
			List<Map<String, Object>> list = null;
			if(perPageSize == null && pageNum == null){
				//不分页
				list = serviceDao.queryForList(clazz, condition, params);
			}else{
				//分页
				PageCount pc = new PageCount(total, perPageSize.intValue(), pageNum);
				list = serviceDao.queryForPage(clazz, condition, pc.getStartRow(), pc.getPerPage(), params);
				map.put("totalPage", pc.getTotalPage());
			}
			
			//处理内容剪切掉多余部分
			for(int i=0, len=list.size(); i<len; i++){
				JmhdCmsDocument obj = (JmhdCmsDocument)(list.get(i));
				int lengthLimit = 50;
				//去掉html标签
				Pattern p_style = Pattern.compile("<[^>]+>", Pattern.CASE_INSENSITIVE);
				Matcher m_style = p_style.matcher(obj.getContent() == null ? "" : obj.getContent());
				StringBuilder content = new StringBuilder(m_style.replaceAll(""));

				if (content.length() > lengthLimit) {
					content.delete(lengthLimit, content.length());
					content.append("...");
				}
				obj.setContent(content.toString());
				String url = obj.getUrl();
				if(url!=null && !"".equals(url)){
					obj.setUrl(ServerUrlUtil.convImgPath4Wx(url.split(ServerUrlUtil.SEPARATOR)[0], request));
				}
			}
			
			map.put("list", list);
			
			HttpHelper.writeHdptJSON(map, response, true);
		} catch (Exception e) {
			LOGGER.error("documentList", e);
			HttpHelper.writeHdptJSON(map, response,true);
		}
	}

	/**
	 * 根据栏目获取最新的N条带图片文章列表
	 * @param request
	 * @param response
	 * @param categoryName
     * @param showNum
     */
	@RequestMapping(value = "/docImgList")
	public void docImgList(HttpServletRequest request, HttpServletResponse response
			, @RequestParam(value="categoryName") String categoryName
			, @RequestParam(value="showNum") int showNum){
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			JmhdCmsSubject noticeSub = serviceDao.queryT("JmhdCmsSubject", "description=?", new Object[]{categoryName});
			String subjectID = String.valueOf(noticeSub.getId());
			String clazz = "JmhdCmsDocument";
			String condition = "category=? and status='0' and url is not null order by filldate desc";
			Object[] params = new Object[]{subjectID};
			String serverUrl = ServerUrlUtil.getServerUrl(request);
			int total = serviceDao.queryForCount(clazz, condition, params);
			PageCount pc = new PageCount(total, showNum);
			List<Map<String, Object>> tempList = serviceDao.queryForPage(clazz, condition, pc.getStartRow(), pc.getPerPage(), params);
			List<Map<String, Object>> resultList  = new ArrayList<Map<String, Object>>();

			for (int i = 0; i < tempList.size(); i++) {
				JmhdCmsDocument doc = (JmhdCmsDocument)(tempList.get(i));
				Map<String, Object> singleMap = new HashMap<String, Object>();
				singleMap.put("id", doc.getId());
				singleMap.put("title", doc.getTitle());
				singleMap.put("url", serverUrl + doc.getUrl().split(ServerUrlUtil.SEPARATOR)[0]);
				resultList.add(singleMap);
			}
			map.put("list", resultList);

		}catch (Exception e){
			LOGGER.error("docImgList", e);
		}
		HttpHelper.writeHdptJSON(map, response, true);
	}
	/**
	 * 健康咨询列表查询，用于门户，可分页
	 * @param request
	 * @param response
	 * @param perPageSize
	 * @param pageNum
	 * @param types		栏目ID集合，以逗号分隔
	 * @throws IOException
	 */
	@RequestMapping(value = "/jkzxList4Page")
	public void jkzxList4Page(HttpServletRequest request, HttpServletResponse response
			, @RequestParam(value="perPageSize", required=false) Integer perPageSize
			, @RequestParam(value="pageNum", required=false) Integer pageNum
			, @RequestParam(value="types") String types) throws IOException {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			String[] typeArry = types.split(",");
			StringBuilder subCondition = new StringBuilder("description in(");
			for(int i=0, len=typeArry.length; i<len; i++){
				subCondition.append("?,");
			}
			subCondition.deleteCharAt(subCondition.length()-1);
			subCondition.append(")");
			
			List<Map<String, Object>> jkzxSubList = serviceDao.queryForList("JmhdCmsSubject", subCondition.toString(), typeArry);
			StringBuilder condition = new StringBuilder("type in(");
			Object[] typeIDArry = new Object[jkzxSubList.size()];
			Map<String, JmhdCmsSubject> subMap = new HashMap<String, JmhdCmsSubject>();
			for(int i=0, len=jkzxSubList.size(); i<len; i++){
				JmhdCmsSubject obj = (JmhdCmsSubject)(jkzxSubList.get(i));
				
				typeIDArry[i] = obj.getId();
				condition.append("?,");
				subMap.put(String.valueOf(obj.getId()), obj);
			}
			condition.deleteCharAt(condition.length()-1);
			condition.append(") and status='0' order by createdate desc");
			
			String clazz = "JmhdJkzx";
			List<Map<String, Object>> list = null;
			if(perPageSize == null && pageNum == null){
				//不分页
				list = serviceDao.queryForList(clazz, condition.toString(), typeIDArry);
			}else{
				//分页
				int total = serviceDao.queryForCount(clazz, condition.toString(), typeIDArry);
				PageCount pc = new PageCount(total, perPageSize.intValue(), pageNum);
				list = serviceDao.queryForPage(clazz, condition.toString(), pc.getStartRow(), pc.getPerPage(), typeIDArry);
				map.put("totalPage", pc.getTotalPage());
			}

			int lengthLimit = 50;
			for(int i=0, len=list.size(); i<len; i++){
				JmhdJkzx obj = (JmhdJkzx)(list.get(i));

				//去掉html标签
				Pattern p_style = Pattern.compile("<[^>]+>", Pattern.CASE_INSENSITIVE);
				Matcher m_style = p_style.matcher(obj.getSubject() == null ? "" : obj.getSubject());
				StringBuilder content = new StringBuilder(m_style.replaceAll(""));

				if (content.length() > lengthLimit) {
					content.delete(lengthLimit, content.length());
					content.append("...");
				}
				obj.setSubject(content.toString());

				String url = obj.getUrl();
				if (url != null && !"".equals(url)) {
					obj.setUrl(ServerUrlUtil.getServerUrl(request) + url);
				}
			}

			map.put("list", list);
			map.put("subMap", subMap);
			
			HttpHelper.writeHdptJSON(map, response, true);
		} catch (Exception e) {
			LOGGER.error("jkzxList4Page", e);
			HttpHelper.writeHdptJSON(map, response,true);
		}
	}

	/**
	 * 健康咨询详细信息
	 * @param request
	 * @param response
	 * @param id
	 * @throws IOException
	 */
	@RequestMapping(value = "/jkzxInfo4Page")
	public void jkzxInfo4Page(HttpServletRequest request, HttpServletResponse response
			, @RequestParam(value="id") Long id) throws IOException {
		try {
			HashMap<String,Object> map = new HashMap<String,Object>();
			
			JmhdJkzx document = serviceDao.queryT("JmhdJkzx", "zxid=?", new Object[]{id});
			if(document != null){
				String content = document.getSubject();
				content = ServerUrlUtil.addImgServerUrl(request, content);
				document.setSubject(content);
				
				String url = document.getUrl();
				if(url!=null && !"".equals(url)){
					document.setUrl(ServerUrlUtil.getServerUrl(request) + url);
				}
				
				map.put("jkzxInfo", document);
				map.put("success", true);
			}else{
				map.put("success", false);
				map.put("message", "健康资讯不存在，请联系管理员！");
			}
			
			HttpHelper.writeHdptJSON(map, response, true);
		} catch (Exception e) {
			LOGGER.error("jkzxInfo4Page", e);
			HttpHelper.writeHdptJSON(null, response,false);
		}
	}

	/**
	 * 根据指定的栏目名，查询列表
	 * 暂只用于微信
	 * @param request
	 * @param response
	 * @param subject
	 * @param pageSize
	 * @param pageNum
     */
	@RequestMapping(value = "/docList4wx")
	public void docList4wx(HttpServletRequest request, HttpServletResponse response
			, @RequestParam(value="subject") String subject
			, @RequestParam(value="pageSize") int pageSize
			, @RequestParam(value="pageNum") int pageNum){
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		String msg = "";
		try {
			if(subject == null || subject.isEmpty()){
				msg = "Parameter[subject] is null!";
				throw new NullPointerException(msg);
			}
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if("jjxc".equals(subject)){
				//健教宣传
				String subCondition = "description in('jkts','bjyf','shbk','hxyh','jbcs','xljk')";

				List<Map<String, Object>> jkzxSubList = serviceDao.queryForList("JmhdCmsSubject", subCondition, new Object[]{});
				StringBuilder condition = new StringBuilder("type in(");
				Object[] typeIDArry = new Object[jkzxSubList.size()];
				Map<String, JmhdCmsSubject> subMap = new HashMap<String, JmhdCmsSubject>();
				for(int i=0; i<jkzxSubList.size(); i++){
					JmhdCmsSubject obj = (JmhdCmsSubject)(jkzxSubList.get(i));
					typeIDArry[i] = obj.getId();
					condition.append("?,");
					subMap.put(String.valueOf(obj.getId()), obj);
				}
				condition.deleteCharAt(condition.length()-1);
				condition.append(") and status='0' order by createdate desc");

				String clazz = "JmhdJkzx";
				int total = serviceDao.queryForCount(clazz, condition.toString(), typeIDArry);
				PageCount pc = new PageCount(total, pageSize, pageNum);
				List<Map<String, Object>> tempList = serviceDao.queryForPage(clazz, condition.toString(), pc.getStartRow(), pc.getPerPage(),typeIDArry);
				resultMap.put("totalPage", pc.getTotalPage());
				//拼装需返回数据
				for(int i=0, len=tempList.size(); i<len; i++){
					JmhdJkzx obj = (JmhdJkzx)(tempList.get(i));
					if (obj.getUrl() != null && !obj.getUrl().isEmpty()) {
						obj.setUrl(ServerUrlUtil.convImgPath4Wx(obj.getUrl(), request).split(ServerUrlUtil.SEPARATOR)[0]);
					}

					HashMap<String, String> singleMap = new HashMap<String, String>();
					singleMap.put("id", String.valueOf(obj.getZxid()));
					singleMap.put("title", obj.getTitlefirst());
					singleMap.put("url", obj.getUrl());
					singleMap.put("content", obj.getSubject());
					singleMap.put("createDate", sdf.format(obj.getCreatedate()));
					singleMap.put("subtitle", obj.getTitlesecond());
					list.add(singleMap);
				}

			}else if("tzgg".equals(subject) || "yyjs".equals(subject) || "zjtj".equals(subject)){
				//通知公告
				JmhdCmsSubject noticeSub = serviceDao.queryT("JmhdCmsSubject", "description=?", new Object[]{subject});
				String subjectID = String.valueOf(noticeSub.getId());
				Object[] params = new Object[]{subjectID};
				String clazz = "JmhdCmsDocument";
				String condition = "category=? and status='0' order by filldate desc";
				int total = serviceDao.queryForCount(clazz, condition, params);
				PageCount pc = new PageCount(total, pageSize, pageNum);
				List<Map<String, Object>> tempList = serviceDao.queryForPage(clazz, condition, pc.getStartRow(), pc.getPerPage(), params);
				resultMap.put("totalPage", pc.getTotalPage());

				//拼装需返回数据
				for(int i=0, len=tempList.size(); i<len; i++){
					JmhdCmsDocument obj = (JmhdCmsDocument)(tempList.get(i));

					if (obj.getUrl() != null && !obj.getUrl().isEmpty()) {
						obj.setUrl(ServerUrlUtil.convImgPath4Wx(obj.getUrl(), request));
					}

					HashMap<String, String> singleMap = new HashMap<String, String>();
					singleMap.put("id", String.valueOf(obj.getId()));
					singleMap.put("title", obj.getTitle());
					singleMap.put("urls", obj.getUrl());
					singleMap.put("content", obj.getContent());
					singleMap.put("createDate", sdf.format(obj.getFilldate()));
					singleMap.put("subtitle", obj.getSubtitle());
					list.add(singleMap);
				}
			}else{
				msg = "Subject is not exist!-->subject["+subject+"]";
				throw new NullPointerException(msg);
			}

			//截取部分内容作为预览
			int lengthLimit = 20;
			for (Map<String, String> map : list) {
				String contentStr = map.get("content");
				Pattern p_style = Pattern.compile("<[^>]+>", Pattern.CASE_INSENSITIVE);
				Matcher m_style = p_style.matcher(contentStr == null ? "" : contentStr);
				StringBuilder content = new StringBuilder(m_style.replaceAll(""));

				if (content.length() > lengthLimit) {
					content.delete(lengthLimit, content.length());
					content.append("...");
				}
				map.put("content", content.toString());
			}

			resultMap.put("result", list);
			resultMap.put("success", true);
		}catch (Exception e){
			if(msg.isEmpty()){
				msg = e.getMessage();
			}
			LOGGER.error(msg, e);
			resultMap.put("success", false);
			resultMap.put("message", msg);
		}
		HttpHelper.writeHdptJSON(resultMap, response, true);
	}

	/**
	 * 获取指定文章内容
	 * 暂用于微信
	 * @param request
	 * @param response
	 * @param subject
     * @param id
     */
	@RequestMapping(value = "/docInfo4wx")
	public void docInfo4wx(HttpServletRequest request, HttpServletResponse response
			, @RequestParam(value="subject") String subject
			, @RequestParam(value="id") Long id){
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, String> docInfo = new HashMap<String, String>();
		String msg = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if(subject == null || subject.isEmpty()){
				msg = "Parameter[subject] is null!";
				throw new NullPointerException(msg);
			}

			if("jjxc".equals(subject)){
				JmhdJkzx doc = serviceDao.queryT("JmhdJkzx", "zxid=?", new Object[]{id});
				docInfo.put("id", String.valueOf(id));
				docInfo.put("title", doc.getTitlefirst());
				docInfo.put("subtitle", doc.getTitlesecond());
				docInfo.put("createDate", sdf.format(doc.getCreatedate()));
				docInfo.put("content", ServerUrlUtil.convContentImg4Wx(doc.getSubject(), request));

			}else if("tzgg".equals(subject) || "yyjs".equals(subject) || "zjtj".equals(subject)){
				JmhdCmsDocument doc = serviceDao.queryT("JmhdCmsDocument", "id=?", new Object[]{id});
				docInfo.put("id", String.valueOf(id));
				docInfo.put("title", doc.getTitle());
				docInfo.put("subtitle", doc.getSubtitle());
				docInfo.put("createDate", sdf.format(doc.getFilldate()));
				docInfo.put("content", ServerUrlUtil.convContentImg4Wx(doc.getContent(), request));
			}else{
				msg = "Subject is not exist!-->subject["+subject+"]";
				throw new NullPointerException(msg);
			}
			resultMap.put("result", docInfo);
			resultMap.put("success", true);
		}catch (Exception e){
			if(msg.isEmpty()){
				msg = e.getMessage();
			}
			LOGGER.error(msg, e);
			resultMap.put("success", false);
			resultMap.put("message", msg);
		}
		HttpHelper.writeHdptJSON(resultMap, response, true);
	}
}
