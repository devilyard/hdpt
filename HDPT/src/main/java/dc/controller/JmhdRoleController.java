package dc.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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

import dc.domain.role.JmhdSysRole;
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
public class JmhdRoleController {
	@Autowired
	private IDAO serviceDao;
	
	@Autowired
	@Qualifier("roleService")
	private IRoleService service;
	
	@RequestMapping(value = "/roleList")
	public void roleList(HttpServletRequest request,
			HttpServletResponse response,
			@ModelAttribute("info") JmhdSysRole info){
		
		 HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			
			String roleName = info.getRoleName();
			int page = info.getPage();
			
			StringBuffer sql = new StringBuffer(" enabled=1 ");
			List<Object> paramsArray = new ArrayList<Object>();
			
			if(!Util.empty(roleName)){
				sql = sql.append("and roleName like '%"+ roleName +"%' ");
			}
			
			Object[] obj = new Object[paramsArray.size()];
			obj = paramsArray.toArray(obj);
			
			int count = serviceDao.queryForCount("JmhdSysRole", sql.toString(), obj);
			int pagesize = 15;
			page = page==0 ? 1 : page;
		    PageBuilder pb = new PageBuilder(pagesize);
		    pb.items(count);
		    pb.page(page);
		    List<Map<String, Object>> list = serviceDao.queryForPage("JmhdSysRole", sql.toString(),(page-1) * pagesize,pagesize, obj);
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
	
	@RequestMapping(value = "/roletoUpdate")
	public void roletoUpdate(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "id", required = true) Integer id)
			throws IOException {
		try{
			JmhdSysRole sysRole = serviceDao.queryT("JmhdSysRole", "roleId=?",  new Object[]{id});
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", true);
			map.put("sysRole", sysRole);
			HttpHelper.writeHdptJSON(map, response,true);
		}catch (Exception e) {
			HttpHelper.writeHdptJSON(null, response,false);
		}
	}
	
	
	
	@RequestMapping(value = "/addOrUpdate")
	public void addOrUpdate(HttpServletRequest request,HttpServletResponse response,
			@ModelAttribute("info") JmhdSysRole info){
		
		Map<String,Object> map = new HashMap<String,Object>();
		if(info.getRoleId()==null){
			try{
				info.setCreatetime(new Date());
				serviceDao.save("JmhdSysRole", info);
				map.put("success", true);
				map.put("message", "添加成功!");
			}catch(Exception e){
				map.put("success", false);
				map.put("message", "添加失败!");
			}
		}else{
			try{
				serviceDao.update("JmhdSysRole", info);
				map.put("success", true);
				map.put("message", "更新成功!");
			}catch(Exception e){
				map.put("success", false);
				map.put("message", "更新失败!");
			}
		}
		
		HttpHelper.writeHdptJSON(map, response,true);
	}
	
	
	
	@RequestMapping(value = "/toRoleModule")
	public void toRoleModule(HttpServletRequest request,HttpServletResponse response,@RequestParam(value = "id", required = true) Integer id){
		try{
			List<Map<String,Object>> list = serviceDao.queryForListSQL("select t.id as mid,t.module_code as mcode,t.module_lev as mlev,t.module_name as mname,rt.rid from jmhd_sys_module t " +
					"left join (select rm.role_id as rid,rm.module_id as mid from jmhd_sys_role t left join jmhd_related_role_module rm on t.role_id=rm.role_id where t.role_id= ? ) rt on t.id=rt.mid  ",  new Object[]{id});
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("sysRoleId", id);
			map.put("roleModules", list);
			HttpHelper.writeHdptJSON(map, response,true);
		}catch (Exception e) {
			HttpHelper.writeHdptJSON(null, response,false);
		}
	}
	
	
	@RequestMapping(value = "/roleSetModule")
	public void roleSetModule(HttpServletRequest request,HttpServletResponse response,@RequestParam(value = "roleId", required = true) Integer roleId,
			@RequestParam(value = "moduleString", required = true) String moduleString){
		HashMap<String,Object> map = new HashMap<String,Object>();
		if(service.setModule(roleId, moduleString)){
			map.put("message", "设置成功");
		}else{
			map.put("message", "设置失败");
		}
		HttpHelper.writeHdptJSON(map, response,true);
	}
		
	
	
	@RequestMapping(value = "/roleDel")
	public void roleDel(HttpServletRequest request,HttpServletResponse response,@RequestParam(value = "id", required = true) Integer id){
		HashMap<String,Object> map = new HashMap<String,Object>();
		if(service.deleteByUpdate(id)){
			map.put("success", true);
			map.put("message", "删除成功!");
		}else{
			map.put("success", false);
			map.put("message", "删除失败!");
		}
		HttpHelper.writeHdptJSON(map, response,true);
	}
	
}
