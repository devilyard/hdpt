package dc.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bsoft.xds.support.dao.IDAO;

import ctd.util.MD5StringUtil;

import dc.domain.adminuser.JmhdRelatedAdminRole;
import dc.domain.adminuser.JmhdSysAdminuser;
import dc.service.IRoleService;
import dc.util.HttpHelper;
import dc.util.PageBuilder;
import dc.util.PageModel;
import dc.util.Util;

/**
 * @author Administrator
 *
 */
@Controller
public class AdminUserController {
	@Autowired
	private IDAO serviceDao;
	
	@Autowired
	@Qualifier("adminUserService")
	private IRoleService service;
	
	@RequestMapping(value = "/adminUserList")
	public void adminUserList(HttpServletRequest request,HttpServletResponse response,
			@ModelAttribute("info") JmhdSysAdminuser info)
			throws IOException {
		 HashMap<String,Object> map = new HashMap<String,Object>();
			try {
				
				String adminName = info.getAdminName();
				String loginName = info.getLoginName();
				int page = info.getPage();
				
				StringBuffer sql = new StringBuffer("1=1 ");
				
				JmhdSysAdminuser user =  (JmhdSysAdminuser)request.getSession().getAttribute("data");
				
//				if(!"admin".equals(user.getLoginName())){
//					
//					//TODO
//					
//					sql = sql.append("and loginName like '%"+ loginName +"%' ");
//				}
				
				if(!Util.empty(adminName)){
					sql = sql.append("and adminName like '%"+ adminName +"%' ");
				}
				
				if(!Util.empty(loginName)){
					sql = sql.append("and loginName like '%"+ loginName +"%' ");
				}
				
				int count = serviceDao.queryForCount("JmhdSysAdminuser", sql.toString(), new Object[]{});
				int pagesize = 15;
				page = page==0 ? 1 : page;
			    PageBuilder pb = new PageBuilder(pagesize);
			    pb.items(count);
			    pb.page(page);
			    List<Map<String, Object>> list = serviceDao.queryForPage("JmhdSysAdminuser", sql.toString(),(page-1) * pagesize,pagesize, new Object[]{});
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
	
	@RequestMapping(value = "/fetchTopManaunit")
	public void fetchTopManaunit(HttpServletRequest request,HttpServletResponse response)
			throws IOException {
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			JmhdSysAdminuser user =  (JmhdSysAdminuser)request.getSession().getAttribute("data");
			List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();
			if(!"admin".equals(user.getLoginName())){
				String orgid= user.getOrgId();
				if(orgid.length()==4){
					list = serviceDao.queryForList("EHR_MANAUNIT", "length(MANAUNITID)=?", new Object[]{6});
				}else{
					orgid=orgid.substring(0,6);
					list = serviceDao.queryForList("EHR_MANAUNIT", "MANAUNITID=?", new Object[]{orgid});
				}
			}else{
				list = serviceDao.queryForList("EHR_MANAUNIT", "length(MANAUNITID)=?", new Object[]{6});
			}
			map.put("success", true);
			map.put("orglist", list);
			HttpHelper.writeHdptJSON(map, response,true);
		}catch (Exception e) {
			HttpHelper.writeHdptJSON(null, response,false);
		}
	}
	
	@RequestMapping(value = "/fetchSubsManaunit")
	public void fetchSubsManaunit(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "orgparentid", required = true) String orgparentid){
		
		Map<String,Object> map = new HashMap<String,Object>();
		try{
//			int length = 0;
//			if (orgparentid.length() == 6) {
//				length = 8;
//			} else {
//				length = orgparentid.length() + 2;
//			}
			JmhdSysAdminuser user =  (JmhdSysAdminuser)request.getSession().getAttribute("data");
			List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();
			if(!"admin".equals(user.getLoginName())){
				String orgid= user.getOrgId();
//				if(orgid.length()>=length){
//					orgid=orgid.substring(0,length);
//					list = serviceDao.queryForList("EHR_MANAUNIT", "MANAUNITID =? ", new Object[]{orgparentid});
//				}else{
					list = serviceDao.queryForList("EHR_MANAUNIT", "MANAUNITID like '"+ orgid +"%'", new Object[]{});
//				}
			}else{
				list = serviceDao.queryForList("EHR_MANAUNIT", "MANAUNITID like '"+ orgparentid +"%'", new Object[]{});
			}
			if (list != null && list.size() > 0) {
				map.put("success", true);
				map.put("orglist", list);
			}else{
				map.put("success", false);
			}
			HttpHelper.writeHdptJSON(map, response,true);
		}catch (Exception e) {
			HttpHelper.writeHdptJSON(null, response,false);
		}
	}
	
	
	@RequestMapping(value = "/findAdminUser")
	public void findAdminUser(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "id", required = true) Integer id,
			@RequestParam(value = "loginName", required = true) String loginName)
			throws IOException {
		
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			
			if ("admin".equals(loginName)) {
				map.put("success", false);
				map.put("message", "管理员账户不允许更新!");
			}else{
				
				List<Map<String, Object>> list = serviceDao.queryForListSQL("select t.*,em.manaunitname as org_name " +
						"from jmhd_sys_adminuser t left join EHR_MANAUNIT em on t.org_id=em.manaunitid where t.ADMIN_ID =?", new Object[]{id});
				if(list != null && list.size()>0){
					map.put("success", true);
					map.put("adminuser", list.get(0));
				}else{
					map.put("success", false);
					map.put("message", "用户不存在或者已经被删除!");
				}
			}
			HttpHelper.writeHdptJSON(map, response,true);
		}catch (Exception e) {
			HttpHelper.writeHdptJSON(null, response,false);
		}
	}
	
	
	@RequestMapping(value = "/findAdminUserRoles")
	public void findAdminUserRoles(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "id", required = true) Integer id)
			throws IOException {
		try{
			Map<String,Object> map = new HashMap<String,Object>();
			JmhdSysAdminuser adminuser = serviceDao.queryT("JmhdSysAdminuser", "adminId=?",  new Object[]{id});
			JmhdSysAdminuser user =  (JmhdSysAdminuser)request.getSession().getAttribute("data");
			String sql = "select r.role_id as roleid,r.role_name as rolename,rt.adminid as adminid,r.usid from " +
					"(select sr.role_id,sr.status,sr.role_name,x.admin_id as usid from jmhd_sys_role sr left join  " +
					"(select rr.role_id,a.admin_id from jmhd_related_admin_role rr left join jmhd_sys_adminuser a on rr.admin_id=a.admin_id where rr.admin_id= ? and rr.enabled=1) x on x.role_id=sr.role_id where sr.enabled=1) r left join " +
					"(select ur.admin_id as adminid,ur.role_id as rid from jmhd_sys_adminuser u left join jmhd_related_admin_role ur on u.admin_id=ur.admin_id where u.admin_id= ?) rt on r.role_id=rt.rid " +
					"where r.status='0' order by r.role_id ";
			List<Map<String, Object>>  list = serviceDao.queryForListSQL(sql, new Object[]{user.getAdminId(),adminuser.getAdminId()});
			map.put("adminuser", adminuser);
			map.put("adminuserId", id);
			map.put("userRoles", list);
			HttpHelper.writeHdptJSON(map, response,true);
		}catch (Exception e) {
			HttpHelper.writeHdptJSON(null, response,false);
		}
	}
	
	
	@RequestMapping(value = "/setAdminUserRole")
	public void setAdminUserRole(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "adminId", required = true) Integer id,
			@RequestParam(value = "roleString", required = false) String roleString)
			throws IOException {
		
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			JmhdSysAdminuser adminuser = serviceDao.queryT("JmhdSysAdminuser", "adminId=?",  new Object[]{id});
			if(adminuser==null|| "admin".equals(adminuser.getLoginName())){
				map.put("message", "此账号不能设置!");
			}else{
				serviceDao.delete("JmhdRelatedAdminRole","adminId=?", new Object[]{id});
				if (roleString != null && !"".equals(roleString)) {
					String[] roleIdArray = roleString.split(",");
					for (String roleId : roleIdArray) {
						JmhdRelatedAdminRole adminRole = new JmhdRelatedAdminRole();
						adminRole.setAdminId(id);
						adminRole.setRoleId(Integer.parseInt(roleId.trim()));
						serviceDao.save("JmhdRelatedAdminRole", adminRole);
					}
				}
				map.put("message", "设置成功");
			}
			
		}catch (Exception e) {
			map.put("message", "设置失败");
		}
		HttpHelper.writeHdptJSON(map, response,true);
	}
	
	
	@RequestMapping(value = "/adminUserAdd")
	public void adminUserAdd(HttpServletRequest request,HttpServletResponse response,
			@ModelAttribute("info") JmhdSysAdminuser info){
		
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			JmhdSysAdminuser user = serviceDao.queryT("JmhdSysAdminuser", "loginName=? and type=?", new Object[]{info.getLoginName(),"0"});
			if(user != null){
				map.put("success", 2);
				map.put("message", "用户名已经存在");
			}else{
				if(info.getLoginName() != null && !"".equals(info.getLoginName())){
					info.setPassword(MD5StringUtil.MD5Encode(info.getPassword()));
					serviceDao.save("JmhdSysAdminuser", info);
					map.put("success", 1);
					map.put("message", "添加成功");
				}else{
					map.put("success", -1);
					map.put("message", "添加失败");
				}
			}
		}catch(Exception e){
			map.put("success", -1);
			map.put("message", "添加失败");
		}
		HttpHelper.writeHdptJSON(map, response,true);
	}
	
	@RequestMapping(value = "/adminUserUpdate")
	public void adminUserUpdate(HttpServletRequest request,HttpServletResponse response,
			@ModelAttribute("info") JmhdSysAdminuser info){
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			JmhdSysAdminuser adminuser = serviceDao.queryT("JmhdSysAdminuser", "adminId=?", new Object[]{info.getAdminId()});
			if(adminuser != null){
				adminuser.setAdminName(info.getAdminName());
				adminuser.setOrgId(info.getOrgId());
				adminuser.setDatafield(info.getDatafield());
				adminuser.setStatus(info.getStatus());
				adminuser.setRemarks(info.getRemarks());
				serviceDao.update("JmhdSysAdminuser", adminuser);
				map.put("success", 1);
				map.put("message", "更新成功");
			}else{
				map.put("success", -1);
				map.put("message", "不存在用户");
			}
			
		}catch(Exception e){
			map.put("success", -1);
			map.put("message", "更新失败");
		}
		HttpHelper.writeHdptJSON(map, response,true);
	}
	
	@RequestMapping(value = "/adminUserDel")
	public void roleDel(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "id", required = true) Integer id,
			@RequestParam(value = "loginName", required = true) String loginName){
		HashMap<String,Object> map = new HashMap<String,Object>();
		
		if ("admin".equals("loginName")) {
			map.put("success", false);
			map.put("message", "删除失败,管理员账户无法删除!");
		}
		
		if(service.delete(id)){
			map.put("success", true);
			map.put("message", "删除成功!");
		}else{
			map.put("success", false);
			map.put("message", "删除失败!");
		}
		HttpHelper.writeHdptJSON(map, response,true);
	}


	@RequestMapping(value = "/adminUserPswReset")
	public void roleDel(HttpServletRequest request,HttpServletResponse response,
						@RequestParam(value = "id", required = true) Integer id){
		HashMap<String,Object> map = new HashMap<String,Object>();

		JmhdSysAdminuser adminuser = serviceDao.queryT("JmhdSysAdminuser", "adminId=?", new Object[]{id});
		if(adminuser != null){
			adminuser.setPassword(MD5StringUtil.MD5Encode("123456"));
			serviceDao.update("JmhdSysAdminuser", adminuser);
			map.put("success", true);
			map.put("message", "重置成功!密码为123456");
		}else{
			map.put("success", false);
			map.put("message", "重置失败!");
		}
		HttpHelper.writeHdptJSON(map, response,true);
	}
	
	
	@RequestMapping(value = "/getAdminUserDataField")
	public void getAdminUserDataField(HttpServletRequest request,HttpServletResponse response)
			throws IOException {
		try{
			Map<String,Object> map = new HashMap<String,Object>();
			JmhdSysAdminuser user = (JmhdSysAdminuser)request.getSession().getAttribute("data");
			map.put("datafield", user.getDatafield());
			HttpHelper.writeHdptJSON(map, response,true);
		}catch (Exception e) {
			HttpHelper.writeHdptJSON(null, response,false);
		}
	}

	
}
