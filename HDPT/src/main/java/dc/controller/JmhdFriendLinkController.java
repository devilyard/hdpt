package dc.controller;

import java.util.ArrayList;
import java.util.Date;
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

import dc.domain.friendlink.JmhdFriendLink;
import dc.util.HttpHelper;
import dc.util.PageBuilder;
import dc.util.PageModel;
import dc.util.Util;

@Controller
public class JmhdFriendLinkController{
	
	
	@Autowired
	private IDAO serviceDao;
	
	@RequestMapping(value = "/friendlinklist")
	public void wszwlist(HttpServletRequest request,
			HttpServletResponse response,
			@ModelAttribute("info") JmhdFriendLink info){
		
		 HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			
			String linkname = info.getLinkname();
			int page = info.getPage();
			
			StringBuffer sql = new StringBuffer("1=1 ");
			
			List<Object> paramsArray = new ArrayList<Object>();
			
			if(!Util.empty(linkname)){
				sql = sql.append("and linkname like '%"+ linkname +"%' ");
			}
			
			
			sql.append("order by createdate desc");
			
			Object[] obj = new Object[paramsArray.size()];
			obj = paramsArray.toArray(obj);
			
			int count = serviceDao.queryForCount("JmhdFriendLink", sql.toString(), obj);
			int pagesize = 15;
			page = page==0 ? 1 : page;
		    PageBuilder pb = new PageBuilder(pagesize);
		    pb.items(count);
		    pb.page(page);
		    List<Map<String, Object>> list = serviceDao.queryForPage("JmhdFriendLink", sql.toString(),(page-1) * pagesize,pagesize, obj);
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
	
	@RequestMapping(value = "/friendlinkQuerybyid")
	public void wszwQuerybyid(HttpServletRequest request, HttpServletResponse response, @RequestParam(value="id") Long id){
				
		HashMap<String,Object> map = new HashMap<String,Object>();
		try{
			List<JmhdFriendLink> list =serviceDao.queryList("JmhdFriendLink", "zwid="+id, null);
			map.put("sublist", list);			
			map.put("success", true);
		}catch (Exception e) {
			e.printStackTrace();
			map.put("false", true);
			map.put("message", "此数据已经不存在");
		}					
		HttpHelper.writeHdptJSON(map, response,true);
	}
	
	@RequestMapping(value = "/friendlinkAdd")
	public void wszwAdd(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("info") JmhdFriendLink info){
		
		HashMap<String,Object> map = new HashMap<String,Object>();
		try{
			info.setCreatedate(new Date());
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
	
	@RequestMapping(value = "/friendlinkFind")
	public void wszwFind(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam(value="id") Long id){
		
		JmhdFriendLink doc = serviceDao.queryT("JmhdFriendLink", "id=?", new Object[]{id});
		HashMap<String,Object> map = new HashMap<String,Object>();
		if(doc != null){
			map.put("friendlink", doc);
			map.put("success", true);
		}else{
			map.put("false", true);
			map.put("message", "此数据已经不存在");
		}
		HttpHelper.writeHdptJSON(map, response,true);
	}
	
	@RequestMapping(value = "/friendlinkUpdate")
	public void wszwUpdate(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("info") JmhdFriendLink info){
		HashMap<String,Object> map = new HashMap<String,Object>();
		try{
			info.setCreatedate(new Date());
			serviceDao.update("JmhdFriendLink", info);
			map.put("success", true);
			map.put("message", "更新成功!");
		}catch(Exception e){
			e.printStackTrace();
			map.put("success", false);
			map.put("message", "更新失败!");
		}
		HttpHelper.writeHdptJSON(map, response,true);
	}
	
	
	@RequestMapping(value = "/friendlinkDel")
	public void wszwDel(HttpServletRequest request, HttpServletResponse response, @RequestParam(value="id") Long id){
		HashMap<String,Object> map = new HashMap<String,Object>();
		try{
			serviceDao.delete("JmhdFriendLink", "id=? ", new Object[]{id});
			map.put("success", true);
			map.put("message", "删除成功!");
		}catch(Exception e){
			map.put("success", false);
			map.put("message", "删除失败!");
		}
		HttpHelper.writeHdptJSON(map, response,true);
	}
	
}
