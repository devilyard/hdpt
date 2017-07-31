package dc.controller;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSON;
import com.bsoft.xds.support.dao.IDAO;

import dc.domain.contact.JmhdContactParam;
import dc.domain.contact.JmhdReplyParam;
import dc.domain.contact.JmhdUserContact;
import dc.domain.contact.JmhdUserContactView;
import dc.domain.contact.JmhdUserRecontact;
import dc.domain.contact.JmhdUserRecontactView;
import dc.domain.main.JmhdAdminWork;
import dc.domain.main.JmhdInfo;
import dc.domain.main.YyUserAccount;
import dc.util.HttpHelper;
import dc.util.PageBuilder;
import dc.util.PageCount;
import dc.util.PageModel;
import dc.util.Util;

@Controller
public class JmhdContactController {
	
	
	@Autowired
	private IDAO serviceDao;
	
	
	
	/**
	 * 咨询基本信息
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/jmhdcontact/creatView")
	public void creatView(HttpServletRequest request, HttpServletResponse response){
		try {
			String manadoctorname = (String) request.getSession().getAttribute("manadoctorname");
			String name = (String) request.getSession().getAttribute("name");
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("name", name);
			map.put("manadoctorname", manadoctorname);
			HttpHelper.writeHdptJSON(map, response,true);
		} catch (Exception e) {
			HttpHelper.writeHdptJSON(null, response,false);
		}

	}
	
	
	/**
	 * 新增咨询提问
	 * @param request
	 * @param response
	 * @param userContact
	 */
	@RequestMapping(value = "/jmhdcontact/askContact")
	public void askContact(HttpServletRequest request,
			HttpServletResponse response,
			@ModelAttribute("userContact") JmhdUserContact userContact){
		
			HashMap<String,Object> map = new HashMap<String,Object>();
		try{
			String manadoctorid = (String) request.getSession().getAttribute("manadoctorid");
			String mpiId = (String) request.getSession().getAttribute("mpiId");
			String empiId = (String) request.getSession().getAttribute("empiId");
			YyUserAccount account = (YyUserAccount) request.getSession().getAttribute("map");
			userContact.setUserId(account.getUserid());
			userContact.setDoctorId(manadoctorid);
			userContact.setEmpiid(empiId);
			userContact.setMpiid(mpiId);
		    userContact.setReplyStatus("0");
		    userContact.setAtime(new Date());
		    userContact.setQtime(userContact.getAtime());
		    userContact.setDelStatus("0");
		    userContact.setEndStatus("0");
		    
		    serviceDao.save("JmhdUserContact", userContact);
		    
		    if(userContact.getId() != null){
		    	 JmhdAdminWork adminWork = new JmhdAdminWork();
		         adminWork.setDoctorId(userContact.getDoctorId());
		         adminWork.setStatus("0");
		         adminWork.setTaskid(userContact.getId().toString());
		         adminWork.setWtype("contact");
		         adminWork.setWtypename("医患互动");
		         serviceDao.save("JmhdAdminWork", adminWork);
		    }
		    
		    map.put("success", Boolean.valueOf(true));
		    map.put("message", "提交成功!");
			HttpHelper.writeHdptJSON(map, response,true);
		} catch (Exception e) {
			map.put("success", Boolean.valueOf(false));
		    map.put("message", "提交失败请重新提交!");
			HttpHelper.writeHdptJSON(null, response,false);
		}
	}
	
	
	/**
	 * 查询咨询列表
	 * @param request
	 * @param response
	 * @param info
	 */
	@RequestMapping(value = "/jmhdcontact/listContact")
	public void listContact(HttpServletRequest request,
			HttpServletResponse response,
			@ModelAttribute("info") JmhdInfo info){
		try {
			HashMap<String,Object> map = new HashMap<String,Object>();
			String mpiId = (String) request.getSession().getAttribute("mpiId");
			String begindate = info.getBeginDate();
			String enddate = info.getEndDate();
			int page = info.getPage();
			YyUserAccount account = (YyUserAccount) request.getSession().getAttribute("map");
			StringBuffer sql = new StringBuffer("");
			if(mpiId != null){
				sql = sql.append("delStatus = 0 and mpiid = '"+mpiId+"' ");
			}else{
				sql = sql.append("delStatus = 0 and user_id = "+account.getUserid()+" ");
			}
			
			if(!Util.empty(begindate)){
				sql = sql.append("and qtime >= to_date('"+begindate+"','yyyy-MM-dd') ");
			}

			if(!Util.empty(enddate)){
				sql = sql.append("and qtime <= to_date('"+enddate+" 23:59:59','yyyy-MM-dd HH24:mi:ss') ");
			}
			
			sql = sql.append("order by qtime desc ");
			
			List<JmhdUserContact> diaryList = serviceDao.queryList("JmhdUserContact", sql.toString(), null);
			int pagesize = 15;
			page = page==0 ? 1 : page;
		    PageBuilder pb = new PageBuilder(pagesize);
		    pb.items(diaryList.size());
		    pb.page(page);
		    Object[] params = null;
		    List<Map<String, Object>> list = serviceDao.queryForPage("JmhdUserContact", sql.toString(),(page-1) * pagesize,pagesize, params);
		    map.put("success", Boolean.valueOf(true));
		    map.put("list", list);
		    map.put("pb", new PageModel(pb));
			
			HttpHelper.writeHdptJSON(map, response,true);
		} catch (Exception e) {
			HttpHelper.writeHdptJSON(null, response,false);
		}

	}
	
