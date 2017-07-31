package dc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bsoft.xds.ILabReportRetrieveService;
import com.bsoft.xds.support.dao.IDAO;

import dc.domain.diary.JmhdUserIndicator;
import dc.domain.main.EhrManageunit;
import dc.domain.main.VjkwUserTips;
import dc.domain.main.YyUserAccount;
import dc.job.RssJob;
import dc.util.ConfigUtil;
import dc.util.HttpHelper;
import dc.util.Util;

@Controller
public class JmhdUserController {

	@Autowired
	private IDAO serviceDao;

	@Autowired
	private ILabReportRetrieveService PtLabReportService;

	/**
	 * 首页信息展示
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/jmhduser/main")
	public void main(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (request.getSession().getAttribute("map") != null) {
				String manaunitid = (String) request.getSession().getAttribute(
						"manaunitid");

				HashMap<String, Object> map = new HashMap<String, Object>();
				List<HashMap<String, Object>> list = RssJob.list;
				if (list == null || list.isEmpty()) {
					list = Util
							.RSSTest(ConfigUtil.getValue("healthe.ducation"));
					RssJob.setList(list);
				}
				map = cmsBulletinList(manaunitid, map);
				map.put("list", list);
				map.put("visitplan", visitplanList((String) request
						.getSession().getAttribute("mpiId")));
				HttpHelper.writeHdptJSON(map, response, true);
			}
		} catch (Exception e) {
			HttpHelper.writeHdptJSON(null, response, false);
		}

	}

	public HashMap<String, Object> cmsBulletinList(String manaunitid,
			HashMap<String, Object> ret) {
		if (!Util.empty(manaunitid)) {
			if (manaunitid.length() > 9) {
				manaunitid = manaunitid.substring(0, 9);
			}
			List<EhrManageunit> manageunitprops = serviceDao.queryList(
					"EhrManageunit", "manaunitid=?",
					new String[] { manaunitid });

			HashMap<String, Object> orgconfig = new HashMap<String, Object>();
			orgconfig.put("noticeSet", 0);
			if (manageunitprops.size() > 0) {
				EhrManageunit manageunitprop = (EhrManageunit) manageunitprops
						.get(0);

				orgconfig.put("regOrgId", manageunitprop.getManaunitid());

				if (!Util.empty(manageunitprop.getIntroduction())) {
					if (manageunitprop.getIntroduction().length() > 180) {
						String description = "";
						if (manageunitprop.getIntroduction().contains(
								"<![endif]-->"))
							description = Util
									.html2Text(manageunitprop
											.getIntroduction()
											.substring(
													manageunitprop
															.getIntroduction()
															.lastIndexOf(
																	"<![endif]-->"),
													manageunitprop
															.getIntroduction()
															.length()));
						else {
							description = Util
									.html2Text(manageunitprop
											.getIntroduction()
											.substring(
													manageunitprop
															.getIntroduction()
															.indexOf(
																	"<introduction>"),
													manageunitprop
															.getIntroduction()
															.indexOf(
																	"</introduction>")));
						}
						orgconfig.put("descrip", description);
					} else {
						orgconfig.put("descrip", Util.html2Text(manageunitprop
								.getIntroduction().substring(
										manageunitprop.getIntroduction()
												.lastIndexOf("<![endif]-->"),
										manageunitprop.getIntroduction()
												.length())));
					}
				} else
					orgconfig.put("descrip", "社区信息正在维护中......");

				ret.put("communitybulletin", cmsDocumentList(manaunitid));
				ret.put("orgconfig", orgconfig);
			} else {
				orgconfig.put("descrip", "社区信息正在维护中......");
				ret.put("orgconfig", orgconfig);
				ret.put("communitybulletin", cmsDocumentList(manaunitid));
			}
		} else {
			ret.put("communitybulletin", cmsDocumentList(manaunitid));
		}
		return ret;
	}

	public List<Map<String, Object>> cmsDocumentList(String manaunitid) {
		StringBuffer sql = new StringBuffer("CATEGORY= 1 ");
		if (!Util.empty(manaunitid)) {
			String id = "";
			if (manaunitid.length() > 9) {
				id = "('" + manaunitid + "','" + manaunitid.subSequence(0, 6)
						+ "','" + manaunitid.subSequence(0, 4) + "')";
			} else {
				id = "('" + manaunitid + "')";
			}
			sql.append("and ORG_ID in " + id);
		}
		sql.append(" order by filldate desc");
		Object[] o = null;
		return serviceDao.queryForPage("JmhdCmsDocument", sql.toString(), 0, 6,
				o);
	}

	/**
	 * 社区信息
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/jmhduser/orgInfo")
	public void orgInfo(HttpServletRequest request, HttpServletResponse response) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			String manaunitid = (String) request.getSession().getAttribute(
					"manaunitid");
			if(manaunitid!=null){
			if (manaunitid.length() > 9) {
				manaunitid = manaunitid.substring(0, 9);
			}
			List<EhrManageunit> manageunitprops = serviceDao.queryList(
					"EhrManageunit", "manaunitid=?",
					new String[] { manaunitid });

			if ((manageunitprops != null) && (manageunitprops.size() > 0)) {
				HashMap<String, Object> orgconfig = new HashMap<String, Object>();
				orgconfig.put("regOrgId", manaunitid);

				if ((((EhrManageunit) manageunitprops.get(0)).getIntroduction() != null)
						&& (((EhrManageunit) manageunitprops.get(0))
								.getIntroduction().length() > 0)) {
					String description = "";
					if (((EhrManageunit) manageunitprops.get(0))
							.getIntroduction().contains("<![endif]-->"))
						description = Util
								.html2Text(((EhrManageunit) manageunitprops
										.get(0))
										.getIntroduction()
										.substring(
												((EhrManageunit) manageunitprops
														.get(0))
														.getIntroduction()
														.lastIndexOf(
																"<![endif]-->"),
												(((EhrManageunit) manageunitprops
														.get(0))
														.getIntroduction()
														.length())));
					else {
						description = Util
								.html2Text(((EhrManageunit) manageunitprops
										.get(0))
										.getIntroduction()
										.substring(
												((EhrManageunit) manageunitprops
														.get(0))
														.getIntroduction()
														.indexOf(
																"<introduction>"),
												(((EhrManageunit) manageunitprops
														.get(0))
														.getIntroduction()
														.indexOf("</introduction>"))));
					}
					orgconfig.put("descrip", description);
				} else {
					orgconfig.put("descrip", "社区信息正在维护..");
				}

				map.put("orgConfig", orgconfig);
				map.put("manaunitname",
						((EhrManageunit) manageunitprops.get(0))
								.getManaunitname());
				map.put("success", Boolean.valueOf(true));
			} else {
				map.put("success", Boolean.valueOf(false));
			}
			}else{
				map.put("success", Boolean.valueOf(false));
			}
			HttpHelper.writeHdptJSON(map, response, true);
		} catch (Exception e) {
			map.put("success", Boolean.valueOf(false));
			HttpHelper.writeHdptJSON(null, response, false);
		}
	}

	/**
	 * 健康指标曲线
	 * 
	 * @param request
	 * @param response
	 * @param curveid
	 */
	@RequestMapping(value = "/jmhduser/curve")
	public void curve(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "curveid") String curveid) {
		try {
			HashMap<String, Object> map = null;
			String mpiId = (String) request.getSession().getAttribute("mpiId");
			YyUserAccount account = (YyUserAccount) request.getSession().getAttribute("map");

			if ("diary".equals(curveid)) {
				map = diaCurve(account.getUserid());
			} else {
				map = PtLabReportService.getHisCurve(mpiId);
			}

			HttpHelper.writeHdptJSON(map, response, true);
		} catch (Exception e) {
			HttpHelper.writeHdptJSON(null, response, false);
		}

	}

	public HashMap<String, Object> diaCurve(Long userid) {
		HashMap<String, Object> ret = new HashMap<String, Object>();
		List<JmhdUserIndicator> constrictionlist = serviceDao.queryList(
				"JmhdUserIndicator",
				"userId=? and indicatorId=2 and status='0' order by createDate desc",
				new Object[] { userid });

		List<JmhdUserIndicator> diastoliclist = serviceDao.queryList(
				"JmhdUserIndicator",
				"userId=? and indicatorId=3 and status='0' order by createDate desc",
				new Object[] { userid });
		
		List<JmhdUserIndicator> fbslist = serviceDao.queryList(
				"JmhdUserIndicator",
				"userId=? and indicatorId=7 and status='0' order by createDate desc",
				new Object[] { userid });
		
		List<JmhdUserIndicator> pbslist = serviceDao.queryList(
				"JmhdUserIndicator",
				"userId=? and indicatorId=8 and status='0' order by createDate desc",
				new Object[] { userid });
		
		int listsize = constrictionlist.size();
		if (listsize > 0) {
			if (listsize > 10) {
				listsize = 10;
			}
			String[] createdate = new String[listsize];
			float[] constriction = new float[listsize];
			float[] diastolic = new float[listsize];
			int j = 0;
			for (int i = listsize - 1; i >= 0; i--) {
				String cin = constrictionlist.get(i).getVal();
				if ((cin == null) || (cin == "")) {
					cin = "90";
				}
				constriction[j] = Float.parseFloat(cin);
				String dia = diastoliclist.get(i).getVal();
				if ((dia == null) || (dia == "")) {
					dia = "100";
				}
				diastolic[j] = Float.parseFloat(dia);

				createdate[j] = Util.dateTostring(constrictionlist.get(i)
						.getCreateDate(), "yyyy.MM.dd");
				j++;
			}
			ret.put("blooddate", createdate);
			ret.put("constriction", constriction);
			ret.put("diastolic", diastolic);
		}
		
		int size = fbslist.size();
		if (size > 0) {
			if (size > 10) {
				size = 10;
			}
			String[] createdate = new String[size];
			float[] fbs = new float[size];
			float[] pbs = new float[size];
			int j = 0;
			for (int i = size - 1; i >= 0; i--) {
				String f = fbslist.get(i).getVal();
				if ((f == null) || (f == "")) {
					f = "0";
				}
				fbs[j] = Float.parseFloat(f);
				String p = pbslist.get(i).getVal();
				if ((p == null) || (p == "")) {
					p = "0";
				}
				pbs[j] = Float.parseFloat(p);

				createdate[j] = Util.dateTostring(fbslist.get(i)
						.getCreateDate(), "yyyy.MM.dd");
				j++;
			}
			ret.put("glucosedate", createdate);
			ret.put("fbs", fbs);
			ret.put("pbs", pbs);
		}
//		int size1 = fbslist.size();
//		int size2 = pbslist.size();
//		String[] createdate1 = new String[size1];
//		String[] createdate2 = new String[size2];
//		if(size1 > 0){
//			if(size1 > 10){
//				size1 = 10;
//			}
//			float [] fbs = new float[size1];
//			int j = 0;
//			for (int i = size1 - 1; i >= 0; i--) {
//				String f = fbslist.get(i).getVal();
//				if ((f == null) || (f == "")) {
//					f = "0";
//				}
//				fbs[j] = Float.parseFloat(f);
//
//				createdate1[j] = Util.dateTostring(fbslist.get(i)
//						.getCreateDate(), "yyyy.MM.dd");
//				j++;
//			}
//			ret.put("glucosedate1", createdate1);
//			ret.put("fbs", fbs);
//		}
//		if(size2 > 0){
//			if(size2 > 10){
//				size2 = 10;
//			}
//			float [] pbs = new float[size2];
//			int j = 0;
//			for (int i = size2 - 1; i >= 0; i--) {
//				String p = pbslist.get(i).getVal();
//				if ((p == null) || (p == "")) {
//					p = "0";
//				}
//				pbs[j] = Float.parseFloat(p);
//
//				createdate2[j] = Util.dateTostring(pbslist.get(i)
//						.getCreateDate(), "yyyy.MM.dd");
//				j++;
//			}
//			ret.put("glucosedate2", createdate2);
//			ret.put("pbs", pbs);
//		}
		
		return ret;
	}
	
