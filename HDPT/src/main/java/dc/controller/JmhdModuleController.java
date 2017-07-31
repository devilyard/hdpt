package dc.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bsoft.xds.support.dao.IDAO;

import dc.domain.main.JmhdSysModule;
import dc.util.HttpHelper;
import dc.util.Util;

/**
 * @author Administrator
 *
 */
@Controller
public class JmhdModuleController {
	@Autowired
	private IDAO serviceDao;
	
	@RequestMapping(value = "/moduleTree")
	public void moduleTree(HttpServletRequest request,HttpServletResponse response)
			throws IOException {
		try{
			List<JmhdSysModule> jmhdSysModules = serviceDao.queryList("JmhdSysModule", "1=1 order by id",  null);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("modules", jmhdSysModules);
			map.put("success", true);
			HttpHelper.writeHdptJSON(map, response,true);
		}catch (Exception e) {
			HttpHelper.writeHdptJSON(null, response,false);
		}
	}
	
	@RequestMapping(value = "/moduletoUpdate")
	public void moduletoUpdate(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "id", required = true) Integer id)
			throws IOException {
		try{
			JmhdSysModule jmhdSysModule = serviceDao.queryT("JmhdSysModule", "id=?",  new Object[]{id});
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("module", jmhdSysModule);
			HttpHelper.writeHdptJSON(map, response,true);
		}catch (Exception e) {
			HttpHelper.writeHdptJSON(null, response,false);
		}
	}
	
	
	
	@RequestMapping(value = "/moduleUpdate")
	public void moduleUpdate(HttpServletRequest request,HttpServletResponse response,
			@ModelAttribute("info") JmhdSysModule info){
		
		try{
			serviceDao.update("JmhdSysModule", info);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", true);
			HttpHelper.writeHdptJSON(map, response,true);
		}catch(Exception e){
			HttpHelper.writeHdptJSON(null, response,false);
		}
		
	}
	
	
	@RequestMapping(value = "/moduleAdd")
	public void moduleAdd(HttpServletRequest request,HttpServletResponse response,
			@ModelAttribute("info") JmhdSysModule info){
		
		try{
			List<Map<String, Object>> modules=null;
			String moduleCode = info.getModuleCode();
			if(Util.empty(info.getModuleCode())){
				moduleCode = "1";
			}
			if(info.getModuleLev()==1){
				modules = serviceDao.queryForListSQL("select module_code from JMHD_SYS_MODULE where module_code like '%'  and LENGTH(module_code)= ? group by module_code order by module_code", new Object[]{Integer.parseInt(info.getModuleLev().toString())*3});
			}else{
				modules = serviceDao.queryForListSQL("select module_code from JMHD_SYS_MODULE where module_code like '"+moduleCode+"%'  and LENGTH(module_code)= ? group by module_code order by module_code", new Object[]{Integer.parseInt(info.getModuleLev().toString())*3});
			}
			info.setModuleCode(getModuleCode(moduleCode, modules));
			info.setIsleaf("0");//默认显示
			serviceDao.save("JmhdSysModule", info);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", true);
			HttpHelper.writeHdptJSON(map, response,true);
		}catch(Exception e){
			HttpHelper.writeHdptJSON(null, response,false);
		}
		
	}
	
	
	@RequestMapping(value = "/moduleDel")
	public void moduleDel(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "moduleCode", required = true) String moduleCode)
			throws IOException {
		try{
			serviceDao.delete("JmhdSysModule", "moduleCode=?",  new Object[]{moduleCode});
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", true);
			HttpHelper.writeHdptJSON(map, response,true);
		}catch (Exception e) {
			HttpHelper.writeHdptJSON(null, response,false);
		}
	}
	
	
	private String getModuleCode(String moduleCode,List<Map<String, Object>> modules) {
		long start=0;
		if(Long.parseLong(moduleCode)==1){
			 start=Long.parseLong(moduleCode)*100+1;
		}else{
			 start=Long.parseLong(moduleCode)*1000+1;
		}
		String code="";
		for(int i=0;i<999;i++){
			if(modules.size()>i){
				if(Long.parseLong(String.valueOf(modules.get(i).get("MODULE_CODE")))==start){
					start++;
				}else{
					code=String.valueOf(start);
					continue;
				}
			}else{
				code=String.valueOf(start);
				continue;
			}
		}
	  return code;
	}
	
	
	@RequestMapping(value = "/getModule")
	public void getModule(HttpServletRequest request,HttpServletResponse response,@RequestParam(value = "moduleCode", required = true) String moduleCode)
			throws IOException {
		try{
			List<JmhdSysModule> jmhdSysModules = serviceDao.queryList("JmhdSysModule", "moduleCode like '"+moduleCode+"%' and LENGTH(moduleCode) > 3",  null);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("modules", jmhdSysModules);
			map.put("success", true);
			HttpHelper.writeHdptJSON(map, response,true);
		}catch (Exception e) {
			HttpHelper.writeHdptJSON(null, response,false);
		}
	}
	
}