	/**
	 * 查看咨询回复
	 * @param request
	 * @param response
	 * @param id
	 */
	@RequestMapping(value = "/jmhdcontact/replyView")
	public void replyView(HttpServletRequest request, HttpServletResponse response,
			@RequestParam (value="id") Long id){
		try {

			HashMap<String,Object> map = new HashMap<String,Object>();
//			String name = (String) request.getSession().getAttribute("name");
//			String mpiId = (String) request.getSession().getAttribute("mpiId");
			
			JmhdUserContactView contact = serviceDao.queryT("JmhdUserContactView", "id=? and delStatus=0", new Object[]{id});
			// 改为按问题编号取出所有回答 start
			List<JmhdUserRecontactView> recontacts = serviceDao.queryList("JmhdUserRecontactView", "contactId=? and delStatus=0", new Object[]{id});
//			List<JmhdUserRecontactView> recontacts = serviceDao.queryList("JmhdUserRecontact", "contactId=? and delStatus=0", new Object[]{id});
			// 改为按问题编号取出所有回答 end 
			
			map.put("contact", contact);
			map.put("recontacts", recontacts);
//			map.put("name", name);
//			map.put("mpiid", mpiId);
			
			HttpHelper.writeHdptJSON(map, response,true);
			
		} catch (Exception e) {
			HttpHelper.writeHdptJSON(null, response,false);
		}
	}
	
	
	/**
	 * 新增咨询回复
	 * @param request
	 * @param response
	 * @param userRecontact
	 */
	@RequestMapping(value = "/jmhdcontact/reply")
	public void reply(HttpServletRequest request,
			HttpServletResponse response,
			@ModelAttribute("userRecontact") JmhdUserRecontact userRecontact){
		
			HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			
			String mpiId = (String) request.getSession().getAttribute("mpiId");
			String empiId = (String) request.getSession().getAttribute("empiId");
			String name = (String) request.getSession().getAttribute("name");
			if(userRecontact.getDoctorId() == null){
			    userRecontact.setEmpiid(empiId);
			    userRecontact.setMpiid(mpiId);
			}
		    userRecontact.setDelStatus("0");
		    userRecontact.setCreateTime(new Date());
		    // 设置登录用户的用户ID
		    YyUserAccount account = (YyUserAccount) request.getSession().getAttribute("map");	
		    userRecontact.setUserId(account.getUserid());
		    if(name==null){
		    	name = account.getUsername();
		    }
		    serviceDao.save("JmhdUserRecontact", userRecontact);
		    if(userRecontact.getId() != null){
		    	if(Util.empty(userRecontact.getDoctorId())){
			    	JmhdUserContact contact = serviceDao.queryT("JmhdUserContact", "id=? and delStatus=0", new Object[]{userRecontact.getContactId()});
			    	contact.setReplyStatus("0");
			        contact.setAtime(userRecontact.getCreateTime());
			        serviceDao.update("JmhdUserContact", contact);
			        map.put("recontact", userRecontact);
				    map.put("success", Boolean.valueOf(true));
		    	}else{
		    		JmhdUserContact contact = serviceDao.queryT("JmhdUserContact", "id=? and delStatus=0", new Object[]{userRecontact.getContactId()});
			    	contact.setReplyStatus("1");
			        contact.setAtime(userRecontact.getCreateTime());
			        serviceDao.update("JmhdUserContact", contact);
			        JmhdUserRecontactView recontact = serviceDao.queryT("JmhdUserRecontactView", "id=? ", new Object[]{userRecontact.getId()});
			        map.put("recontact", recontact);
				    map.put("success", Boolean.valueOf(true));
		    	}
			   
		    }else{
		        map.put("success", Boolean.valueOf(false));
		    }
		    map.put("name", name);
		    map.put("mpiid", mpiId);
		    HttpHelper.writeHdptJSON(map, response,true);
		} catch (Exception e) {
			map.put("success", Boolean.valueOf(false));
			HttpHelper.writeHdptJSON(null, response,false);
		}

	}
	
	
	/**
	 * 删除咨询主题
	 * @param request
	 * @param response
	 * @param id
	 */
	@RequestMapping(value = "/jmhdcontact/delete")
	public void delete(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam (value="id") Long id){
			
			HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			
			JmhdUserContact contact  = serviceDao.queryT("JmhdUserContact", "id=?", new Object[]{id});
			if (contact != null && contact.getId() != null){
				contact.setDelStatus("1");
				serviceDao.update("JmhdUserContact", contact);
				map.put("message", "删除成功!");
				//ehrViewService.update("JMHD_USER_INFOEDIT", "IDCARD='"+request.getSession().getAttribute("idcard")+"'", Params);
				
				JmhdAdminWork work  = serviceDao.queryT("JmhdAdminWork", " wtype='contact' and   taskid=?",  new Object[]{String.valueOf(id)});
				work.setStatus("1");
				serviceDao.update("JmhdAdminWork", work);
			}else{
				map.put("message", "此数据已被删除!");
			}
			HttpHelper.writeHdptJSON(map, response,true);
			
		} catch (Exception e) {
			map.put("message", "删除失败!");
			HttpHelper.writeHdptJSON(map, response,false);
		}

	}
	
	
	/**
	 * 删除回复
	 * @param request
	 * @param response
	 * @param id
	 */
	@RequestMapping(value = "/jmhdcontact/recontactDelete")
	public void recontactDelete(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam (value="id") Long id){
		
			HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			
			JmhdUserRecontact contact = serviceDao.queryT("JmhdUserRecontact", "id=?", new Object[]{id});
		    if (contact != null && contact.getId() != null) {
		    	
		    	List<JmhdUserRecontact> childrens = serviceDao.queryList("JmhdUserRecontact", "reId=?", new Object[]{id});
		    	if(!childrens.isEmpty() && childrens.size()>1){
		    		for(JmhdUserRecontact children : childrens){
		    			children.setDelStatus("1");
				    	serviceDao.update("JmhdUserRecontact", children);
		    		}
		    	}
		    	contact.setDelStatus("1");
		    	serviceDao.update("JmhdUserRecontact", contact);
		    	map.put("message", "删除成功!");
		    }
		    else {
		    	map.put("message", "无权删除!");
		    }
			HttpHelper.writeHdptJSON(map, response,true);
			
		} catch (Exception e) {
			map.put("message", "删除失败!");
			HttpHelper.writeHdptJSON(map, response,false);
		}

	}
	

	/**
	 * 咨询详细信息
	 * @param request
	 * @param response
	 * @param id
	 */
	@RequestMapping(value = "/jmhdcontact/updateView")
	public void updateView(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam (value="id") Long id){
		
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			
			String name = (String) request.getSession().getAttribute("name");
			String manadoctorname = (String) request.getSession().getAttribute("manadoctorname");
			
			JmhdUserContact contact = serviceDao.queryT("JmhdUserContact", "id=?", new Object[]{id});
			if (contact != null && contact.getId() != null && !"1".equals(contact.getDelStatus()) && "0".equals(contact.getReplyStatus())) {
				
				map.put("success", Boolean.valueOf(true));
				map.put("contact", contact);
			} else {
				map.put("success", Boolean.valueOf(false));
				map.put("message", "此数据已被其他人员更新!请刷新后再更新!");
			}

			map.put("manadoctorname", manadoctorname);
			map.put("name", name);
			HttpHelper.writeHdptJSON(map, response,true);
			
		} catch (Exception e) {
			map.put("message", "数据查询失败!");
			HttpHelper.writeHdptJSON(map, response,false);
		}
		
	}
	
	/**
	 * 更新回复
	 * @param request
	 * @param response
	 * @param userContact
	 */
	@RequestMapping(value = "/jmhdcontact/updateContact")
	public void updateContact(HttpServletRequest request,
			HttpServletResponse response,
			@ModelAttribute("userContact") JmhdUserContact userContact){
		
			HashMap<String,Object> map = new HashMap<String,Object>();
		try{
			JmhdUserContact contact = serviceDao.queryT("JmhdUserContact"," id=?",new Object[]{userContact.getId()});
			contact.setMessage(userContact.getMessage());
		    serviceDao.update("JmhdUserContact", contact);
		    map.put("success", Boolean.valueOf(true));
		    map.put("message", "更新成功!");
			HttpHelper.writeHdptJSON(map, response,true);
		} catch (Exception e) {
			map.put("success", Boolean.valueOf(false));
		    map.put("message", "更新失败!");
			HttpHelper.writeHdptJSON(null, response,false);
		}
	}

	/**
	 * 根据页码查询咨询列表
	 * @param request
	 * @param response
	 * @param pageNo
	 * @param pageSize
     * @param content
     */
	@RequestMapping(value="queryInteractiveList")
	public void contentList4Interface(HttpServletRequest request, HttpServletResponse response,
									  @RequestParam(value="pageNo") int pageNo,
									  @RequestParam(value="pageSize") int pageSize,
									  @RequestParam(value="content", required = false) int content){
		Map<String, Object> map = new HashMap<String, Object>();
		int code = 0;
		String msg = "success";

		try {
			String clazz = "JmhdUserContact";
			StringBuffer sql = new StringBuffer("delStatus = 0 order by qtime desc ");
			Object[] params = null;

			int total = serviceDao.queryForCount(clazz, sql.toString(), params);
			PageCount pc = new PageCount(total, pageSize, pageNo);

			List<Map<String, Object>> list = serviceDao.queryForPage(clazz, pc.getStartRow(), pc.getPerPage(), params);
			List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Map<String, Object> singleContact = null;
			for (int i = 0; i < list.size(); i++) {
				JmhdUserContact obj = (JmhdUserContact)list.get(i);

				Map<String, Object> member = serviceDao.queryForMap("JMHD_MEMBER", "mpiid=?", new Object[]{obj.getMpiid()});
				Object docID = member.get("MANADOCTORID");
				Object docName = member.get("MANADOCTOR_TEXT");

				singleContact = new HashMap<String, Object>();
				singleContact.put("questionId", obj.getId());
				singleContact.put("questionContent", obj.getMessage());
				singleContact.put("pictures", "");
				singleContact.put("userId", obj.getUserId());
				singleContact.put("questionTime", sdf.format(obj.getQtime()));
				singleContact.put("status", obj.getReplyStatus());

				List<Map<String, Object>> replyList = serviceDao.queryForList("JmhdUserRecontact", "contact_id = ? order by create_time", new Object[]{obj.getId()});
				List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();
				for (int j = 0; j < replyList.size(); j++) {
					JmhdUserRecontact recontact = (JmhdUserRecontact)replyList.get(j);
					Map<String, Object> tempMap = new HashMap<String, Object>();
					tempMap.put("doctorId", docID);
					tempMap.put("doctorName", docName);
					tempMap.put("replyDate", sdf.format(recontact.getCreateTime()));
					tempMap.put("replyContent", recontact.getRemessage());
					tempList.add(tempMap);
				}
				singleContact.put("replies", tempList);
				dataList.add(singleContact);
			}
			map.put("data", dataList);
		}catch (Exception e){
			code = 99999;
			msg = "Query error!-->"+e.getMessage();
		}
		map.put("code", code);
		map.put("msg", msg);
		HttpHelper.writeJSONOnly(map, response);
	}
	@RequestMapping(value="replyQuestion")
	public void reply4Interface(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> map = new HashMap<String, Object>();
		int code = 0;
		String msg = "success";

		try {
			Object jsonObj = request.getAttribute("data");
			JmhdContactParam param = JSON.parseObject(jsonObj.toString(), JmhdContactParam.class);

			long questionId = param.getQuestionId();
			JmhdUserContact contact = serviceDao.queryT("JmhdUserContact", "id=? and delStatus=0", new Object[]{questionId});
			for (JmhdReplyParam replyParam : param.getReplies()) {
				JmhdUserRecontact reply = new JmhdUserRecontact();
				reply.setUserId(contact.getUserId());
				reply.setEmpiid(contact.getEmpiid());
				reply.setDoctorId(replyParam.getDoctorId());
				reply.setRemessage(replyParam.getReplyContent());
				reply.setCreateTime(replyParam.getReplyDate());
				reply.setContactId(questionId);
				reply.setDelStatus("0");
				reply.setMpiid(contact.getMpiid());
				serviceDao.save("JmhdUserRecontact", reply);
				if(reply.getId() != null){
					if(!Util.empty(reply.getDoctorId())) {
						contact.setReplyStatus("1");
						contact.setAtime(reply.getCreateTime());
						serviceDao.update("JmhdUserContact", contact);
					}
				}
			}
		}catch (Exception e){
			code = 99999;
			msg = "Reply error!-->"+e.getMessage();
		}

		map.put("code", code);
		map.put("msg", msg);
		HttpHelper.writeJSONOnly(map, response);
	}
}