//	public HashMap<String, Object> diaCurve(String mpiId) {
//		HashMap<String, Object> ret = new HashMap<String, Object>();
//		List<JmhdUserIndicator> constrictionlist = serviceDao.queryList(
//				"JmhdUserIndicator",
//				"mpiid=? and indicatorId=2 order by createDate desc",
//				new String[] { mpiId });
//
//		List<JmhdUserIndicator> diastoliclist = serviceDao.queryList(
//				"JmhdUserIndicator",
//				"mpiid=? and indicatorId=3 order by createDate desc",
//				new String[] { mpiId });
//		int listsize = constrictionlist.size();
//		if (listsize > 0) {
//			if (listsize > 10) {
//				listsize = 10;
//			}
//			String[] createdate = new String[listsize];
//			int[] constriction = new int[listsize];
//			int[] diastolic = new int[listsize];
//			int j = 0;
//			for (int i = listsize - 1; i >= 0; i--) {
//				String cin = constrictionlist.get(i).getVal();
//				if ((cin == null) || (cin == "")) {
//					cin = "90";
//				}
//				constriction[j] = Integer.parseInt(cin);
//				String dia = diastoliclist.get(i).getVal();
//				if ((dia == null) || (dia == "")) {
//					dia = "100";
//				}
//				diastolic[j] = Integer.parseInt(dia);
//
//				createdate[j] = Util.dateTostring(constrictionlist.get(i)
//						.getCreateDate(), "yyyy.MM.dd");
//				j++;
//			}
//			ret.put("blooddate", createdate);
//			ret.put("glucosedate", "");
//			ret.put("constriction", constriction);
//			ret.put("diastolic", diastolic);
//			ret.put("fbs", "");
//			ret.put("pbs", "");
//		}
//		return ret;
//	}

	/**
	 * 检查session是否失效
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/jmhduser/check")
	public void check(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (request.getSession().getAttribute("map") != null) {
				HttpHelper.writeHdptJSON(null, response, true);
			} else {
				HttpHelper.writeHdptJSON(null, response, false);
			}

		} catch (Exception e) {
			HttpHelper.writeHdptJSON(null, response, false);
		}
	}

	/**
	 * 检查后台登陆用户session是否失效
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/check")
	public void adminCheck(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			if (request.getSession().getAttribute("data") != null) {
				HttpHelper.writeHdptJSON(null, response, true);
			} else {
				HttpHelper.writeHdptJSON(null, response, false);
			}

		} catch (Exception e) {
			HttpHelper.writeHdptJSON(null, response, false);
		}
	}

	public List<VjkwUserTips> visitplanList(String mpiId) {

		return serviceDao.queryList("VjkwUserTips",
				"empiid=? order by ptime desc", new String[] { mpiId });
	}
}
