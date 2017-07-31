package dc.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.wltea.expression.ExpressionEvaluator;
import org.wltea.expression.datameta.Variable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bsoft.xds.support.dao.IDAO;

import dc.domain.contact.JmhdUserContact;
import dc.domain.diary.JmhdLibIndicator;
import dc.domain.diary.JmhdRelatedDiaryIndicator;
import dc.domain.diary.JmhdRelatedDiaryIndicatorId;
import dc.domain.diary.JmhdUserDiary;
import dc.domain.diary.JmhdUserIndicator;
import dc.domain.diary.VjmhdDiaryIndicator;
import dc.domain.main.JmhdInfo;
import dc.domain.main.YyUserAccount;
import dc.util.HttpHelper;
import dc.util.PageBuilder;
import dc.util.PageModel;
import dc.util.Util;

@Controller
public class DiaryController{
	
	
	@Autowired
	private IDAO serviceDao;
	

    private static final Logger LOGGER = LoggerFactory.getLogger(DiaryController.class);
	
	
    /**
     * 新增日志前查询出指标信息
     * @param request
     * @param response
     */
	@RequestMapping(value = "/diary/createView")
	public void createView(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			HashMap<String,Object> map = new HashMap<String,Object>();
			String manadoctorid = (String) request.getSession().getAttribute("manadoctorid");
			List<JmhdLibIndicator> list = serviceDao.queryList("JmhdLibIndicator", "status=?", new Object[]{"0"});
			map.put("manadoctorid", manadoctorid);
			map.put("list", list);
			HttpHelper.writeHdptJSON(map, response,true);
		} catch (Exception e) {
			LOGGER.error("createView error"+e);
			HttpHelper.writeHdptJSON(null, response,false);
		}

	}
	
	
	/**
	 * 新增日志
	 * @param request
	 * @param response
	 * @param info
	 */
	@RequestMapping(value = "/diary/create")
	public void create(HttpServletRequest request,
			HttpServletResponse response,
			@ModelAttribute("info") JmhdInfo info){
		
			HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			
			String manadoctorid = (String) request.getSession().getAttribute("manadoctorid");
			
			String mpiId = (String) request.getSession().getAttribute("mpiId");
			String name = (String) request.getSession().getAttribute("name");
			YyUserAccount account = (YyUserAccount) request.getSession().getAttribute("map");
			
			String isend = info.getIsend();
			JmhdUserDiary diary  = new JmhdUserDiary();
			 
			diary.setUserId(account.getUserid());
			diary.setMpiId(mpiId);
			diary.setUsername(name);
			
		    diary.setCreateTime(new Date());
		    diary.setIp("");
		    diary.setReplyNum(Integer.valueOf(0));
		    diary.setSource("");
		    diary.setTitle(info.getDiary().getTitle());
		    diary.setMessage(info.getDiary().getMessage());
		    
		    List<JmhdUserIndicator> indicators = info.getIndicators();
		    JSONObject indicatorJson = null;
		    
		    String uuid = Util.getUuid();
		    
		    if (indicators != null && indicators.size()!=0) {
		    	for(int i=0; i<indicators.size(); i++){
		    		indicators.get(i).setUserId(account.getUserid());
		    	}
		        indicatorJson = healthIndicatorCreate(indicators, diary, uuid, name, mpiId,manadoctorid, isend);
		    }
		    
		    if(indicatorJson != null){
		    	diary.setCacheJson(indicatorJson.toString());
		    }

		    serviceDao.save("JmhdUserDiary", diary);
		    
		    JmhdRelatedDiaryIndicator record = new JmhdRelatedDiaryIndicator();
		    JmhdRelatedDiaryIndicatorId id = new JmhdRelatedDiaryIndicatorId();
		    id.setDiaryId(diary.getId());
		    id.setIndicatorId(uuid);
		    record.setId(id);
		    
		    serviceDao.save("JmhdRelatedDiaryIndicator", record);
		    
		    map.put("diary", diary);
		    
		    map.put("message", "用户日志保存成功！");
			
			HttpHelper.writeHdptJSON(map, response,true);
			
		} catch (Exception e) {
			LOGGER.error("create error"+e);
			map.put("success", Boolean.valueOf(false));
			map.put("message", "用户日志保存失败");
			HttpHelper.writeHdptJSON(map, response,false);
		}

	}
	
	
	/**
	 * 更新日志
	 * @param request
	 * @param response
	 * @param info
	 */
	@RequestMapping(value = "/diary/update")
	public void update(HttpServletRequest request,
			HttpServletResponse response,@ModelAttribute("info") JmhdInfo info){
		
			HashMap<String,Object> map = new HashMap<String,Object>();
		try{
			
			String mpiId = (String) request.getSession().getAttribute("mpiId");
			String name = (String) request.getSession().getAttribute("name");
			String manadoctorid = (String) request.getSession().getAttribute("manadoctorid");
			YyUserAccount account = (YyUserAccount) request.getSession().getAttribute("map");
			
			JmhdUserDiary diary = info.getDiary();
			diary = serviceDao.queryT("JmhdUserDiary", "id=?", new Object[]{diary.getId()});
			diary.setMpiId(mpiId);
			diary.setUsername(name);
			
			diary.setUserId(account.getUserid());
		    diary.setCreateTime(new Date());
		    diary.setIp("");
		    diary.setReplyNum(Integer.valueOf(0));
		    diary.setSource("");
		    diary.setTitle(info.getDiary().getTitle());
		    diary.setMessage(info.getDiary().getMessage());
			
			String uuid = "";
			List<Map<String, Object>> relatedDiaryIndicators = serviceDao.queryForListSQL("select indicator_id from jmhd_related_diary_indicator where diary_id=?", 
					new Object[]{diary.getId()});
			
			if (relatedDiaryIndicators.size() > 0)
				uuid = (String)relatedDiaryIndicators.get(0).get("INDICATOR_ID");
			else {
				uuid = Util.getUuid();
			}
			
			List<JmhdUserIndicator> indicators = info.getIndicators();
		    JSONObject indicatorJson = null;
			if (indicators != null  && indicators.size()!=0) {
		    	for(int i=0; i<indicators.size(); i++){
		    		indicators.get(i).setUserId(account.getUserid());
		    	}
				indicatorJson = healthIndicatorCreate(indicators, diary, uuid, name, mpiId,manadoctorid, info.getIsend());
			}
			if (indicatorJson != null) {
				String cacheOldStr = diary.getCacheJson();
			      if (!Util.empty(cacheOldStr)) {
			        JSONObject cacheOldJson = JSON.parseObject(cacheOldStr);
			        JSONArray cacheNewArr = cacheOldJson.getJSONArray("indicator");
			        JSONArray cacheJsonArray = indicatorJson.getJSONArray("indicator");
			        cacheJsonArray.addAll(cacheNewArr);
			      }
				
				diary.setCacheJson(indicatorJson.toString());
			}
			
			serviceDao.update("JmhdUserDiary", diary);
			
			if (relatedDiaryIndicators.size() == 0) {
				JmhdRelatedDiaryIndicator record = new JmhdRelatedDiaryIndicator();
				JmhdRelatedDiaryIndicatorId id = new JmhdRelatedDiaryIndicatorId();
				id.setDiaryId(diary.getId());
				id.setIndicatorId(uuid);
				record.setId(id);
				serviceDao.save("JmhdRelatedDiaryIndicator", record);
			}
			map.put("diary", diary);
			map.put("message","用户日志保存成功！");
			map.put("success", true);
			HttpHelper.writeHdptJSON(map, response,true);
			
		}catch (Exception e) {
			LOGGER.error("update error"+e);
			map.put("success", Boolean.valueOf(false));
			map.put("message", "用户日志保存失败");
			HttpHelper.writeHdptJSON(map, response,false);
		}
	}
	
	
	
	
	/**
	 * 更新日志的指标
	 * @param request
	 * @param response
	 * @param id
	 */
	@RequestMapping(value = "/diary/updateView")
	public void updateView(HttpServletRequest request,
			HttpServletResponse response,@RequestParam(value="id") Long id){
		
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			List<JmhdLibIndicator> list = serviceDao.queryList("JmhdLibIndicator", "status=?", new Object[]{"0"});
			
			JmhdUserDiary diary = serviceDao.queryT("JmhdUserDiary", "id=?", new Object[]{id});
			
			List<VjmhdDiaryIndicator> diaryIndicatorList = serviceDao.queryList("VjmhdDiaryIndicator", "diaryId=?",new Object[]{id});
			
			map.put("diary", diary);
			map.put("indicatorLibs", list);
			map.put("indicators",diaryIndicatorList);
			map.put("success", true);
			HttpHelper.writeHdptJSON(map, response,true);
		} catch (Exception e) {
			LOGGER.error("updateView error"+e);
			map.put("success", false);
			HttpHelper.writeHdptJSON(map, response,false);
		}
	}

	
	/**
	 * 查询日志列表
	 * @param request
	 * @param response
	 * @param info
	 */
	@RequestMapping(value = "/diary/pagelist")
	public void pagelist(HttpServletRequest request,
			HttpServletResponse response,
			@ModelAttribute("info") JmhdInfo info) {
		
		 HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			
			String mpiId = (String) request.getSession().getAttribute("mpiId");
			YyUserAccount account = (YyUserAccount) request.getSession().getAttribute("map");
			
			String title = info.getTitle();
			String begindate = info.getBeginDate();
			String enddate = info.getEndDate();
			int page = info.getPage();
			StringBuffer sql = new StringBuffer("");
			
			if(mpiId != null){
				sql = sql.append("mpiid = '"+mpiId+"' ");
			}else{
				sql = sql.append("user_id = "+account.getUserid()+" ");
			}
			
			if(!Util.empty(title)){
				sql = sql.append("and title like '%"+ title +"%' ");
			}
			
			if(!Util.empty(begindate)){
				sql = sql.append("and create_time >= to_date('"+begindate+"','yyyy-MM-dd') ");
			}

			if(!Util.empty(enddate)){
				sql = sql.append("and create_time <= to_date('"+enddate+" 23:59:59','yyyy-MM-dd HH24:mi:ss')");
			}
			
			sql.append(" order by create_time desc ");
			
			List<JmhdUserDiary> diaryList = serviceDao.queryList("JmhdUserDiary", sql.toString(), null);
			int pagesize = 15;
			page = page==0 ? 1 : page;
		    PageBuilder pb = new PageBuilder(pagesize);
		    pb.items(diaryList.size());
		    pb.page(page);
		    Object[] params = null;
		    List<Map<String, Object>> list = serviceDao.queryForPage("JmhdUserDiary", sql.toString(),(page-1) * pagesize,pagesize, params);
		    map.put("success", Boolean.valueOf(true));
		    map.put("list", list);
		    map.put("pb", new PageModel(pb));
		    HttpHelper.writeHdptJSON(map, response,true);
		} catch (Exception e) {
			LOGGER.error("pagelist error"+e);
			map.put("success", false);
			map.put("message", "查询失败");
			HttpHelper.writeHdptJSON(map, response,false);
		}
		
	}
	

	public JSONObject healthIndicatorCreate(List<JmhdUserIndicator> indicatorList, JmhdUserDiary diary,String uuid, String name,String mpiId,String manadoctorid, String isend)
	  {
		JSONObject json = null;
		JSONArray cacheJsonArray = new JSONArray();

	    Map<Integer, JmhdLibIndicator> libMap = libIndicatorMap();

	    Map<Integer, JmhdUserIndicator> inValMap = new HashMap<Integer, JmhdUserIndicator>();
	    JmhdUserContact userContact = new JmhdUserContact();
	    String contString = "</br><font style='font-weight:bold'>居民" + name + "提交了份健康日志</font></br><font style='font-weight:bold'>各项指标如下</font>:</br>";
	    String newuuid1 = Util.getUuid();
	    String newuuid4 = Util.getUuid();
	    String newuuid5 = Util.getUuid();
	    String newuuid6 = Util.getUuid();
	    String newuuid9 = Util.getUuid();
	    for (JmhdUserIndicator indicator : indicatorList) {

	      if ((indicator != null) && (!Util.empty(indicator.getVal())))
	      {
	        JmhdLibIndicator libIndicator = (JmhdLibIndicator)libMap.get(indicator.getIndicatorId());

	        //System.out.println(indicator.getType());
	        if("1".equals(indicator.getType())){
	        	indicator.setNewuuid(newuuid1);
	        }else if("4".equals(indicator.getType())){
	        	indicator.setNewuuid(newuuid4);
	        }else if("5".equals(indicator.getType())){
	        	indicator.setNewuuid(newuuid5);
	        }else if("6".equals(indicator.getType())){
	        	indicator.setNewuuid(newuuid6);
	        }else if("9".equals(indicator.getType())){
	        	indicator.setNewuuid(newuuid9);
	        }
	        inValMap.put(indicator.getIndicatorId(), indicator);
	        indicator.setMpiId(mpiId);
	        indicator.setUuid(uuid);
	        indicator.setCreateDate(diary.getCreateTime());
	        indicator.setStatus("0");
	        indicator.setSource("居民互动平台");
	        indicator.setIndicatorName(libIndicator.getName());
	        JSONObject cacheIndicatorJson = new JSONObject();

	        if ("1".equals(libIndicator.getAssemble())) {
	        	

	          List<Variable> variables = new ArrayList<Variable>();
	          String[] itemIdxs = indicator.getVal().split(",");
	          for (String idx : itemIdxs) {
	            JmhdUserIndicator itemObj = (JmhdUserIndicator)indicatorList.get(Integer.parseInt(idx));

	            if ((Util.empty(itemObj)) || (Util.empty(itemObj.getVal())))
	            {
	              break;
	            }
	            String paramName = "A" + itemObj.getIndicatorId();
	            variables.add(Variable.createVariable(paramName, itemObj.getVal()));
	          }
	          
	          String val = (String)ExpressionEvaluator.evaluate(libIndicator.getFormula(),variables);
	          

	          cacheIndicatorJson.put("name", indicator.getIndicatorName());
	          cacheIndicatorJson.put("val", val);
	          cacheIndicatorJson.put("units", libIndicator.getUnits());
	          contString = contString + indicator.getIndicatorName() + ":" + val + "(" + libIndicator.getUnits() + ")</br>";
	        }
	        else if ("0".equals(indicator.getIsFactor())) {
	          cacheIndicatorJson.put("name", indicator.getIndicatorName());
	          cacheIndicatorJson.put("val", indicator.getVal());
	          cacheIndicatorJson.put("units", libIndicator.getUnits());
	          contString = contString + indicator.getIndicatorName() + ":" + indicator.getVal() + "(" + libIndicator.getUnits() + ")</br>";
	        }
	       
	        serviceDao.save("JmhdUserIndicator", indicator);
	        
	        if (("1".equals(libIndicator.getAssemble())) || ("0".equals(indicator.getIsFactor()))) {
	          cacheIndicatorJson.put("id", indicator.getId());
	          cacheJsonArray.add(cacheIndicatorJson);
	        }
	      }
	    }
	    if (cacheJsonArray.size() > 0) {
	      json = new  JSONObject();
	      json.put("indicator", cacheJsonArray);
	    }
        
	    //是否发送给责任医生
	    if (!Util.empty(isend)) {
	      contString = contString + "<font style='font-weight:bold'>健康内容如下</font>:</br>" + diary.getMessage();
	      userContact.setDoctorId(manadoctorid);
	      userContact.setMpiid(mpiId);
	      userContact.setMessage(contString);
	      userContact.setReplyStatus("0");
	      userContact.setAtime(new Date());
	      userContact.setQtime(userContact.getAtime());
	      userContact.setDelStatus("0");
	      userContact.setEndStatus("0");
	      serviceDao.save("JmhdUserContact",userContact);
	    }
	    return json;
	  }
	
	public Map<Integer, JmhdLibIndicator> libIndicatorMap() {
		
	    Map<Integer, JmhdLibIndicator> libMap = new HashMap<Integer, JmhdLibIndicator>();

		List<JmhdLibIndicator> list = serviceDao.queryList("JmhdLibIndicator", "status=?", new Object[]{"0"});

	    for (JmhdLibIndicator JmhdLibIndicator : list) {
	      libMap.put(JmhdLibIndicator.getId(), JmhdLibIndicator);
	    }
	    return libMap;
	  }

	
	
	/**
	 * 删除日志
	 * @param request
	 * @param response
	 * @param id
	 */
	@RequestMapping(value = "/diary/delete")
	public void delete(HttpServletRequest request,
			HttpServletResponse response,@RequestParam(value="id") Long id) {
			
			HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			List<VjmhdDiaryIndicator> vDIList = serviceDao.queryList("VjmhdDiaryIndicator", "diaryId=?",new Object[]{id});
			
		    for (VjmhdDiaryIndicator VjmhdDiaryIndicator : vDIList) {
		      serviceDao.delete("JmhdUserIndicator", "uuid=?", new String[]{VjmhdDiaryIndicator.getUuid()});
		    }

		    int row = serviceDao.delete("JmhdUserDiary", "id=?", new Object[]{id});
		    if (row > 0) {
		    	map.put("success", Boolean.valueOf(true));
		    	map.put("message", "删除成功!");
		    }
		    HttpHelper.writeHdptJSON(map, response,true);
			
		} catch (Exception e) {
			LOGGER.error("delete error"+e);
			map.put("success", Boolean.valueOf(false));
			map.put("message", "删除失败");
			HttpHelper.writeHdptJSON(map, response,false);
		}
	}
	
	
	
	/**
	 * 删除日志的指标
	 * @param request
	 * @param response
	 * @param id
	 */
	@RequestMapping(value = "/diary/deleteIndicator")
	public void deleteIndicator(HttpServletRequest request,
			HttpServletResponse response,@RequestParam(value="id") Long id) {
		
			HashMap<String,Object> map = new HashMap<String,Object>();
		try {
		
			List<VjmhdDiaryIndicator> vDIList = serviceDao.queryList("VjmhdDiaryIndicator", "id=?",new Object[]{id});
		    if ((vDIList == null) || (vDIList.size() == 0)) {
		    	map.put("success", Boolean.valueOf(false));
				map.put("message", "参数异常");
				HttpHelper.writeHdptJSON(map, response,false);
				return ;
		    }
		    //serviceDao.update("JmhdUserIndicator", "set status=?", "id=?", new Object[]{"1",id});
		    for (VjmhdDiaryIndicator VjmhdDiaryIndicator : vDIList)
		    {
		      serviceDao.update("JmhdUserIndicator", "set status=?", "newuuid=?", new Object[]{"1",VjmhdDiaryIndicator.getNewuuid()});

		      if (!Util.empty(VjmhdDiaryIndicator.getDiaryId())) {
		    	JmhdUserDiary diary = serviceDao.queryT("JmhdUserDiary", "id=?", new Object[]{VjmhdDiaryIndicator.getDiaryId()});
		        String cacheStr = diary.getCacheJson();
		        if (!Util.empty(cacheStr)) {
		          JSONObject cache = JSON.parseObject(cacheStr);
		          JSONArray cacheIndicators = cache.getJSONArray("indicator");
		          if (cacheIndicators.size() > 0) {
		            for (int i = 0; i < cacheIndicators.size(); i++) {
		              JSONObject item = cacheIndicators.getJSONObject(i);
		              if (item.getLong("id").equals(VjmhdDiaryIndicator.getId())) {
		                cacheIndicators.remove(i);
		              }
		            }
		            diary.setCacheJson(cache.toString());
		            serviceDao.update("JmhdUserDiary", diary);
		          }
		        }
		      }
		    }

		    map.put("success", Boolean.valueOf(true));
		    map.put("message", "用户日志删除成功！");
			
		    HttpHelper.writeHdptJSON(map, response,true);
			
		} catch (Exception e) {
			LOGGER.error("deleteIndicator error"+e);
			map.put("success", Boolean.valueOf(false));
			map.put("message", "用户日志删除失败");
			HttpHelper.writeHdptJSON(map, response,false);
		}
		
		
	}
}
