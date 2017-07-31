package dc.controller;

import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.bsoft.xds.support.dao.IDAO;
import dc.domain.volunteer.JmhdVolunteerDonation;
import dc.util.HttpHelper;

@Controller
public class VolunteerDonationController{
	
	
	@Autowired
	private IDAO serviceDao;
	
	/**
	 * 检查是否已经献血
	 * @param request
	 * @param response
	 * @param certificateno
	 */
	@RequestMapping(value = "/volunteers/checkVolunteers")
	public void checkVolunteers(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value="certificateno") String certificateno){
		
			HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			List<JmhdVolunteerDonation> list = serviceDao.queryList("JmhdVolunteerDonation", "certificateno=?", new String[]{certificateno});
			if (list.size() < 1)
				map.put("success", Boolean.valueOf(true));
			else {
				map.put("success", Boolean.valueOf(false));
			}
			HttpHelper.writeHdptJSON(map, response,true);
		} catch (Exception e) {
			HttpHelper.writeHdptJSON(map, response,false);
		}

	}
	
	
	/**
	 * 新增献血
	 * @param request
	 * @param response
	 * @param bloodrecord
	 */
	@RequestMapping(value = "/volunteers/createVolunteers")
	public void createVolunteers(HttpServletRequest request,
			HttpServletResponse response,
			@ModelAttribute("bloodrecord") JmhdVolunteerDonation bloodrecord){
		
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			serviceDao.save("JmhdVolunteerDonation", bloodrecord);
			map.put("success", Boolean.valueOf(true));
			map.put("message", "保存成功");
			HttpHelper.writeHdptJSON(map, response,true);
		} catch (Exception e) {
			map.put("success", Boolean.valueOf(false));
			map.put("message", "保存失败请重试");
			HttpHelper.writeHdptJSON(map, response,false);
		}

	}
}
